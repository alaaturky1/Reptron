#!/usr/bin/env python3

import requests
import json
import time

# Configuration for device testing
DEVICE_BASE_URL = "http://192.168.1.11:8001"
API_KEY = "test-key"
HEADERS = {
    "X-API-Key": API_KEY,
    "Content-Type": "application/json"
}

def test_device_connection():
    """Test connection from external device (like iOS app)"""
    print("📱 Testing Device Access to Backend...")
    print("=" * 50)
    print(f"🔗 Backend URL: {DEVICE_BASE_URL}")
    print(f"🔑 API Key: {API_KEY}")
    
    # Test 1: Health Check
    print("\n1️⃣ Testing health endpoint...")
    try:
        response = requests.get(f"{DEVICE_BASE_URL}/health", headers=HEADERS, timeout=10)
        if response.status_code == 200:
            print("✅ Health check successful")
            print(f"   Response: {response.json()}")
        else:
            print(f"❌ Health check failed: {response.status_code}")
            return False
    except Exception as e:
        print(f"❌ Connection failed: {e}")
        return False
    
    # Test 2: Start Session
    print("\n2️⃣ Testing session start...")
    try:
        payload = {"language": "en", "level": "beginner"}
        response = requests.post(f"{DEVICE_BASE_URL}/start-session", 
                               headers=HEADERS, 
                               json=payload, 
                               timeout=10)
        
        if response.status_code == 200:
            session_data = response.json()
            session_id = session_data["session_id"]
            print(f"✅ Session started: {session_id}")
        else:
            print(f"❌ Session start failed: {response.status_code}")
            return False
    except Exception as e:
        print(f"❌ Session start failed: {e}")
        return False
    
    # Test 3: Analyze Frame
    print("\n3️⃣ Testing frame analysis...")
    try:
        frame_payload = {
            "session_id": session_id,
            "frame": {
                "exercise": "squat",
                "image_b64": "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==",
                "timestamp": time.time()
            }
        }
        
        response = requests.post(f"{DEVICE_BASE_URL}/analyze-frame", 
                               headers=HEADERS, 
                               json=frame_payload, 
                               timeout=10)
        
        if response.status_code == 200:
            result = response.json()
            print(f"✅ Frame analyzed: score={result['score']:.1f}, reps={result['rep_count']}")
            print(f"   Feedback: {result['feedback'][:50]}...")
        else:
            print(f"❌ Frame analysis failed: {response.status_code}")
            return False
    except Exception as e:
        print(f"❌ Frame analysis failed: {e}")
        return False
    
    # Test 4: End Session
    print("\n4️⃣ Testing session end...")
    try:
        end_payload = {"session_id": session_id}
        response = requests.post(f"{DEVICE_BASE_URL}/end-session", 
                               headers=HEADERS, 
                               json=end_payload, 
                               timeout=10)
        
        if response.status_code == 200:
            summary = response.json()
            print(f"✅ Session ended:")
            print(f"   Exercise: {summary['exercise']}")
            print(f"   Total reps: {summary['reps']}")
        else:
            print(f"❌ Session end failed: {response.status_code}")
            return False
    except Exception as e:
        print(f"❌ Session end failed: {e}")
        return False
    
    # Test 5: Backend Stats
    print("\n5️⃣ Testing backend stats...")
    try:
        response = requests.get(f"{DEVICE_BASE_URL}/stats", headers=HEADERS, timeout=10)
        
        if response.status_code == 200:
            stats = response.json()
            print(f"✅ Backend stats:")
            print(f"   Active sessions: {stats['system']['active_sessions']}")
            print(f"   CPU usage: {stats['system']['cpu_percent']:.1f}%")
            print(f"   Memory usage: {stats['system']['memory_percent']:.1f}%")
        else:
            print(f"❌ Stats failed: {response.status_code}")
            return False
    except Exception as e:
        print(f"❌ Stats failed: {e}")
        return False
    
    print("\n" + "=" * 50)
    print("🎉 All device tests passed!")
    print("✅ Backend is ready for iOS device testing!")
    return True

def check_network_requirements():
    """Check network requirements for device testing"""
    print("\n🌐 Network Requirements Check:")
    print("=" * 40)
    
    print("✅ Backend running on: http://192.168.1.11:8001")
    print("✅ Server bound to: 0.0.0.0 (all interfaces)")
    print("✅ API Key configured: test-key")
    
    print("\n📱 iOS Device Requirements:")
    print("1. Connect to same WiFi network as Mac")
    print("2. Ensure Mac firewall allows connections on port 8001")
    print("3. Update app's baseURL to: http://192.168.1.11:8001")
    print("4. Build and run the app on device")
    
    print("\n🔧 Troubleshooting:")
    print("- If connection fails, check Mac firewall settings")
    print("- Ensure both devices are on same subnet (192.168.1.x)")
    print("- Try pinging the Mac from the iOS device")
    print("- Check if port 8001 is accessible from device")

if __name__ == "__main__":
    try:
        success = test_device_connection()
        if success:
            check_network_requirements()
    except Exception as e:
        print(f"❌ Test failed: {e}")
