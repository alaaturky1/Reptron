import { mapImageByEntity } from "./imageMap.js";
import * as productsApi from "../api/productsApi.js";
import * as equipmentsApi from "../api/equipmentsApi.js";
import * as coachesApi from "../api/coachesApi.js";
import * as authApi from "../api/authApi.js";
import * as ordersApi from "../api/ordersApi.js";
import * as reviewsApi from "../api/reviewsApi.js";
import { getApiErrorMessage } from "../api/responseUtils.js";

const fallbackEquipments = [
  { id: 1, name: "Chest Press Machine", specialty: "Chest", price: 499.99 },
  { id: 2, name: "Dual Lat Pulldown / Low Row Machine", specialty: "Back", price: 699.99 },
];

const fallbackCoaches = [
  { id: 1, name: "Ahmed Mohamed", specialty: "Bodybuilding", title: "Professional Bodybuilding Coach" },
];

function normalizeProduct(item) {
  if (!item) return null;
  const price = Number(item.price ?? item.currentPrice ?? 0);
  const original = item.originalPrice ?? item.oldPrice ?? item.salePrice;
  return {
    id: item.id ?? item.productId,
    name: item.name ?? item.title ?? "Product",
    description: item.longDescription ?? item.shortDescription ?? item.description ?? "Premium fitness product.",
    additionalInfo: item.additionalInfo ?? item.specifications ?? "No additional information available.",
    reviews: item.reviews ?? [],
    price,
    oldPrice: Number(original != null ? original : price),
    rating: Number(item.averageRating ?? item.rating ?? 4.5),
    category: (item.categoryName ?? item.category ?? "supplements").toLowerCase(),
    img: mapImageByEntity(item, "products"),
    image: mapImageByEntity(item, "products"),
    stockQuantity: item.stockQuantity,
    isOnSale: item.isOnSale,
    reviewCount: item.reviewCount,
  };
}

function normalizeEquipment(item) {
  if (!item) return null;
  return {
    id: item.id ?? item.equipmentId,
    name: item.name ?? item.title ?? "Equipment",
    specialty: item.categoryName ?? item.specialty ?? item.category ?? "General",
    bio: item.shortDescription ?? item.bio ?? item.description ?? "Professional gym equipment.",
    description: item.longDescription ?? item.shortDescription ?? item.description ?? "Professional gym equipment.",
    additionalInfo: item.additionalInfo ?? item.specifications ?? "No additional information available.",
    reviews: item.reviews ?? [],
    price: Number(item.price ?? 0),
    salePrice: Number(item.originalPrice ?? item.oldPrice ?? item.price ?? 0),
    img: mapImageByEntity(item, "equipments"),
    image: mapImageByEntity(item, "equipments"),
  };
}

function normalizeCoach(item) {
  if (!item) return null;
  const name = item.fullName ?? item.name ?? "Coach";
  return {
    id: item.id ?? item.coachId,
    name,
    specialty: item.specialization ?? item.specialty ?? item.category ?? "Fitness",
    title: item.title ?? "Fitness Coach",
    bio: item.bio ?? item.shortDescription ?? item.description ?? "Certified trainer.",
    fullBio: item.fullBio ?? item.bio ?? item.description ?? "Certified trainer.",
    experience: item.experience ?? "5+ years",
    clients: item.clients ?? "100+ clients",
    certifications: item.certifications ?? "Certified",
    phone: item.phone ?? "N/A",
    email: item.email ?? "N/A",
    hourlyRate: item.hourlyRate ?? "$50/hour",
    availability: item.availability ?? [],
    image: mapImageByEntity({ ...item, name }, "coaches"),
  };
}

function normalizeReviewRow(r) {
  return {
    name: r.userName ?? r.authorName ?? r.name ?? "Customer",
    rating: Math.min(5, Math.max(1, Number(r.rating ?? 5))),
    comment: r.comment ?? r.text ?? "",
  };
}

export async function getProducts(params) {
  const raw = await productsApi.fetchProducts(params ?? {});
  return raw.map((x) => normalizeProduct(x)).filter(Boolean);
}

export async function getProductById(id) {
  const data = await productsApi.fetchProductById(id);
  return normalizeProduct(data);
}

/** Product reviews for the details tab (GET /api/Reviews/products/{id}). */
export async function getProductReviews(productId) {
  try {
    const rows = await reviewsApi.fetchProductReviews(productId);
    return rows.map(normalizeReviewRow);
  } catch {
    return [];
  }
}

export async function getEquipments(category) {
  try {
    const raw = await equipmentsApi.fetchEquipments(category);
    return raw.map((x) => normalizeEquipment(x)).filter(Boolean);
  } catch (e) {
    console.warn("getEquipments fallback:", getApiErrorMessage(e));
    return fallbackEquipments.map(normalizeEquipment);
  }
}

export async function getEquipmentById(id) {
  try {
    const data = await equipmentsApi.fetchEquipmentById(id);
    return normalizeEquipment(data);
  } catch (e) {
    console.warn("getEquipmentById fallback:", getApiErrorMessage(e));
    const local = fallbackEquipments.find((p) => Number(p.id) === Number(id)) || fallbackEquipments[0];
    return normalizeEquipment(local);
  }
}

export async function getCoaches() {
  try {
    const raw = await coachesApi.fetchCoaches();
    return raw.map((x) => normalizeCoach(x)).filter(Boolean);
  } catch (e) {
    console.warn("getCoaches fallback:", getApiErrorMessage(e));
    return fallbackCoaches.map(normalizeCoach);
  }
}

export async function getCoachById(id) {
  try {
    const data = await coachesApi.fetchCoachById(id);
    return normalizeCoach(data);
  } catch (e) {
    console.warn("getCoachById fallback:", getApiErrorMessage(e));
    const local = fallbackCoaches.find((p) => Number(p.id) === Number(id)) || fallbackCoaches[0];
    return normalizeCoach(local);
  }
}

/**
 * Login — returns token string or throws with axios error (use getApiErrorMessage).
 */
export async function login(payload) {
  const { raw, token } = await authApi.login(payload);
  return token ? { ...raw, token } : raw;
}

/**
 * Register — maps UI "name" to first/last; Swagger has no phone field (phone ignored for API).
 */
export async function register(form) {
  const full = (form.name || "").trim();
  const parts = full.split(/\s+/);
  const firstName = parts[0] || "User";
  const lastName = parts.slice(1).join(" ") || firstName;
  const userName = form.email?.split("@")[0] || full.replace(/\s+/g, "") || "user";

  const { raw, token } = await authApi.register({
    userName: form.userName ?? userName,
    email: form.email,
    password: form.password,
    firstName: form.firstName ?? firstName,
    lastName: form.lastName ?? lastName,
  });
  return token ? { ...raw, token } : raw;
}

export async function submitOrder(shippingAddressString) {
  return ordersApi.createOrder({ shippingAddress: shippingAddressString });
}

export async function getOrders() {
  return ordersApi.fetchOrders();
}

export async function getOrderById(orderId) {
  return ordersApi.fetchOrderById(orderId);
}
