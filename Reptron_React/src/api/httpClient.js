import axios from "axios";
import { API_BASE_URL } from "../config/env.js";

export const AUTH_TOKEN_KEY = "userToken";

/** Saved on email/password login/register so the Profile page can verify "current password" (no backend change-password API). */
export const LOCAL_AUTH_PASSWORD_KEY = "localAuthPassword";

const httpClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 20000,
  headers: {
    "Content-Type": "application/json",
  },
});

httpClient.interceptors.request.use((config) => {
  const token = localStorage.getItem(AUTH_TOKEN_KEY);
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default httpClient;
