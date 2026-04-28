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

def debug_pose_extraction():
    print("🔍 Debugging Pose Extraction...")
    
    # Start session
    response = requests.post(f"{BASE_URL}/start-session", 
                           headers=HEADERS, 
                           json={"language": "en", "level": "beginner"})
    
    if response.status_code != 200:
        print(f"❌ Session start failed: {response.status_code}")
        return
    
    session_id = response.json()["session_id"]
    print(f"✅ Session: {session_id}")
    
    # Test with image only
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
        print(f"📊 Analysis Result:")
        print(f"   Score: {result['score']}")
        print(f"   Reps: {result['rep_count']}")
        print(f"   Feedback: {result['feedback']}")
        print(f"   Issues: {result['issues']}")
        print(f"   Paused: {result['paused']}")
        print(f"   Debug: {result['debug']}")
        
        # Check if pose was extracted
        if result['debug'].get('pose_extracted'):
            print("✅ Pose was extracted from image")
        else:
            print("❌ Pose was NOT extracted from image")
            
        # Check missing joints
        missing = result['debug'].get('missing_joints', [])
        if missing:
            print(f"❌ Missing joints: {missing}")
        else:
            print("✅ No missing joints")
            
    else:
        print(f"❌ Analysis failed: {response.status_code} - {response.text}")

def test_with_manual_joints():
    print("\n🧪 Testing with manual joints...")
    
    # Start session
    response = requests.post(f"{BASE_URL}/start-session", 
                           headers=HEADERS, 
                           json={"language": "en", "level": "beginner"})
    
    session_id = response.json()["session_id"]
    
    # Test with manual joints (backend format)
    frame_payload = {
        "session_id": session_id,
        "frame": {
            "exercise": "squat",
            "joints": [
                {"name": "shoulder_l", "x": 100, "y": 200, "confidence": 0.9},
                {"name": "shoulder_r", "x": 150, "y": 200, "confidence": 0.9},
                {"name": "hip_l", "x": 110, "y": 350, "confidence": 0.9},
                {"name": "hip_r", "x": 140, "y": 350, "confidence": 0.9},
                {"name": "knee_l", "x": 115, "y": 400, "confidence": 0.9},
                {"name": "knee_r", "x": 135, "y": 400, "confidence": 0.9},
                {"name": "ankle_l", "x": 120, "y": 450, "confidence": 0.9},
                {"name": "ankle_r", "x": 130, "y": 450, "confidence": 0.9},
            ],
            "timestamp": time.time()
        }
    }
    
    response = requests.post(f"{BASE_URL}/analyze-frame", 
                           headers=HEADERS, 
                           json=frame_payload)
    
    if response.status_code == 200:
        result = response.json()
        print(f"📊 Manual joints result:")
        print(f"   Score: {result['score']}")
        print(f"   Reps: {result['rep_count']}")
        print(f"   Feedback: {result['feedback']}")
        print(f"   Issues: {result['issues']}")
        print(f"   Paused: {result['paused']}")
        print(f"   Debug: {result['debug']}")
        
        if not result['paused']:
            print("🎉 SUCCESS! Manual joints work!")
        else:
            print("❌ Still paused with manual joints")
    else:
        print(f"❌ Manual joints failed: {response.status_code}")

if __name__ == "__main__":
    debug_pose_extraction()
    test_with_manual_joints()
