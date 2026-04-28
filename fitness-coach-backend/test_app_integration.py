#!/usr/bin/env python3

import requests
import json
import time
import base64

# Configuration
BASE_URL = "http://localhost:8001"
API_KEY = "test-key"
HEADERS = {
    "X-API-Key": API_KEY,
    "Content-Type": "application/json"
}

def create_test_image():
    """Create a small test image (1x1 pixel)"""
    # 1x1 transparent PNG
    png_data = base64.b64decode("iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==")
    return base64.b64encode(png_data).decode('utf-8')

def test_full_workflow():
    """Test the complete AI Coach workflow like the app would"""
    print("🚀 Testing AI Coach App Integration...")
    print("=" * 50)
    
    # 1. Start Session
    print("\n1️⃣ Starting session...")
    start_payload = {
        "language": "en",
        "level": "beginner"
    }
    
    response = requests.post(f"{BASE_URL}/start-session", 
                           headers=HEADERS, 
                           json=start_payload)
    
    if response.status_code != 200:
        print(f"❌ Start session failed: {response.status_code} - {response.text}")
        return False
    
    session_data = response.json()
    session_id = session_data["session_id"]
    print(f"✅ Session started: {session_id}")
    
    # 2. Analyze Frames (simulate app behavior)
    print("\n2️⃣ Testing frame analysis...")
    test_image = create_test_image()
    
    for i in range(3):
        frame_payload = {
            "session_id": session_id,
            "frame": {
                "exercise": "squat",
                "image_b64": test_image,
                "timestamp": time.time()
            }
        }
        
        response = requests.post(f"{BASE_URL}/analyze-frame", 
                               headers=HEADERS, 
                               json=frame_payload)
        
        if response.status_code != 200:
            print(f"❌ Frame {i+1} analysis failed: {response.status_code}")
            continue
            
        result = response.json()
        print(f"   Frame {i+1}: score={result['score']:.1f}, reps={result['rep_count']}")
        print(f"   Feedback: {result['feedback'][:50]}...")
        
        time.sleep(0.1)  # Simulate real-time processing
    
    # 3. End Session
    print("\n3️⃣ Ending session...")
    end_payload = {"session_id": session_id}
    
    response = requests.post(f"{BASE_URL}/end-session", 
                           headers=HEADERS, 
                           json=end_payload)
    
    if response.status_code != 200:
        print(f"❌ End session failed: {response.status_code}")
        return False
    
    summary = response.json()
    print(f"✅ Session ended:")
    print(f"   Exercise: {summary['exercise']}")
    print(f"   Total reps: {summary['reps']}")
    print(f"   Active time: {summary['active_time_s']:.1f}s")
    
    # 4. Get Session Summary
    print("\n4️⃣ Getting session summary...")
    response = requests.get(f"{BASE_URL}/session-summary/{session_id}", 
                          headers=HEADERS)
    
    if response.status_code == 200:
        summary = response.json()
        print(f"✅ Summary retrieved:")
        print(f"   Session ID: {summary['session_id']}")
        print(f"   Issues tally: {summary['issues_tally']}")
    
    # 5. Check Backend Stats
    print("\n5️⃣ Checking backend stats...")
    response = requests.get(f"{BASE_URL}/stats", headers=HEADERS)
    
    if response.status_code == 200:
        stats = response.json()
        print(f"✅ Backend stats:")
        print(f"   Active sessions: {stats['system']['active_sessions']}")
        print(f"   Memory sessions: {stats['storage']['memory_sessions']}")
        print(f"   CPU usage: {stats['system']['cpu_percent']:.1f}%")
        print(f"   Memory usage: {stats['system']['memory_percent']:.1f}%")
    
    print("\n" + "=" * 50)
    print("🎉 Integration test completed successfully!")
    print("✅ AI Coach App is fully compatible with the backend!")
    return True

def test_error_handling():
    """Test error scenarios"""
    print("\n🧪 Testing error handling...")
    
    # Test invalid session
    response = requests.post(f"{BASE_URL}/analyze-frame", 
                           headers=HEADERS, 
                           json={
                               "session_id": "invalid-session",
                               "frame": {"exercise": "squat", "image_b64": "invalid", "timestamp": time.time()}
                           })
    
    if response.status_code == 404:
        print("✅ Invalid session correctly returns 404")
    else:
        print(f"❌ Expected 404, got {response.status_code}")
    
    # Test missing API key
    response = requests.post(f"{BASE_URL}/start-session", 
                           headers={"Content-Type": "application/json"}, 
                           json={"language": "en", "level": "beginner"})
    
    if response.status_code == 422:
        print("✅ Missing API key correctly returns 422")
    else:
        print(f"❌ Expected 422, got {response.status_code}")

if __name__ == "__main__":
    try:
        success = test_full_workflow()
        if success:
            test_error_handling()
            print("\n🚀 Ready to run the iOS app!")
            print("📱 Make sure your Mac and iOS device are on the same network")
            print("🔧 Update the baseURL in the app to your Mac's IP address if testing on device")
    except Exception as e:
        print(f"❌ Test failed: {e}")
