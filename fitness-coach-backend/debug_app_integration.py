#!/usr/bin/env python3

import requests
import json
import time
import base64

# Configuration
BASE_URL = "http://192.168.1.11:8001"
API_KEY = "test-key"
HEADERS = {
    "X-API-Key": API_KEY,
    "Content-Type": "application/json"
}

def create_realistic_frame_data():
    """Create more realistic frame data with joints (not just image)"""
    return {
        "session_id": "test-session",
        "frame": {
            "exercise": "squat",
            "joints": [
                {"name": "left_shoulder", "x": 100, "y": 200, "confidence": 0.9},
                {"name": "right_shoulder", "x": 150, "y": 200, "confidence": 0.9},
                {"name": "left_elbow", "x": 120, "y": 250, "confidence": 0.9},
                {"name": "right_elbow", "x": 130, "y": 250, "confidence": 0.9},
                {"name": "left_wrist", "x": 110, "y": 300, "confidence": 0.9},
                {"name": "right_wrist", "x": 140, "y": 300, "confidence": 0.9},
                {"name": "left_hip", "x": 110, "y": 350, "confidence": 0.9},
                {"name": "right_hip", "x": 140, "y": 350, "confidence": 0.9},
                {"name": "left_knee", "x": 115, "y": 400, "confidence": 0.9},
                {"name": "right_knee", "x": 135, "y": 400, "confidence": 0.9},
                {"name": "left_ankle", "x": 120, "y": 450, "confidence": 0.9},
                {"name": "right_ankle", "x": 130, "y": 450, "confidence": 0.9},
            ],
            "angles": {
                "left_knee": 90.0,
                "right_knee": 90.0,
                "left_hip": 180.0,
                "right_hip": 180.0,
                "torso_lean": 5.0
            },
            "timestamp": time.time()
        }
    }

def test_with_joints():
    """Test with joints data (what the backend expects)"""
    print("🧪 Testing with joints data (backend expects this)...")
    
    # Start session
    response = requests.post(f"{BASE_URL}/start-session", 
                           headers=HEADERS, 
                           json={"language": "en", "level": "beginner"})
    
    if response.status_code != 200:
        print(f"❌ Session start failed: {response.status_code}")
        return False
    
    session_id = response.json()["session_id"]
    print(f"✅ Session started: {session_id}")
    
    # Test with joints
    frame_data = create_realistic_frame_data()
    frame_data["session_id"] = session_id
    
    response = requests.post(f"{BASE_URL}/analyze-frame", 
                           headers=HEADERS, 
                           json=frame_data)
    
    if response.status_code == 200:
        result = response.json()
        print(f"✅ Frame with joints analyzed:")
        print(f"   Score: {result['score']:.1f}")
        print(f"   Reps: {result['rep_count']}")
        print(f"   Feedback: {result['feedback']}")
        print(f"   Issues: {result['issues']}")
        print(f"   Paused: {result['paused']}")
        return True
    else:
        print(f"❌ Frame analysis failed: {response.status_code} - {response.text}")
        return False

def test_with_image_only():
    """Test with image only (what iOS app sends)"""
    print("\n🧪 Testing with image only (what iOS app sends)...")
    
    # Start session
    response = requests.post(f"{BASE_URL}/start-session", 
                           headers=HEADERS, 
                           json={"language": "en", "level": "beginner"})
    
    if response.status_code != 200:
        print(f"❌ Session start failed: {response.status_code}")
        return False
    
    session_id = response.json()["session_id"]
    print(f"✅ Session started: {session_id}")
    
    # Test with image only (iOS app style)
    frame_data = {
        "session_id": session_id,
        "frame": {
            "exercise": "squat",
            "image_b64": "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==",
            "timestamp": time.time()
        }
    }
    
    response = requests.post(f"{BASE_URL}/analyze-frame", 
                           headers=HEADERS, 
                           json=frame_data)
    
    if response.status_code == 200:
        result = response.json()
        print(f"✅ Frame with image analyzed:")
        print(f"   Score: {result['score']:.1f}")
        print(f"   Reps: {result['rep_count']}")
        print(f"   Feedback: {result['feedback']}")
        print(f"   Issues: {result['issues']}")
        print(f"   Paused: {result['paused']}")
        return True
    else:
        print(f"❌ Frame analysis failed: {response.status_code} - {response.text}")
        return False

