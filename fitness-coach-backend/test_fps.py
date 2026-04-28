#!/usr/bin/env python3

import time
import sys
sys.path.append('.')

from app.storage.hybrid_storage import get_hybrid_sessions
from app.core.models import FrameInput, Joint, Language, Level

def test_fps():
    """Test frames per second processing"""
    print("🚀 Testing FPS performance...")
    
    # Create session
    hybrid_sessions = get_hybrid_sessions()
    session = hybrid_sessions.create_session(language=Language.en, level=Level.beginner)
    
    # Create test frame with full joint data
    frame = FrameInput(
        exercise='pushup',
        joints=[
            Joint(name='left_shoulder', x=100, y=200, confidence=0.9),
            Joint(name='right_shoulder', x=150, y=200, confidence=0.9),
            Joint(name='left_elbow', x=120, y=250, confidence=0.9),
            Joint(name='right_elbow', x=130, y=250, confidence=0.9),
            Joint(name='left_wrist', x=110, y=300, confidence=0.9),
            Joint(name='right_wrist', x=140, y=300, confidence=0.9),
            Joint(name='left_hip', x=110, y=350, confidence=0.9),
            Joint(name='right_hip', x=140, y=350, confidence=0.9),
            Joint(name='left_knee', x=115, y=400, confidence=0.9),
            Joint(name='right_knee', x=135, y=400, confidence=0.9),
            Joint(name='left_ankle', x=120, y=450, confidence=0.9),
            Joint(name='right_ankle', x=130, y=450, confidence=0.9),
        ],
        timestamp=time.time()
    )
    
    # Test different frame counts
    test_cases = [10, 50, 100, 200]
    
    for frame_count in test_cases:
        print(f"\n📊 Testing {frame_count} frames...")
        
        start = time.time()
        for i in range(frame_count):
            frame.timestamp = time.time() + i * 0.033  # 30 FPS timing
            result = session.engine.analyze(frame)
        end = time.time()
        
        duration = end - start
        fps = frame_count / duration
        avg_ms = (duration / frame_count) * 1000
        
        print(f"   ⏱️  Duration: {duration:.3f}s")
        print(f"   🎯 FPS: {fps:.1f}")
        print(f"   ⚡ Avg per frame: {avg_ms:.2f}ms")
        
        # Rate limiting check
        if fps > 30:
            print(f"   ⚠️  Note: Rate limited to 30 FPS per session")

if __name__ == "__main__":
    test_fps()
