# Frontend Tester (Separate from Backend)

واجهة بسيطة لاختبار كل خصائص Backend الخاصة بـ Fitness Coach بسرعة.

## ماذا يغطي؟

- `GET /health`
- `POST /start-session`
- `POST /analyze-frame`
- `GET /session-summary/{session_id}`
- `POST /end-session`
- `WS /ws/session/{session_id}`

## التشغيل

1) شغّل الباك أولًا على المنفذ 8000:

```bash
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

2) من فولدر `frontend-tester`:

```bash
npm install
npm run dev
```

3) افتح:

`http://localhost:5173`

## ملاحظات

- الواجهة تستخدم Vite proxy:
  - `/api/*` -> `http://localhost:8000/*`
  - `/ws/*` -> `ws://localhost:8000/ws/*`
- ضع قيمة `FITCOACH_API_KEY` نفسها في خانة API Key داخل الواجهة.
- لو عايز Backend مختلف:

```bash
BACKEND_URL=http://localhost:9000 npm run dev
```
