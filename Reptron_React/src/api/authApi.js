import httpClient from "./httpClient.js";
import { extractAuthToken } from "./responseUtils.js";

/**
 * POST /api/Auth/login — Swagger: LoginRequest { email, password }
 */
export async function login({ email, password }) {
  const { data } = await httpClient.post("/api/Auth/login", { email, password });
  return { raw: data, token: extractAuthToken(data) };
}

/**
 * POST /api/Auth/register — Swagger: RegisterRequest { userName, email, password, firstName, lastName }
 */
export async function register({ userName, email, password, firstName, lastName }) {
  const { data } = await httpClient.post("/api/Auth/register", {
    userName,
    email,
    password,
    firstName,
    lastName,
  });
  return { raw: data, token: extractAuthToken(data) };
}
