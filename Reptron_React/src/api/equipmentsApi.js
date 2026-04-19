import httpClient from "./httpClient.js";
import { unwrapListPayload } from "./responseUtils.js";

export async function fetchEquipments(category) {
  const { data } = await httpClient.get("/api/Equipments", {
    params: category ? { category } : undefined,
  });
  return unwrapListPayload(data);
}

export async function fetchEquipmentById(id) {
  const { data } = await httpClient.get(`/api/Equipments/${id}`);
  return data;
}
