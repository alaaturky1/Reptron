import { API_BASE_URL } from "../config/env.js";

const defaultImage = "/images/fallback.svg";
const normalizedApiBaseUrl = API_BASE_URL.replace(/\/+$/, "");

const imageByName = {
  "whey sport": "/images/fallback.svg",
  "whey protein": "/images/fallback.svg",
  "protein bar": "/images/fallback.svg",
  creatine: "/images/fallback.svg",
  "bcaa powder": "/images/fallback.svg",
  "pre-workout": "/images/fallback.svg",
  glutamine: "/images/fallback.svg",
  "omega 3 capsules": "/images/fallback.svg",
  "vitamin d3": "/images/fallback.svg",
  multivitamins: "/images/fallback.svg",
  "weight gainer": "/images/fallback.svg",
  "electrolyte drink": "/images/fallback.svg",
};

export function mapImageByEntity(entity, prefix = "products") {
  if (!entity) return defaultImage;

  const explicit =
    entity.imageUrl ||
    entity.profilePictureUrl ||
    entity.photoUrl ||
    entity.image ||
    entity.img;
  if (typeof explicit === "string" && explicit.trim()) {
    if (/^https?:\/\//i.test(explicit)) return explicit;
    if (explicit.startsWith("/")) return `${normalizedApiBaseUrl}${explicit}`;
    return `${normalizedApiBaseUrl}/${explicit.replace(/^\/+/, "")}`;
  }

  if (entity.id) {
    return `/images/${prefix}/${entity.id}.jpg`;
  }

  const byName = imageByName[String(entity.name || "").toLowerCase()];
  return byName || defaultImage;
}

export const FALLBACK_IMAGE = defaultImage;