def test_app_behavior():
    """Test exactly like the iOS app behaves"""
    print("\n📱 Testing iOS app behavior...")
    
    # Simulate iOS app flow
    try:
        # 1. Start session
        response = requests.post(f"{BASE_URL}/start-session", 
                               headers=HEADERS, 
                               json={"language": "en", "level": "beginner"})
        
        if response.status_code != 200:
            print(f"❌ iOS app session start failed: {response.status_code}")
            return False
        
        session_id = response.json()["session_id"]
        print(f"✅ iOS app session: {session_id}")
        
        # 2. Send frames like iOS app (image only)
        for i in range(3):
            frame_payload = {
                "session_id": session_id,
                "frame": {
                    "exercise": "squat",
                    "image_b64": "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==",
                    "timestamp": time.time() + i * 0.1
                }
            }
            
            response = requests.post(f"{BASE_URL}/analyze-frame", 
                                   headers=HEADERS, 
                                   json=frame_payload)
            
            if response.status_code == 200:
                result = response.json()
                print(f"   Frame {i+1}: score={result['score']:.1f}, reps={result['rep_count']}")
                print(f"   Feedback: {result['feedback'][:50]}...")
                print(f"   Issues: {result['issues']}")
                print(f"   Paused: {result['paused']}")
            else:
                print(f"❌ Frame {i+1} failed: {response.status_code}")
                return False
            
            time.sleep(0.1)
        
        # 3. End session
        response = requests.post(f"{BASE_URL}/end-session", 
                               headers=HEADERS, 
                               json={"session_id": session_id})
        
        if response.status_code == 200:
            summary = response.json()
            print(f"✅ Session ended:")
            print(f"   Exercise: {summary['exercise']}")
            print(f"   Total reps: {summary['reps']}")
            print(f"   Active time: {summary['active_time_s']:.1f}s")
            return True
        else:
            print(f"❌ Session end failed: {response.status_code}")
            return False
            
    except Exception as e:
        print(f"❌ iOS app simulation failed: {e}")
        return False

def diagnose_problem():
    """Diagnose the main issue"""
    print("\n🔍 DIAGNOSIS:")
    print("=" * 50)
    
    # Test 1: Backend health
    print("1. Backend Health:")
    try:
        response = requests.get(f"{BASE_URL}/health", headers=HEADERS, timeout=5)
        if response.status_code == 200:
            print("   ✅ Backend is running and responding")
        else:
            print(f"   ❌ Backend health check failed: {response.status_code}")
    except Exception as e:
        print(f"   ❌ Cannot reach backend: {e}")
        return
    
    # Test 2: Session creation
    print("\n2. Session Creation:")
    try:
        response = requests.post(f"{BASE_URL}/start-session", 
                               headers=HEADERS, 
                               json={"language": "en", "level": "beginner"})
        if response.status_code == 200:
            print("   ✅ Session creation works")
        else:
            print(f"   ❌ Session creation failed: {response.status_code}")
    except Exception as e:
        print(f"   ❌ Session creation error: {e}")
    
    # Test 3: Frame analysis
    print("\n3. Frame Analysis:")
    try:
        # Start session first
        session_response = requests.post(f"{BASE_URL}/start-session", 
                                       headers=HEADERS, 
                                       json={"language": "en", "level": "beginner"})
        session_id = session_response.json()["session_id"]
        
        # Test frame
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
            print(f"   ✅ Frame analysis works")
            print(f"   📊 Score: {result['score']}, Reps: {result['rep_count']}")
            print(f"   💬 Feedback: {result['feedback']}")
            print(f"   ⚠️  Issues: {result['issues']}")
            print(f"   ⏸️  Paused: {result['paused']}")
            
            if result['paused']:
                print("   🚨 ISSUE: Backend is PAUSED because it can't analyze the frame!")
                print("   🔧 This means the backend needs joints/angles data, not just image")
            else:
                print("   ✅ Backend is actively analyzing frames")
        else:
            print(f"   ❌ Frame analysis failed: {response.status_code}")
    except Exception as e:
        print(f"   ❌ Frame analysis error: {e}")

if __name__ == "__main__":
    print("🔍 DIAGNOSING FITNESS COACH INTEGRATION")
    print("=" * 50)
    
    # Run all tests
    test_with_joints()
    test_with_image_only()
    test_app_behavior()
    diagnose_problem()
    
    print("\n" + "=" * 50)
    print("🎯 LIKELY ISSUE:")
    print("The backend expects joints/angles data but the iOS app sends only image_b64")
    print("The backend pauses analysis when it can't detect proper pose data")
    print("Solution: Either add pose detection to iOS app or modify backend to handle images")
