/**
 * Capture current video frame → resize (max side) → JPEG → base64 data URL payload only.
 * @param {HTMLVideoElement} video
 * @param {number} maxSide default 480
 * @param {number} quality JPEG 0..1
 * @returns {string} base64 without data: prefix, or empty string if not ready
 */
export function captureFrameAsJpegBase64(video, maxSide = 480, quality = 0.5) {
  if (!video || video.readyState < 2) return "";

  const vw = video.videoWidth;
  const vh = video.videoHeight;
  if (!vw || !vh) return "";

  let w = vw;
  let h = vh;
  const scale = Math.min(1, maxSide / Math.max(w, h));
  w = Math.round(w * scale);
  h = Math.round(h * scale);

  const canvas = document.createElement("canvas");
  canvas.width = w;
  canvas.height = h;
  const ctx = canvas.getContext("2d");
  if (!ctx) return "";
  ctx.drawImage(video, 0, 0, w, h);

  const dataUrl = canvas.toDataURL("image/jpeg", quality);
  const comma = dataUrl.indexOf(",");
  if (comma === -1) return "";
  return dataUrl.slice(comma + 1);
}

export function buildAnalyzePayload(sessionId, base64NoPrefix) {
  return {
    sessionId,
    frameBase64: base64NoPrefix,
    mimeType: "image/jpeg",
    timestamp: Date.now(),
  };
}
