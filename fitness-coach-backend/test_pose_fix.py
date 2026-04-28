#!/usr/bin/env python3

import requests
import json
import time

# Configuration
BASE_URL = "http://192.168.1.11:8001"
API_KEY = "test-key"
HEADERS = {
    "X-API-Key": API_KEY,
    "Content-Type": "application/json"
}

def test_pose_fix():
    print("🔧 Testing Pose Fix...")
    
    # Start session
    response = requests.post(f"{BASE_URL}/start-session", 
                           headers=HEADERS, 
                           json={"language": "en", "level": "beginner"})
    
    session_id = response.json()["session_id"]
    print(f"✅ Session: {session_id}")
    
    # Test with image only (should work now)
    frame_payload = {
        "session_id": session_id,
        "frame": {
            "exercise": "squat",
            "image_b64": "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==",
            "timestamp": time.time()
        }
    }
    
    response = requests.post(f"{BASE_URL}/analyze-frame", 
                           headers=HEADERS, 
                           json=frame_payload)
    
    if response.status_code == 200:
        result = response.json()
        print(f"📊 Result:")
        print(f"   Score: {result['score']}")
        print(f"   Reps: {result['rep_count']}")
        print(f"   Feedback: {result['feedback']}")
        print(f"   Issues: {result['issues']}")
        print(f"   Paused: {result['paused']}")
        
        if not result['paused'] and result['score'] > 0:
            print("🎉 SUCCESS! Pose extraction is working!")
            print("✅ iOS app will now work with image-only frames")
        else:
            print("❌ Still not working - need more debugging")
    else:
        print(f"❌ Failed: {response.status_code}")

if __name__ == "__main__":
    test_pose_fix()
