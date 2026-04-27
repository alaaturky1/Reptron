from __future__ import annotations

import os

from fastapi.testclient import TestClient

from app.main import create_app


def _client_no_auth() -> TestClient:
    os.environ["FITCOACH_API_KEY"] = "legacy-test-key"
    os.environ["FITCOACH_REQUIRE_API_KEY"] = "true"
    app = create_app()
    client = TestClient(app)
    client.headers.update({"X-API-Key": "legacy-test-key"})
    return client


def test_legacy_rest_flow_start_analyze_end() -> None:
    client = _client_no_auth()

    start = client.post("/start-session", json={"language": "en", "level": "beginner"})
    assert start.status_code == 200
    data = start.json()
    assert "session_id" in data
    assert "ws_url" in data

    session_id = data["session_id"]
    analyze = client.post(
        "/analyze-frame",
        json={
            "session_id": session_id,
            "frame": {
                "exercise": "squat",
                "angles": {"knee_l": 175.0, "knee_r": 175.0, "torso_l_vs_vertical": 15.0},
                "timestamp": 0.0,
            },
        },
    )
    assert analyze.status_code == 200
    out = analyze.json()
    for key in ("feedback", "score", "issues", "rep_count", "exercise", "paused", "speak", "priority", "lang"):
        assert key in out

    summary = client.post("/end-session", json={"session_id": session_id})
    assert summary.status_code == 200
    s = summary.json()
    assert s["session_id"] == session_id
    assert "reps" in s
    assert "issues_tally" in s


def test_legacy_ws_analyze_contract() -> None:
    client = _client_no_auth()
    start = client.post("/start-session", json={"language": "en", "level": "beginner"})
    session_id = start.json()["session_id"]

    with client.websocket_connect(f"/ws/session/{session_id}?x_api_key=legacy-test-key") as ws:
        ws.send_json(
            {
                "exercise": "pushup",
                "angles": {"elbow_l": 175.0, "elbow_r": 175.0},
                "timestamp": 0.0,
            }
        )
        out = ws.receive_json()
        for key in ("feedback", "score", "issues", "rep_count", "exercise", "paused", "speak", "priority", "lang"):
            assert key in out
