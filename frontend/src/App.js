import React, { useState, useEffect } from "react";

export default function App() {
  const [journeys, setJourneys] = useState([]); // KHỞI TẠO LÀ MẢNG
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function load() {
      try {
        // --- Thay URL nếu backend của bạn khác ---
        const res = await fetch("/api/journeys"); 
        if (!res.ok) throw new Error(`HTTP ${res.status}`);

        const data = await res.json();
        // LOG để bạn xem chính xác server trả gì
        console.log("API response (raw):", data);
        console.log("typeof:", typeof data, "Array.isArray:", Array.isArray(data));

        // --- Xử lý các cấu trúc thường gặp ---
        if (Array.isArray(data)) {
          setJourneys(data);
        } else if (data == null) {
          setJourneys([]);
        } else if (Array.isArray(data.journeys)) {
          setJourneys(data.journeys);
        } else if (Array.isArray(data.content)) {
          setJourneys(data.content);
        } else if (Array.isArray(data.data)) {
          // một số API trả { data: [...] }
          setJourneys(data.data);
        } else {
          // fallback: nếu server trả 1 object duy nhất, không bỏ qua, hiển thị dưới dạng 1 phần tử
          setJourneys([data]);
        }
      } catch (err) {
        console.error("Fetch error:", err);
        setError(err.message || "Fetch error");
        setJourneys([]);
      } finally {
        setLoading(false);
      }
    }
    load();
  }, []);

  if (loading) return <div>Đang tải...</div>;
  if (error) return <div style={{ color: "red" }}>Lỗi: {error}</div>;

  return (
    <div style={{ padding: 20 }}>
      <h1>Danh sách journeys</h1>

      {!Array.isArray(journeys) && (
        <div style={{ color: "orange" }}>
          Warning: journeys không phải mảng (type: {typeof journeys})
        </div>
      )}

      {Array.isArray(journeys) && journeys.length === 0 ? (
        <p>Không có hành trình</p>
      ) : (
        <ul>
          {Array.isArray(journeys)
            ? journeys.map((j, i) => (
                <li key={j.id ?? i}>
                  {/* hiển thị những trường hay có */}
                  <strong>{j.name ?? j.title ?? `Hành trình #${i + 1}`}</strong>
                  <div>id: {j.id ?? "—"}</div>
                  <div>{j.distanceKm ? `Distance: ${j.distanceKm} km` : null}</div>
                  <pre style={{ whiteSpace: "pre-wrap" }}>
                    {typeof j === "object" ? JSON.stringify(j, null, 2) : String(j)}
                  </pre>
                </li>
              ))
            : null}
        </ul>
      )}
    </div>
  );
}
