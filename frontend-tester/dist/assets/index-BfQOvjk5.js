(function(){const s=document.createElement("link").relList;if(s&&s.supports&&s.supports("modulepreload"))return;for(const t of document.querySelectorAll('link[rel="modulepreload"]'))l(t);new MutationObserver(t=>{for(const i of t)if(i.type==="childList")for(const c of i.addedNodes)c.tagName==="LINK"&&c.rel==="modulepreload"&&l(c)}).observe(document,{childList:!0,subtree:!0});function r(t){const i={};return t.integrity&&(i.integrity=t.integrity),t.referrerPolicy&&(i.referrerPolicy=t.referrerPolicy),t.crossOrigin==="use-credentials"?i.credentials="include":t.crossOrigin==="anonymous"?i.credentials="omit":i.credentials="same-origin",i}function l(t){if(t.ep)return;t.ep=!0;const i=r(t);fetch(t.href,i)}})();const h=document.querySelector("#app");h.innerHTML=`
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
`;const d=n("apiKey"),_=n("language"),w=n("level"),u=n("sessionId"),f=n("frameJson"),m=n("wsFrameJson"),y=n("logs");let a=null;f.value=JSON.stringify(g(),null,2);m.value=JSON.stringify(I(),null,2);d.value=localStorage.getItem("tester_api_key")||"dev-secret";d.addEventListener("input",()=>{localStorage.setItem("tester_api_key",d.value.trim())});n("fillSquat").addEventListener("click",()=>{f.value=JSON.stringify(g(),null,2)});n("fillVisibility").addEventListener("click",()=>{f.value=JSON.stringify(L(),null,2)});n("healthBtn").addEventListener("click",async()=>{await p("GET","/api/health")});n("startBtn").addEventListener("click",async()=>{const e={language:_.value,level:w.value},s=await p("POST","/api/start-session",e);s?.session_id&&(u.value=s.session_id)});n("analyzeBtn").addEventListener("click",async()=>{const e=u.value.trim();if(!e)return o("Missing session_id");const s=S(f.value,"Invalid frame JSON");s&&await p("POST","/api/analyze-frame",{session_id:e,frame:s})});n("summaryBtn").addEventListener("click",async()=>{const e=u.value.trim();if(!e)return o("Missing session_id");await p("GET",`/api/session-summary/${e}`)});n("endBtn").addEventListener("click",async()=>{const e=u.value.trim();if(!e)return o("Missing session_id");await p("POST","/api/end-session",{session_id:e})});n("wsConnectBtn").addEventListener("click",()=>{const e=u.value.trim();if(!e)return o("Missing session_id");const s=d.value.trim();if(!s)return o("Missing API key");a&&a.readyState===WebSocket.OPEN&&a.close();const l=`${location.protocol==="https:"?"wss":"ws"}://${location.host}/ws/session/${encodeURIComponent(e)}?x_api_key=${encodeURIComponent(s)}`;a=new WebSocket(l),a.onopen=()=>o(`WS connected: ${l}`),a.onmessage=t=>o(`WS message:
${v(t.data)}`),a.onerror=()=>o("WS error"),a.onclose=t=>o(`WS closed code=${t.code}`)});n("wsSendBtn").addEventListener("click",()=>{if(!a||a.readyState!==WebSocket.OPEN)return o("WS is not connected");const e=S(m.value,"Invalid WS frame JSON");e&&(a.send(JSON.stringify(e)),o(`WS sent:
${JSON.stringify(e,null,2)}`))});n("wsCloseBtn").addEventListener("click",()=>{a&&a.close()});n("clearLogsBtn").addEventListener("click",()=>{y.textContent=""});async function p(e,s,r){const l=d.value.trim();if(!l)return o("Missing API key"),null;const t={"X-API-Key":l};r!==void 0&&(t["Content-Type"]="application/json"),o(`${e} ${s}${r!==void 0?`
${JSON.stringify(r,null,2)}`:""}`);try{const i=await fetch(s,{method:e,headers:t,body:r!==void 0?JSON.stringify(r):void 0}),c=await i.text(),b=v(c);return o(`Response ${i.status}:
${b}`),O(c)}catch(i){return o(`Request failed: ${i.message}`),null}}function n(e){return document.getElementById(e)}function S(e,s){try{return JSON.parse(e)}catch{return o(s),null}}function O(e){try{return JSON.parse(e)}catch{return null}}function v(e){if(typeof e!="string")return JSON.stringify(e,null,2);try{return JSON.stringify(JSON.parse(e),null,2)}catch{return e}}function o(e){y.textContent=`[${new Date().toLocaleTimeString()}] ${e}

${y.textContent}`}function g(){return{exercise:"squat",timestamp:.25,angles:{knee_l:118,knee_r:121,torso_l_vs_vertical:24,torso_r_vs_vertical:22}}}function L(){return{exercise:"squat",timestamp:1,joints:[{name:"hip_l",x:.4,y:.5,confidence:.2},{name:"knee_l",x:.45,y:.65,confidence:.2}]}}function I(){return{exercise:"pushup",timestamp:2.5,angles:{elbow_l:95,elbow_r:92,body_line_l:168,body_line_r:170}}}
