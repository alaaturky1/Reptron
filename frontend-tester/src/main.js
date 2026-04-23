import "./style.css";

const app = document.querySelector("#app");

app.innerHTML = `
  <h1>Fitness Coach Backend Test Console</h1>
  <p>واجهة مخصصة لاختبار كل خصائص الباك (REST + WebSocket) بسرعة.</p>

  <div class="grid">
    <section class="card">
      <h2>الإعدادات</h2>
      <label>API Key (X-API-Key)</label>
      <input id="apiKey" placeholder="dev-secret" />
      <label>Language</label>
      <select id="language">
        <option value="en">en</option>
        <option value="ar">ar</option>
      </select>
      <label>Level</label>
      <select id="level">
        <option value="beginner">beginner</option>
        <option value="intermediate">intermediate</option>
        <option value="advanced">advanced</option>
      </select>

      <div class="row">
        <button id="healthBtn" class="secondary">GET /health</button>
        <button id="startBtn">POST /start-session</button>
      </div>
      <label>Current Session ID</label>
      <input id="sessionId" placeholder="auto from start-session" />
      <p class="hint">كل طلبات REST بتعدي عبر <span class="mono">/api</span> proxy على <span class="mono">localhost:8000</span>.</p>
    </section>

    <section class="card">
      <h2>analyze-frame (REST)</h2>
      <label>Frame JSON</label>
      <textarea id="frameJson" class="mono"></textarea>
      <div class="row">
        <button id="fillSquat" class="secondary">Squat Sample</button>
        <button id="fillVisibility" class="secondary">Low Visibility Sample</button>
      </div>
      <div class="row">
        <button id="analyzeBtn">POST /analyze-frame</button>
        <button id="summaryBtn" class="secondary">GET /session-summary</button>
        <button id="endBtn" class="secondary">POST /end-session</button>
      </div>
    </section>

    <section class="card">
      <h2>WebSocket Test</h2>
      <label>WS Frame JSON</label>
      <textarea id="wsFrameJson" class="mono"></textarea>
      <div class="row">
        <button id="wsConnectBtn">Connect</button>
        <button id="wsSendBtn" class="secondary">Send Frame</button>
        <button id="wsCloseBtn" class="secondary">Close</button>
      </div>
      <p class="hint">الاتصال على: <span class="mono">/ws/session/SESSION_ID?x_api_key=...</span></p>
    </section>

    <section class="card">
      <h2>Logs</h2>
      <pre id="logs"></pre>
      <button id="clearLogsBtn" class="secondary">Clear Logs</button>
    </section>
  </div>
`;

const apiKeyInput = byId("apiKey");
const languageInput = byId("language");
const levelInput = byId("level");
const sessionIdInput = byId("sessionId");
const frameJsonInput = byId("frameJson");
const wsFrameJsonInput = byId("wsFrameJson");
const logs = byId("logs");

let ws = null;

frameJsonInput.value = JSON.stringify(defaultSquatFrame(), null, 2);
wsFrameJsonInput.value = JSON.stringify(defaultWsFrame(), null, 2);
apiKeyInput.value = localStorage.getItem("tester_api_key") || "dev-secret";

apiKeyInput.addEventListener("input", () => {
  localStorage.setItem("tester_api_key", apiKeyInput.value.trim());
});

byId("fillSquat").addEventListener("click", () => {
  frameJsonInput.value = JSON.stringify(defaultSquatFrame(), null, 2);
});

byId("fillVisibility").addEventListener("click", () => {
  frameJsonInput.value = JSON.stringify(defaultLowVisibilityFrame(), null, 2);
});

byId("healthBtn").addEventListener("click", async () => {
  await callApi("GET", "/api/health");
});

byId("startBtn").addEventListener("click", async () => {
  const body = {
    language: languageInput.value,
    level: levelInput.value,
  };
  const data = await callApi("POST", "/api/start-session", body);
  if (data?.session_id) {
    sessionIdInput.value = data.session_id;
  }
});

byId("analyzeBtn").addEventListener("click", async () => {
  const session_id = sessionIdInput.value.trim();
  if (!session_id) return log("Missing session_id");
  const frame = parseJson(frameJsonInput.value, "Invalid frame JSON");
  if (!frame) return;
  await callApi("POST", "/api/analyze-frame", { session_id, frame });
});

