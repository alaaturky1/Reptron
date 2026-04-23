import { defineConfig } from "vite";

const backendTarget = process.env.BACKEND_URL || "http://localhost:8000";

export default defineConfig({
  server: {
    port: 5173,
    proxy: {
      "/api": {
        target: backendTarget,
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ""),
      },
      "/ws": {
        target: backendTarget.replace(/^http/, "ws"),
        ws: true,
        changeOrigin: true,
      },
    },
  },
});
