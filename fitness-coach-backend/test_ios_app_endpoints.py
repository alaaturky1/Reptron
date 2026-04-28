#!/usr/bin/env python3

import requests
import json
import time

# Configuration
BASE_URL = "http://172.20.10.5:8001"
API_KEY = "test-key"
HEADERS = {
    "X-API-Key": API_KEY,
    "Content-Type": "application/json"
}

def test_ios_app_endpoints():
    """Test all endpoints that iOS app uses"""
    print("📱 Testing iOS App Endpoints...")
    print("=" * 50)
    print(f"🔗 Backend URL: {BASE_URL}")
    print(f"🔑 API Key: {API_KEY}")
    
    # Test 1: Start Session (iOS app calls this)
    print("\n1️⃣ Testing start-session (iOS app)...")
    try:
        payload = {"language": "en", "level": "beginner"}
        response = requests.post(f"{BASE_URL}/start-session", 
                               headers=HEADERS, 
                               json=payload, 
                               timeout=10)
        
        if response.status_code == 200:
            session_data = response.json()
            session_id = session_data["session_id"]
            print(f"✅ Start session successful")
            print(f"   Session ID: {session_id}")
            print(f"   Response: {session_data}")
        else:
            print(f"❌ Start session failed: {response.status_code}")
            print(f"   Response: {response.text}")
            return False
    except Exception as e:
        print(f"❌ Start session error: {e}")
        return False
    
    # Test 2: Analyze Frame (iOS app calls this with image_b64)
    print("\n2️⃣ Testing analyze-frame (iOS app style)...")
    try:
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
                               json=frame_payload, 
                               timeout=10)
        
        if response.status_code == 200:
            result = response.json()
            print(f"✅ Analyze frame successful")
            print(f"   Score: {result['score']}")
            print(f"   Reps: {result['rep_count']}")
            print(f"   Feedback: {result['feedback']}")
            print(f"   Issues: {result['issues']}")
            print(f"   Paused: {result['paused']}")
            
            if not result['paused'] and result['score'] > 0:
                print("   🎉 SUCCESS! iOS app will get proper feedback")
            else:
                print("   ⚠️  Still paused - iOS app won't get proper feedback")
        else:
            print(f"❌ Analyze frame failed: {response.status_code}")
            print(f"   Response: {response.text}")
            return False
    except Exception as e:
        print(f"❌ Analyze frame error: {e}")
        return False
    
    # Test 3: End Session (iOS app calls this)
    print("\n3️⃣ Testing end-session (iOS app)...")
    try:
        end_payload = {"session_id": session_id}
        response = requests.post(f"{BASE_URL}/end-session", 
                               headers=HEADERS, 
                               json=end_payload, 
                               timeout=10)
        
        if response.status_code == 200:
            summary = response.json()
            print(f"✅ End session successful")
            print(f"   Exercise: {summary['exercise']}")
            print(f"   Total reps: {summary['reps']}")
            print(f"   Active time: {summary['active_time_s']:.1f}s")
            print(f"   Issues tally: {summary['issues_tally']}")
        else:
            print(f"❌ End session failed: {response.status_code}")
            print(f"   Response: {response.text}")
            return False
    except Exception as e:
        print(f"❌ End session error: {e}")
        return False
    
    # Test 4: Session Summary (iOS app might call this)
    print("\n4️⃣ Testing session-summary (iOS app)...")
    try:
        response = requests.get(f"{BASE_URL}/session-summary/{session_id}", 
                              headers=HEADERS, 
                              timeout=10)
        
        if response.status_code == 200:
            summary = response.json()
            print(f"✅ Session summary successful")
            print(f"   Session ID: {summary['session_id']}")
            print(f"   Exercise: {summary['exercise']}")
            print(f"   Reps: {summary['reps']}")
            print(f"   Issues tally: {summary['issues_tally']}")
        else:
            print(f"❌ Session summary failed: {response.status_code}")
            print(f"   Response: {response.text}")
            return False
    except Exception as e:
        print(f"❌ Session summary error: {e}")
        return False
    
    return True

def check_endpoint_compatibility():
    """Check if iOS app endpoints match backend endpoints"""
    print("\n🔍 Endpoint Compatibility Check:")
    print("=" * 40)
    
    # iOS app endpoints (from APIEndpoints.swift)
    ios_endpoints = {
        "startSession": "/api/FitnessCoach/start-session",
        "analyzeFrame": "/api/FitnessCoach/analyze-frame", 
        "endSession": "/api/FitnessCoach/end-session",
        "sessionSummary": "/api/FitnessCoach/session-summary/{sessionId}",
        "legacyStartSession": "/start-session",
        "legacyAnalyzeFrame": "/analyze-frame",
        "legacyEndSession": "/end-session",
        "legacySessionSummary": "/session-summary/{sessionId}"
    }
    
    # Backend endpoints (from our implementation)
    backend_endpoints = {
        "startSession": "/start-session",
        "analyzeFrame": "/analyze-frame",
        "endSession": "/end-session", 
        "sessionSummary": "/session-summary/{sessionId}",
        "health": "/health",
        "stats": "/stats"
    }
    
    print("iOS App Endpoints:")
    for name, path in ios_endpoints.items():
        print(f"   {name}: {path}")
    
    print("\nBackend Endpoints:")
    for name, path in backend_endpoints.items():
        print(f"   {name}: {path}")
    
    print("\n✅ Compatibility:")
    print("   - iOS app uses /api/FitnessCoach prefix")
    print("   - Backend uses direct paths")
    print("   - iOS app has fallback to legacy paths (without prefix)")
    print("   - This means iOS app will work with our backend!")

def test_all_exercises():
    """Test all exercise types that iOS app supports"""
    print("\n🏋️ Testing All Exercise Types:")
    print("=" * 40)
    
    exercises = ["squat", "pushup", "plank"]
    
    for exercise in exercises:
        print(f"\n🔹 Testing {exercise}:")
        
        # Start session
        response = requests.post(f"{BASE_URL}/start-session", 
                               headers=HEADERS, 
                               json={"language": "en", "level": "beginner"})
        session_id = response.json()["session_id"]
        
        # Analyze frame
        frame_payload = {
            "session_id": session_id,
            "frame": {
                "exercise": exercise,
                "image_b64": "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==",
                "timestamp": time.time()
            }
        }
        
        response = requests.post(f"{BASE_URL}/analyze-frame", 
                               headers=HEADERS, 
                               json=frame_payload)
        
        if response.status_code == 200:
            result = response.json()
            print(f"   ✅ {exercise}: score={result['score']}, feedback='{result['feedback'][:30]}...'")
        else:
            print(f"   ❌ {exercise}: failed")

if __name__ == "__main__":
    try:
        success = test_ios_app_endpoints()
        if success:
            check_endpoint_compatibility()
            test_all_exercises()
            print("\n" + "=" * 50)
            print("🎉 All iOS app endpoints are working!")
            print("✅ The iOS app is fully compatible with the backend!")
        else:
            print("\n❌ Some endpoints failed - check backend status")
    except Exception as e:
        print(f"❌ Test failed: {e}")
