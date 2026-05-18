FROM python:3.12-slim

ENV PYTHONDONTWRITEBYTECODE=1 \
    PYTHONUNBUFFERED=1

WORKDIR /app

COPY requirements.txt /app/requirements.txt
RUN pip install --no-cache-dir -r /app/requirements.txt

COPY app /app/app

EXPOSE 8000

# Required for auth by default:
#   FITCOACH_API_KEY
# Optional:
#   FITCOACH_REQUIRE_API_KEY=false
#   FITCOACH_LOG_LEVEL=INFO
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]

