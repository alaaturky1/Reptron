import * as poseDetection from "@tensorflow-models/pose-detection";
import "@tensorflow/tfjs-backend-webgl";

const NAME_MAP = {
  left_shoulder: "shoulder_l",
  right_shoulder: "shoulder_r",
  left_elbow: "elbow_l",
  right_elbow: "elbow_r",
  left_wrist: "wrist_l",
  right_wrist: "wrist_r",
  left_hip: "hip_l",
  right_hip: "hip_r",
  left_knee: "knee_l",
  right_knee: "knee_r",
  left_ankle: "ankle_l",
  right_ankle: "ankle_r",
};

function angleDeg(a, b, c) {
  if (!a || !b || !c) return null;
  const abx = a.x - b.x;
  const aby = a.y - b.y;
  const cbx = c.x - b.x;
  const cby = c.y - b.y;
  const dot = abx * cbx + aby * cby;
  const mag1 = Math.hypot(abx, aby);
  const mag2 = Math.hypot(cbx, cby);
  if (mag1 < 1e-6 || mag2 < 1e-6) return null;
  const cos = Math.max(-1, Math.min(1, dot / (mag1 * mag2)));
  return (Math.acos(cos) * 180) / Math.PI;
}

function torsoVsVerticalDeg(shoulder, hip) {
  if (!shoulder || !hip) return null;
  const vx = shoulder.x - hip.x;
  const vy = shoulder.y - hip.y;
  const mag = Math.hypot(vx, vy);
  if (mag < 1e-6) return null;
  // Angle against image vertical axis.
  const dot = Math.abs(vy);
  const cos = Math.max(-1, Math.min(1, dot / mag));
  return (Math.acos(cos) * 180) / Math.PI;
}

function extractMappedPoints(pose) {
  const byName = {};
  for (const kp of pose?.keypoints || []) {
    if (!kp?.name || !(kp.name in NAME_MAP)) continue;
    const mapped = NAME_MAP[kp.name];
    byName[mapped] = {
      name: mapped,
      x: kp.x,
      y: kp.y,
      confidence: typeof kp.score === "number" ? kp.score : null,
    };
  }
  return byName;
}

function computeAngles(points) {
  const out = {};
  const add = (key, value) => {
    if (typeof value === "number" && Number.isFinite(value)) out[key] = value;
  };
  add("knee_l", angleDeg(points.hip_l, points.knee_l, points.ankle_l));
  add("knee_r", angleDeg(points.hip_r, points.knee_r, points.ankle_r));
  add("elbow_l", angleDeg(points.shoulder_l, points.elbow_l, points.wrist_l));
  add("elbow_r", angleDeg(points.shoulder_r, points.elbow_r, points.wrist_r));
  add("torso_l_vs_vertical", torsoVsVerticalDeg(points.shoulder_l, points.hip_l));
  add("torso_r_vs_vertical", torsoVsVerticalDeg(points.shoulder_r, points.hip_r));
  return out;
}

export async function createPoseAnalyzer() {
  const detector = await poseDetection.createDetector(poseDetection.SupportedModels.BlazePose, {
    runtime: "mediapipe",
    modelType: "lite",
    solutionPath: "https://cdn.jsdelivr.net/npm/@mediapipe/pose",
  });

  return {
    async buildFrame(video, timestampMs, exercise = "squat") {
      const poses = await detector.estimatePoses(video, { flipHorizontal: false });
      if (!Array.isArray(poses) || poses.length === 0) return null;
      const points = extractMappedPoints(poses[0]);
      const joints = Object.values(points);
      if (joints.length === 0) return null;
      const angles = computeAngles(points);
      return {
        exercise,
        timestamp: Number(timestampMs) / 1000,
        joints,
        angles,
      };
    },
    dispose() {
      detector.dispose();
    },
  };
}