byId("summaryBtn").addEventListener("click", async () => {
  const session_id = sessionIdInput.value.trim();
  if (!session_id) return log("Missing session_id");
  await callApi("GET", `/api/session-summary/${session_id}`);
});

byId("endBtn").addEventListener("click", async () => {
  const session_id = sessionIdInput.value.trim();
  if (!session_id) return log("Missing session_id");
  await callApi("POST", "/api/end-session", { session_id });
});

byId("wsConnectBtn").addEventListener("click", () => {
  const session_id = sessionIdInput.value.trim();
  if (!session_id) return log("Missing session_id");
  const key = apiKeyInput.value.trim();
  if (!key) return log("Missing API key");

  if (ws && ws.readyState === WebSocket.OPEN) {
    ws.close();
  }

  const protocol = location.protocol === "https:" ? "wss" : "ws";
  const url = `${protocol}://${location.host}/ws/session/${encodeURIComponent(session_id)}?x_api_key=${encodeURIComponent(key)}`;
  ws = new WebSocket(url);
  ws.onopen = () => log(`WS connected: ${url}`);
  ws.onmessage = (ev) => log(`WS message:\n${formatJsonMaybe(ev.data)}`);
  ws.onerror = () => log("WS error");
  ws.onclose = (ev) => log(`WS closed code=${ev.code}`);
});

byId("wsSendBtn").addEventListener("click", () => {
  if (!ws || ws.readyState !== WebSocket.OPEN) {
    return log("WS is not connected");
  }
  const frame = parseJson(wsFrameJsonInput.value, "Invalid WS frame JSON");
  if (!frame) return;
  ws.send(JSON.stringify(frame));
  log(`WS sent:\n${JSON.stringify(frame, null, 2)}`);
});

byId("wsCloseBtn").addEventListener("click", () => {
  if (ws) ws.close();
});

byId("clearLogsBtn").addEventListener("click", () => {
  logs.textContent = "";
});

async function callApi(method, url, body) {
  const key = apiKeyInput.value.trim();
  if (!key) {
    log("Missing API key");
    return null;
  }
  const headers = {
    "X-API-Key": key,
  };
  if (body !== undefined) headers["Content-Type"] = "application/json";

  log(`${method} ${url}${body !== undefined ? `\n${JSON.stringify(body, null, 2)}` : ""}`);

  try {
    const res = await fetch(url, {
      method,
      headers,
      body: body !== undefined ? JSON.stringify(body) : undefined,
    });
    const text = await res.text();
    const payload = formatJsonMaybe(text);
    log(`Response ${res.status}:\n${payload}`);
    return safeJsonParse(text);
  } catch (err) {
    log(`Request failed: ${err.message}`);
    return null;
  }
}

function byId(id) {
  return document.getElementById(id);
}

function parseJson(value, errorMessage) {
  try {
    return JSON.parse(value);
  } catch {
    log(errorMessage);
    return null;
  }
}

function safeJsonParse(value) {
  try {
    return JSON.parse(value);
  } catch {
    return null;
  }
}

function formatJsonMaybe(value) {
  if (typeof value !== "string") return JSON.stringify(value, null, 2);
  try {
    return JSON.stringify(JSON.parse(value), null, 2);
  } catch {
    return value;
  }
}

function log(message) {
  logs.textContent = `[${new Date().toLocaleTimeString()}] ${message}\n\n${logs.textContent}`;
}

function defaultSquatFrame() {
  return {
    exercise: "squat",
    timestamp: 0.25,
    angles: {
      knee_l: 118,
      knee_r: 121,
      torso_l_vs_vertical: 24,
      torso_r_vs_vertical: 22,
    },
  };
}

function defaultLowVisibilityFrame() {
  return {
    exercise: "squat",
    timestamp: 1.0,
    joints: [
      { name: "hip_l", x: 0.4, y: 0.5, confidence: 0.2 },
      { name: "knee_l", x: 0.45, y: 0.65, confidence: 0.2 },
    ],
  };
}

function defaultWsFrame() {
  return {
    exercise: "pushup",
    timestamp: 2.5,
    angles: {
      elbow_l: 95,
      elbow_r: 92,
      body_line_l: 168,
      body_line_r: 170,
    },
  };
}
