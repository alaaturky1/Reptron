from __future__ import annotations

import json
import pickle
from dataclasses import dataclass, field
from typing import Any, Optional

import aioredis
from aioredis import Redis

from app.analysis.engine import CoachingEngine, new_session_engine
from app.core.config import get_settings
from app.core.models import Language, Level


@dataclass
class RedisSession:
    session_id: str
    engine_data: dict[str, Any]
    ended: bool = False
    created_at: float = field(default_factory=lambda: __import__('time').time())
    last_accessed: float = field(default_factory=lambda: __import__('time').time())


class RedisSessions:
    def __init__(self) -> None:
        self._redis: Optional[Redis] = None
        self._settings = get_settings()
        self._session_ttl = 3600  # 1 hour
        self._key_prefix = "fitness_session:"

    async def get_redis(self) -> Redis:
        if self._redis is None:
            redis_url = getattr(self._settings, 'redis_url', 'redis://localhost:6379')
            self._redis = await aioredis.from_url(redis_url, decode_responses=False)
        return self._redis

    def _get_session_key(self, session_id: str) -> str:
        return f"{self._key_prefix}{session_id}"

    async def create_session(self, language: Language, level: Level) -> RedisSession:
        engine = new_session_engine(language=language, level=level.value)
        
        # Serialize engine state
        engine_data = {
            'session_id': engine.session_id,
            'language': engine.language.value,
            'level': engine.level,
            'exercise': engine.exercise,
            'rep_records': [
                {
                    'rep_index': r.rep_index,
                    'score': r.score,
                    'issues': r.issues
                } for r in engine.rep_records
            ],
            'issues_tally': engine.issues_tally,
            'last_timestamp': engine.last_timestamp,
            'active_time_s': engine.active_time_s,
            'idle_time_s': engine.idle_time_s,
            'last_rep_timestamp': engine.last_rep_timestamp,
            'min_joint_confidence': engine.min_joint_confidence,
            'rep_cooldown_s': engine.rep_cooldown_s,
        }
        
        session = RedisSession(
            session_id=engine.session_id,
            engine_data=engine_data
        )
        
        redis = await self.get_redis()
        session_key = self._get_session_key(session.session_id)
        await redis.setex(
            session_key, 
            self._session_ttl, 
            pickle.dumps(session)
        )
        
        return session

    async def get(self, session_id: str) -> Optional[RedisSession]:
        redis = await self.get_redis()
        session_key = self._get_session_key(session_id)
        
        data = await redis.get(session_key)
        if data is None:
            return None
            
        try:
            session = pickle.loads(data)
            session.last_accessed = __import__('time').time()
            
            # Update TTL on access
            await redis.expire(session_key, self._session_ttl)
            
            return session
        except (pickle.PickleError, AttributeError, TypeError):
            # Corrupted data, remove it
            await redis.delete(session_key)
            return None

    async def update_session(self, session: RedisSession) -> None:
        redis = await self.get_redis()
        session_key = self._get_session_key(session.session_id)
        
        await redis.setex(
            session_key,
            self._session_ttl,
            pickle.dumps(session)
        )

    async def delete_session(self, session_id: str) -> bool:
        redis = await self.get_redis()
        session_key = self._get_session_key(session_id)
        
        result = await redis.delete(session_key)
        return result > 0

    async def get_active_sessions_count(self) -> int:
        redis = await self.get_redis()
        pattern = f"{self._key_prefix}*"
        keys = await redis.keys(pattern)
        return len(keys)

    async def cleanup_expired_sessions(self) -> int:
        """Clean up expired sessions (handled automatically by Redis TTL)"""
        # Redis handles TTL automatically, but we can force cleanup if needed
        return 0

    async def close(self) -> None:
        if self._redis:
            await self._redis.close()
            self._redis = None


# Global instance
redis_sessions = RedisSessions()


async def get_redis_sessions() -> RedisSessions:
    return redis_sessions
