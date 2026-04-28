#!/usr/bin/env python3

import time
import sys
sys.path.append('.')

from app.analysis.pose_detector import PoseDetector

def test_pose_detection():
    print("🧪 Testing Pose Detection...")
    
    detector = PoseDetector()
    
    # Test with small image
    test_image = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg=="
    
    print("Testing with MediaPipe...")
    start = time.time()
    result = detector.detect_from_base64(test_image)
    end = time.time()
    
    print(f"⏱️  Detection time: {(end - start) * 1000:.2f}ms")
    print(f"📊 Confidence: {result.confidence}")
    print(f"🦴 Joints found: {len(result.joints)}")
    print(f"❌ Error: {result.error}")
    
    if result.joints:
        print("🎯 Sample joints:")
        for joint in result.joints[:3]:
            print(f"   {joint.name}: ({joint.x:.1f}, {joint.y:.1f}) conf={joint.confidence:.2f}")

if __name__ == "__main__":
    test_pose_detection()
