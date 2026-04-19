import httpClient from "./httpClient.js";
import { unwrapListPayload } from "./responseUtils.js";

export async function fetchCoaches() {
  const { data } = await httpClient.get("/api/Coaches");
  return unwrapListPayload(data);
}

export async function fetchCoachById(id) {
  const { data } = await httpClient.get(`/api/Coaches/${id}`);
  return data;
}

export async function fetchCoachAvailability(id) {
  const { data } = await httpClient.get(`/api/Coaches/${id}/availability`);
  return data;
}
