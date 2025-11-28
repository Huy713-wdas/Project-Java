import React, { useState } from "react";

const AdminLoginModal = ({ open, onClose, onSuccess }) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  if (!open) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      await onSuccess(username, password);
      setUsername("");
      setPassword("");
    } catch (err) {
      setError(err.message || "Đăng nhập thất bại");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bot-modal active" aria-hidden="false">
      <div className="bot-modal__content" role="dialog" aria-modal="true">
        <button className="bot-close" onClick={onClose}>
          ×
        </button>
        <div className="ai-bot__text compact">
          <p className="eyebrow">Admin portal</p>
          <h2>Đăng nhập quản trị</h2>
          <p>Dùng tài khoản admin do hệ thống cấp.</p>
        </div>
        <form className="stack" onSubmit={handleSubmit}>
          <label>
            Username
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </label>
          <label>
            Password
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </label>
          {error && <div className="result error">{error}</div>}
          <button type="submit" disabled={loading}>
            {loading ? "Đang xác thực..." : "Đăng nhập"}
          </button>
        </form>
      </div>
    </div>
  );
};

export default AdminLoginModal;

