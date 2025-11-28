const API_BASE = process.env.REACT_APP_API_BASE || "http://localhost:8080/api";

const formatPayload = (payload) =>
  typeof payload === "string" ? payload : JSON.stringify(payload, null, 2);

async function parseResponse(response) {
  const text = await response.text();
  if (!text) return null;
  try {
    return JSON.parse(text);
  } catch {
    return text;
  }
}

export async function apiRequest(path, options = {}) {
  const {
    method = "GET",
    params,
    data,
    formData,
    formUrlEncoded,
    headers = {},
  } = options;

  const url = new URL(path.replace(/^\//, ""), `${API_BASE}/`);
  if (params) {
    Object.entries(params).forEach(([key, value]) => {
      if (value !== undefined && value !== null && value !== "") {
        url.searchParams.append(key, value);
      }
    });
  }

  const fetchOptions = { method, headers: { ...headers } };

  if (formData) {
    fetchOptions.body = formData;
  } else if (formUrlEncoded) {
    fetchOptions.body = new URLSearchParams(formUrlEncoded);
    fetchOptions.headers["Content-Type"] =
      "application/x-www-form-urlencoded;charset=UTF-8";
  } else if (data !== undefined) {
    fetchOptions.body = JSON.stringify(data);
    fetchOptions.headers["Content-Type"] = "application/json";
  }

  const response = await fetch(url.toString(), fetchOptions);
  const payload = await parseResponse(response);

  if (!response.ok) {
    const message =
      (payload && payload.message) ||
      (typeof payload === "string" && payload) ||
      `HTTP ${response.status}`;
    const error = new Error(message);
    error.payload = payload;
    throw error;
  }

  return payload;
}

export { API_BASE, formatPayload };

