import React, { useState } from "react";
import { apiRequest } from "../api/client";

const ChatWidget = () => {
  const [open, setOpen] = useState(false);
  const [input, setInput] = useState("");
  const [messages, setMessages] = useState([]);
  const [loading, setLoading] = useState(false);

  const sendMessage = async (event) => {
    event.preventDefault();
    const text = input.trim();
    if (!text) return;
    const userMsg = { role: "user", content: text };
    setMessages((prev) => [...prev, userMsg]);
    setInput("");
    setLoading(true);

    try {
      const res = await apiRequest("/ai/chat", {
        method: "POST",
        data: { message: text },
      });
      setMessages((prev) => [
        ...prev,
        { role: "bot", content: res?.reply || "AI phản hồi trống." },
      ]);
    } catch (err) {
      setMessages((prev) => [
        ...prev,
        {
          role: "bot",
          content: err.message || "AI hiện không phản hồi, thử lại sau.",
        },
      ]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={`chat-widget ${open ? "open" : ""}`}>
      {open ? (
        <div className="chat-panel">
          <div className="chat-header">
            <strong>AI Copilot</strong>
            <button onClick={() => setOpen(false)}>×</button>
          </div>
          <div className="chat-messages">
            {messages.length === 0 && (
              <div className="chat-placeholder">
                Hỏi bot về giá tín chỉ, xu hướng thị trường, cách tối ưu hành
                trình...
              </div>
            )}
            {messages.map((msg, idx) => (
              <div
                key={idx}
                className={`chat-message ${msg.role === "user" ? "user" : "bot"}`}
              >
                {msg.content}
              </div>
            ))}
            {loading && <div className="chat-message bot">Đang suy nghĩ...</div>}
          </div>
          <form className="chat-input" onSubmit={sendMessage}>
            <input
              type="text"
              placeholder="Nhập câu hỏi..."
              value={input}
              onChange={(e) => setInput(e.target.value)}
              disabled={loading}
            />
            <button type="submit" disabled={loading}>
              Gửi
            </button>
          </form>
        </div>
      ) : (
        <button className="chat-toggle" onClick={() => setOpen(true)}>
          AI Bot
        </button>
      )}
    </div>
  );
};

export default ChatWidget;

