from __future__ import annotations

import os
from dataclasses import dataclass


def _get_bool(name: str, default: bool) -> bool:
    v = os.getenv(name)
    if v is None:
        return default
    return v.strip().lower() in {"1", "true", "yes", "y", "on"}


@dataclass(frozen=True)
class Settings:
    api_key: str | None
    require_api_key: bool
    log_level: str
    redis_url: str
    use_redis: bool


def get_settings() -> Settings:
    api_key = os.getenv("FITCOACH_API_KEY")
    require_api_key = _get_bool("FITCOACH_REQUIRE_API_KEY", default=True)
    log_level = os.getenv("FITCOACH_LOG_LEVEL", "INFO").upper()
    redis_url = os.getenv("FITCOACH_REDIS_URL", "redis://localhost:6379")
    use_redis = _get_bool("FITCOACH_USE_REDIS", default=True)
    return Settings(
        api_key=api_key, 
        require_api_key=require_api_key, 
        log_level=log_level,
        redis_url=redis_url,
        use_redis=use_redis
    )

