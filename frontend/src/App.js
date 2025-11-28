import React, { useState } from "react";
import "./styles/main.css";
import { apiRequest, API_BASE, formatPayload } from "./api/client";
import hero1 from "./assets/xedien/chungtay.jpg";
import hero3 from "./assets/xedien/vande.jpg";
import hero4 from "./assets/xedien/xe.jpg";
import ChatWidget from "./components/ChatWidget";
import AdminLoginModal from "./components/AdminLoginModal";

const heroImages = [hero1, hero3, hero4];

const ResultBox = ({ result }) => (
  <div className={`result ${result?.isError ? "error" : ""}`}>
    {result?.text || "Chưa có dữ liệu"}
  </div>
);

const SectionTitle = ({ eyebrow, title, description }) => (
  <header className="panel__head">
    {eyebrow && <p className="eyebrow">{eyebrow}</p>}
    <h2>{title}</h2>
    {description && <p>{description}</p>}
  </header>
);

function App() {
  const [viewMode, setViewMode] = useState("guest");
  const [activeRole, setActiveRole] = useState("owner");
  const [results, setResults] = useState({});
  const [backendStatus, setBackendStatus] = useState({
    text: "Chưa kiểm tra",
    ok: false,
  });
  const [adminSession, setAdminSession] = useState({
    loggedIn: false,
    token: null,
  });
  const [loginModalOpen, setLoginModalOpen] = useState(false);

  const showResult = (key, payload, isError = false) => {
    setResults((prev) => ({
      ...prev,
      [key]: {
        text: formatPayload(payload ?? "Đã xong"),
        isError,
      },
    }));
  };

  const showLoading = (key) => showResult(key, "Đang xử lý...");

  const handleForm = (key, handler, reset = false) => async (event) => {
    event.preventDefault();
    showLoading(key);
    try {
      const payload = await handler(new FormData(event.target));
      showResult(key, payload);
      if (reset) event.target.reset();
    } catch (err) {
      showResult(key, err.message || "Có lỗi xảy ra", true);
    }
  };

  const handleAction = (key, action) => async () => {
    showLoading(key);
    try {
      const payload = await action();
      showResult(key, payload);
    } catch (err) {
      showResult(key, err.message || "Có lỗi xảy ra", true);
    }
  };

  const pingBackend = async () => {
    setBackendStatus({ text: "Đang ping...", ok: false });
    try {
      const res = await apiRequest("/hello");
      setBackendStatus({ text: `Online: ${res}`, ok: true });
    } catch (err) {
      setBackendStatus({ text: err.message || "Không kết nối được", ok: false });
    }
  };

  const journeyUpload = handleForm(
    "journeyUpload",
    async (fd) => {
      const formData = new FormData();
      const ownerId = fd.get("ownerId");
      const file = fd.get("file");
      if (!ownerId) throw new Error("Nhập owner ID");
      if (!file || !file.size) throw new Error("Chọn file CSV hành trình");
      formData.append("ownerId", ownerId);
      formData.append("file", file);
      return apiRequest("/journeys/upload-csv", {
        method: "POST",
        formData,
      });
    },
    true
  );

  const journeyList = handleForm("journeyList", (fd) => {
    const ownerId = fd.get("ownerId");
    const params = ownerId ? { ownerId } : undefined;
    return apiRequest("/journeys", { params });
  });

  const walletLookup = handleForm("wallet", (fd) => {
    const ownerId = fd.get("ownerId");
    if (!ownerId) throw new Error("Nhập owner ID");
    return apiRequest(`/wallet/${ownerId}`);
  });

  const walletDeposit = handleForm(
    "walletDeposit",
    (fd) =>
      apiRequest("/wallet/deposit", {
        method: "POST",
        data: {
          ownerId: Number(fd.get("ownerId")),
          amount: Number(fd.get("amount")),
        },
      }),
    true
  );

  const walletWithdraw = handleForm(
    "walletWithdraw",
    (fd) =>
      apiRequest("/wallet/withdraw", {
        method: "POST",
        data: {
          ownerId: Number(fd.get("ownerId")),
          amount: Number(fd.get("amount")),
        },
      }),
    true
  );

  const cashDeposit = handleForm(
    "cashDeposit",
    (fd) =>
      apiRequest("/wallet/cash/deposit", {
        method: "POST",
        data: {
          ownerId: Number(fd.get("ownerId")),
          amount: Number(fd.get("amount")),
        },
      }),
    true
  );

  const cashWithdraw = handleForm(
    "cashWithdraw",
    (fd) =>
      apiRequest("/wallet/cash/withdraw", {
        method: "POST",
        data: {
          ownerId: Number(fd.get("ownerId")),
          amount: Number(fd.get("amount")),
        },
      }),
    true
  );

  const personalReport = handleForm("personalReport", (fd) =>
    apiRequest("/reports/personal", {
      params: { ownerId: fd.get("ownerId") },
    })
  );

  const listingCreate = handleForm(
    "listingCreate",
    (fd) => {
      const payload = {
        ownerId: Number(fd.get("ownerId")),
        title: fd.get("title"),
        region: fd.get("region"),
        credits: Number(fd.get("credits")),
        price: Number(fd.get("price")),
        auction: fd.get("auction") === "true",
      };
      return apiRequest("/listings/create", { method: "POST", data: payload });
    },
    true
  );

  const listingByOwner = handleForm("listingOwner", (fd) => {
    const ownerId = fd.get("ownerId");
    if (!ownerId) throw new Error("Nhập owner ID");
    return apiRequest(`/listings/owner/${ownerId}`);
  });

  const listingActive = handleAction("listingActive", () =>
    apiRequest("/listings/active")
  );

  const searchCredits = handleForm("creditSearch", (fd) => {
    const region = fd.get("region");
    const maxPrice = fd.get("maxPrice");
    const params = {};
    if (region) params.region = region;
    if (maxPrice) params.maxPrice = maxPrice;
    return apiRequest("/transactions/credits", { params });
  });

  const buyCredits = handleForm("purchase", (fd) => {
    return apiRequest("/transactions/buy", {
      method: "POST",
      formUrlEncoded: {
        buyerId: fd.get("buyerId"),
        listingId: fd.get("listingId"),
        quantity: fd.get("quantity"),
        paymentMethod: fd.get("paymentMethod"),
      },
    });
  });

  const history = handleForm("history", (fd) => {
    const buyerId = fd.get("buyerId");
    if (!buyerId) throw new Error("Nhập buyer ID");
    return apiRequest("/transactions/history", { params: { buyerId } });
  });

  const verificationRequest = handleForm(
    "verificationRequest",
    (fd) => {
      return apiRequest("/verification/request", {
        method: "POST",
        formUrlEncoded: {
          ownerId: fd.get("ownerId"),
          emissionReduced: fd.get("emissionReduced"),
          credits: fd.get("credits"),
        },
      });
    },
    true
  );

  const verificationApprove = handleForm(
    "verificationAction",
    (fd) => {
      return apiRequest("/verification/approve", {
        method: "POST",
        formUrlEncoded: {
          requestId: fd.get("requestId"),
          auditorId: fd.get("auditorId"),
        },
      });
    },
    true
  );

  const verificationReject = handleForm(
    "verificationAction",
    (fd) => {
      return apiRequest("/verification/reject", {
        method: "POST",
        formUrlEncoded: {
          requestId: fd.get("requestId"),
          auditorId: fd.get("auditorId"),
          reason: fd.get("reason"),
        },
      });
    },
    true
  );

  const verificationPending = handleAction("verificationList", () =>
    apiRequest("/verification/pending")
  );

  const verificationReports = handleAction("verificationList", () =>
    apiRequest("/verification/reports")
  );

  const adminAddUser = handleForm(
    "adminAddUser",
    (fd) =>
      apiRequest("/admin/users", {
        method: "POST",
        formUrlEncoded: {
          username: fd.get("username"),
          role: fd.get("role"),
        },
      }),
    true
  );

  const adminToggleUser = handleForm(
    "adminToggleUser",
    (fd) =>
      apiRequest(`/admin/users/${fd.get("userId")}/status`, {
        method: "POST",
        params: { active: fd.get("active") },
      }),
    true
  );

  const adminFetch = (path) => handleAction("adminLists", () => apiRequest(path));

  const adminDispute = handleForm(
    "adminDispute",
    (fd) =>
      apiRequest(`/admin/transactions/${fd.get("transactionId")}/dispute`, {
        method: "POST",
      }),
    true
  );

  const aiSuggest = handleForm(
    "aiSuggest",
    (fd) => {
      const payload = {
        credits: Number(fd.get("credits") || 0),
        targetRevenue: Number(fd.get("targetRevenue") || 0),
        region: fd.get("region"),
        carbonType: fd.get("carbonType"),
        notes: fd.get("notes"),
      };
      return apiRequest("/ai/price-suggest", {
        method: "POST",
        data: payload,
      });
    },
    true
  );

  const openRole = (role) => {
    setActiveRole(role);
    const el = document.getElementById(role);
    if (el) el.scrollIntoView({ behavior: "smooth" });
  };

  const requestAdminView = () => {
    if (!adminSession.loggedIn) {
      setLoginModalOpen(true);
      return;
    }
    setViewMode("admin");
    openRole("admin");
  };

  const handleAdminLogin = async (username, password) => {
    const res = await apiRequest("/auth/login", {
      method: "POST",
      data: { username, password },
    });
    setAdminSession({ loggedIn: true, token: res.token });
    setLoginModalOpen(false);
    setViewMode("admin");
    openRole("admin");
  };

  const ownerPanel = (
    <section
      className={`role-panel ${activeRole === "owner" ? "" : "hidden"}`}
      id="owner"
    >
      <SectionTitle
        title="Chủ sở hữu EV"
        description="Đồng bộ hành trình, quản lý ví, niêm yết tín chỉ và xem báo cáo."
      />
      <div className="grid">
        <article className="card">
          <h3>Đồng bộ hành trình (CSV)</h3>
          <form onSubmit={journeyUpload}>
            <label>
              Owner ID
              <input type="text" name="ownerId" required />
            </label>
            <label className="file-input">
              CSV hành trình
              <input type="file" name="file" accept=".csv" required />
            </label>
            <button type="submit">Upload</button>
          </form>
          <ResultBox result={results.journeyUpload} />
        </article>

        <article className="card">
          <h3>Lịch sử hành trình</h3>
          <form onSubmit={journeyList}>
            <label>
              Owner ID
              <input type="text" name="ownerId" placeholder="Để trống để lấy tất cả" />
            </label>
            <button type="submit">Tải dữ liệu</button>
          </form>
          <ResultBox result={results.journeyList} />
        </article>

        <article className="card">
          <h3>Ví carbon</h3>
          <form onSubmit={walletLookup}>
            <label>
              Owner ID
              <input type="number" name="ownerId" required />
            </label>
            <button type="submit">Xem ví</button>
          </form>
          <ResultBox result={results.wallet} />
        </article>

        <article className="card">
          <h3>Nạp tín chỉ</h3>
          <form onSubmit={walletDeposit}>
            <label>
              Owner ID
              <input type="number" name="ownerId" required />
            </label>
            <label>
              Số credits
              <input type="number" step="0.1" name="amount" required />
            </label>
            <button type="submit">Nạp</button>
          </form>
          <ResultBox result={results.walletDeposit} />
        </article>

        <article className="card">
          <h3>Rút tín chỉ</h3>
          <form onSubmit={walletWithdraw}>
            <label>
              Owner ID
              <input type="number" name="ownerId" required />
            </label>
            <label>
              Số credits
              <input type="number" step="0.1" name="amount" required />
            </label>
            <button type="submit">Rút</button>
          </form>
          <ResultBox result={results.walletWithdraw} />
        </article>

        <article className="card">
          <h3>Tiền mặt</h3>
          <form onSubmit={cashDeposit}>
            <label>
              Owner ID
              <input type="number" name="ownerId" required />
            </label>
            <label>
              Nạp tiền (USD)
              <input type="number" step="0.01" name="amount" required />
            </label>
            <button type="submit">Nạp tiền</button>
          </form>
          <ResultBox result={results.cashDeposit} />
          <form onSubmit={cashWithdraw}>
            <label>
              Owner ID
              <input type="number" name="ownerId" required />
            </label>
            <label>
              Rút tiền (USD)
              <input type="number" step="0.01" name="amount" required />
            </label>
            <button type="submit" className="danger">
              Rút
            </button>
          </form>
          <ResultBox result={results.cashWithdraw} />
        </article>

        <article className="card">
          <h3>Niêm yết tín chỉ</h3>
          <form onSubmit={listingCreate}>
            <label>
              Owner ID
              <input type="number" name="ownerId" required />
            </label>
            <label>
              Tên listing
              <input type="text" name="title" placeholder="VD: Lô EV Hà Nội" />
            </label>
            <label>
              Khu vực
              <input type="text" name="region" placeholder="Hà Nội" />
            </label>
            <label>
              Số credits
              <input type="number" step="0.1" name="credits" required />
            </label>
            <label>
              Giá/credit
              <input type="number" step="0.01" name="price" required />
            </label>
            <label>
              Hình thức
              <select name="auction">
                <option value="false">Fixed price</option>
                <option value="true">Auction</option>
              </select>
            </label>
            <button type="submit">Niêm yết</button>
          </form>
          <ResultBox result={results.listingCreate} />
        </article>

        <article className="card">
          <h3>Listing theo owner</h3>
          <form onSubmit={listingByOwner}>
            <label>
              Owner ID
              <input type="number" name="ownerId" required />
            </label>
            <button type="submit">Tải listing</button>
          </form>
          <ResultBox result={results.listingOwner} />
        </article>

        <article className="card">
          <h3>Listing đang active</h3>
          <button className="ghost full" onClick={listingActive}>
            Lấy danh sách
          </button>
          <ResultBox result={results.listingActive} />
        </article>

        <article className="card">
          <h3>AI gợi ý giá</h3>
          <form onSubmit={aiSuggest}>
            <label>
              Số credits
              <input type="number" step="0.1" name="credits" required />
            </label>
            <label>
              Doanh thu kỳ vọng (USD)
              <input type="number" step="0.01" name="targetRevenue" required />
            </label>
            <label>
              Khu vực
              <input type="text" name="region" placeholder="Việt Nam" />
            </label>
            <label>
              Loại tín chỉ
              <input type="text" name="carbonType" placeholder="EV offset" />
            </label>
            <label>
              Ghi chú
              <textarea name="notes" rows="3" />
            </label>
            <button type="submit">Hỏi AI</button>
          </form>
          <ResultBox result={results.aiSuggest} />
        </article>

        <article className="card">
          <h3>Báo cáo cá nhân</h3>
          <form onSubmit={personalReport}>
            <label>
              Owner ID
              <input type="text" name="ownerId" required />
            </label>
            <button type="submit">Xem báo cáo</button>
          </form>
          <ResultBox result={results.personalReport} />
        </article>
      </div>
    </section>
  );

  const buyerPanel = (
    <section
      className={`role-panel ${activeRole === "buyer" ? "" : "hidden"}`}
      id="buyer"
    >
      <SectionTitle
        title="Carbon Credit Buyer"
        description="Tìm kiếm listing, mua tín chỉ và xem lịch sử giao dịch."
      />
      <div className="grid">
        <article className="card">
          <h3>Tìm tín chỉ</h3>
          <form onSubmit={searchCredits}>
            <label>
              Khu vực
              <input type="text" name="region" placeholder="Hà Nội" />
            </label>
            <label>
              Giá tối đa
              <input type="number" name="maxPrice" step="0.01" />
            </label>
            <button type="submit">Search</button>
          </form>
          <ResultBox result={results.creditSearch} />
        </article>

        <article className="card">
          <h3>Mua tín chỉ</h3>
          <form onSubmit={buyCredits}>
            <label>
              Buyer ID
              <input type="number" name="buyerId" required />
            </label>
            <label>
              Listing ID
              <input type="number" name="listingId" required />
            </label>
            <label>
              Số lượng
              <input type="number" name="quantity" required />
            </label>
            <label>
              Thanh toán
              <select name="paymentMethod">
                <option value="BANKING">Banking</option>
                <option value="E_WALLET">E-wallet</option>
                <option value="CASH">Cash</option>
              </select>
            </label>
            <button type="submit">Thanh toán</button>
          </form>
          <ResultBox result={results.purchase} />
        </article>

        <article className="card">
          <h3>Lịch sử giao dịch</h3>
          <form onSubmit={history}>
            <label>
              Buyer ID
              <input type="number" name="buyerId" required />
            </label>
            <button type="submit">Xem lịch sử</button>
          </form>
          <ResultBox result={results.history} />
        </article>
      </div>
    </section>
  );

  const auditorPanel = (
    <section
      className={`role-panel ${activeRole === "auditor" ? "" : "hidden"}`}
      id="auditor"
    >
      <SectionTitle
        title="Verifier & Audit"
        description="Gửi yêu cầu, duyệt/từ chối và xem báo cáo."
      />
      <div className="grid">
        <article className="card">
          <h3>Gửi yêu cầu xác minh</h3>
          <form onSubmit={verificationRequest}>
            <label>
              Owner ID
              <input type="number" name="ownerId" required />
            </label>
            <label>
              CO₂ giảm (kg)
              <input type="number" step="0.1" name="emissionReduced" required />
            </label>
            <label>
              Credits
              <input type="number" name="credits" required />
            </label>
            <button type="submit">Submit</button>
          </form>
          <ResultBox result={results.verificationRequest} />
        </article>

        <article className="card">
          <h3>Duyệt yêu cầu</h3>
          <form onSubmit={verificationApprove}>
            <label>
              Request ID
              <input type="number" name="requestId" required />
            </label>
            <label>
              Auditor ID
              <input type="number" name="auditorId" required />
            </label>
            <button type="submit">Approve</button>
          </form>
          <form onSubmit={verificationReject}>
            <label>
              Request ID
              <input type="number" name="requestId" required />
            </label>
            <label>
              Auditor ID
              <input type="number" name="auditorId" required />
            </label>
            <label>
              Lý do từ chối
              <input type="text" name="reason" required />
            </label>
            <button type="submit" className="danger">
              Reject
            </button>
          </form>
          <ResultBox result={results.verificationAction} />
        </article>

        <article className="card">
          <h3>Danh sách & báo cáo</h3>
          <div className="stack">
            <button onClick={verificationPending}>Yêu cầu chờ duyệt</button>
            <button className="ghost" onClick={verificationReports}>
              Báo cáo audit
            </button>
          </div>
          <ResultBox result={results.verificationList} />
        </article>
      </div>
    </section>
  );

  const adminPanel = (
    <section className="role-panel" id="admin">
      <SectionTitle
        title="Admin control"
        description="Quản lý user, listing, giao dịch và dispute."
      />
      <div className="grid">
        <article className="card">
          <h3>Tạo người dùng</h3>
          <form onSubmit={adminAddUser}>
            <label>
              Username
              <input type="text" name="username" required />
            </label>
            <label>
              Vai trò
              <select name="role">
                <option value="EV_OWNER">EV Owner</option>
                <option value="BUYER">Buyer</option>
                <option value="VERIFIER">Verifier</option>
                <option value="ADMIN">Admin</option>
              </select>
            </label>
            <button type="submit">Tạo user</button>
          </form>
          <ResultBox result={results.adminAddUser} />
        </article>

        <article className="card">
          <h3>Đổi trạng thái user</h3>
          <form onSubmit={adminToggleUser}>
            <label>
              User ID
              <input type="number" name="userId" required />
            </label>
            <label>
              Trạng thái
              <select name="active">
                <option value="true">Active</option>
                <option value="false">Inactive</option>
              </select>
            </label>
            <button type="submit">Cập nhật</button>
          </form>
          <ResultBox result={results.adminToggleUser} />
        </article>

        <article className="card">
          <h3>Dữ liệu nền tảng</h3>
          <div className="stack">
            <button onClick={adminFetch("/admin/users")}>Người dùng</button>
            <button className="ghost" onClick={adminFetch("/admin/credits")}>
              Credits
            </button>
            <button className="ghost" onClick={adminFetch("/admin/transactions")}>
              Giao dịch
            </button>
            <button onClick={adminFetch("/admin/reports/platform")}>
              Báo cáo tổng
            </button>
          </div>
          <ResultBox result={results.adminLists} />
        </article>

        <article className="card">
          <h3>Đánh dấu tranh chấp</h3>
          <form onSubmit={adminDispute}>
            <label>
              Transaction ID
              <input type="number" name="transactionId" required />
            </label>
            <button type="submit" className="danger">
              Mark disputed
            </button>
          </form>
          <ResultBox result={results.adminDispute} />
        </article>
      </div>
    </section>
  );

  return (
    <>
      <div className="background-glow" />
      <header className="topbar">
        <div className="brand">
          <div className="brand__logo">EV</div>
          <div>
            <strong>EV Carbon</strong>
            <small>Credit Platform</small>
          </div>
        </div>
        <nav className="topbar__nav">
          <a href="#overview">Tổng quan</a>
          <button onClick={() => openRole("owner")}>Owner</button>
          <button onClick={() => openRole("buyer")}>Buyer</button>
          <button onClick={() => openRole("auditor")}>Verifier</button>
          <button onClick={requestAdminView}>
            {adminSession.loggedIn ? "Admin" : "Đăng nhập Admin"}
          </button>
        </nav>
        <div className="view-switch">
          <button
            className={viewMode === "guest" ? "active" : ""}
            onClick={() => setViewMode("guest")}
          >
            Khách
          </button>
          <button
            className={viewMode === "admin" ? "active" : ""}
            onClick={requestAdminView}
          >
            Admin
          </button>
          {adminSession.loggedIn && (
            <button
              onClick={() => {
                setAdminSession({ loggedIn: false, token: null });
                setViewMode("guest");
              }}
            >
              Đăng xuất
            </button>
          )}
        </div>
      </header>

      <section className="hero" id="home">
        <div className="hero__media">
          <div className="hero__pulse" />
          {heroImages.map((img, idx) => (
            <div
              key={img}
              className="hero__image"
              style={{
                backgroundImage: `url(${img})`,
                animationDelay: `${idx * 8}s`,
              }}
            />
          ))}
          <div className="floating-card">
            <p>Giảm phát thải</p>
            <strong>–42%</strong>
            <span>so với baseline</span>
          </div>
        </div>
        <div className="hero__content">
          <p className="eyebrow">Project Java · EV Carbon</p>
          <h1>
            {viewMode === "guest"
              ? "Thao tác tín chỉ carbon trong một dashboard"
              : "Điều khiển nền tảng carbon toàn diện"}
          </h1>
          <p>
            Một dashboard duy nhất để đồng bộ hành trình, xác minh tín chỉ,
            giao dịch và quản trị dòng tiền.
          </p>
          <div className="hero__badges">
            <span className="badge glow">Realtime CO₂ tracking</span>
            <span className="badge">AI pricing & chat</span>
            <span className="badge">Ví carbon đa vai trò</span>
          </div>
          <div className="hero__actions">
            <button onClick={pingBackend}>Kiểm tra kết nối backend</button>
            <div
              className="status-pill"
              style={{
                background: backendStatus.ok
                  ? "rgba(34,197,94,0.2)"
                  : "rgba(255,255,255,0.15)",
                borderColor: backendStatus.ok
                  ? "rgba(34,197,94,0.6)"
                  : "rgba(255,255,255,0.35)",
              }}
            >
              {backendStatus.text}
            </div>
          </div>
          <div className="hero__stats">
            <div className="stat-card">
              <strong>120k+</strong>
              <span>kg CO₂ giảm</span>
            </div>
            <div className="stat-card">
              <strong>3 role</strong>
              <span>Owner · Buyer · Verifier</span>
            </div>
            <div className="stat-card">
              <strong>24/7</strong>
              <span>API Spring Boot</span>
            </div>
          </div>
          <nav className="role-nav" aria-label="Chọn vai trò">
            {["owner", "buyer", "auditor"].map((role) => (
              <button
                key={role}
                className={activeRole === role ? "active" : ""}
                onClick={() => {
                  setViewMode("guest");
                  openRole(role);
                }}
              >
                {role === "owner"
                  ? "EV Owner"
                  : role === "buyer"
                  ? "Carbon Buyer"
                  : "Verifier"}
              </button>
            ))}
            <button
              className={viewMode === "admin" ? "active" : ""}
              onClick={() => {
                setViewMode("admin");
                openRole("admin");
              }}
            >
              Admin
            </button>
          </nav>
        </div>
      </section>

      <section className="overview" id="overview">
        <div className="overview__intro">
          <p className="eyebrow">Tổng quan</p>
          <h2>Quy trình carbon liền mạch</h2>
          <p>
            Đồng bộ hành trình, xác minh tín chỉ và giao dịch đều thực hiện ngay trên một trang duy nhất.
            Các khối dưới đây mô tả nhanh ba bước chính trước khi đi tới từng vai trò bên dưới.
          </p>
        </div>
        <div className="feature-grid">
          <article className="feature-card">
            <h3>1. Theo dõi & đồng bộ</h3>
            <p>Upload CSV hành trình, tự động tính CO₂ và chuyển thành credit.</p>
            <span>→ API /journeys</span>
          </article>
          <article className="feature-card">
            <h3>2. Xác minh & cấp tín chỉ</h3>
            <p>Verifier duyệt yêu cầu, sinh báo cáo và cấp credit cho ví.</p>
            <span>→ API /verification</span>
          </article>
          <article className="feature-card">
            <h3>3. Niêm yết & giao dịch</h3>
            <p>Listing, giao dịch, báo cáo nền tảng và xử lý tranh chấp.</p>
            <span>→ API /listings · /transactions · /admin</span>
          </article>
        </div>
      </section>

      <main>
        <div className="role-intro">
          <p className="eyebrow">Bảng điều khiển</p>
          <h2>{viewMode === "guest" ? "Làm việc theo vai trò" : "Bảng điều khiển Admin"}</h2>
          <p>
            {viewMode === "guest"
              ? "Chọn một tab để thao tác trực tiếp với backend."
              : "Tập trung vào quản trị người dùng, listing và giao dịch."}
          </p>
        </div>

        {viewMode === "guest" ? (
          <>
            {ownerPanel}
            {buyerPanel}
            {auditorPanel}
          </>
        ) : adminSession.loggedIn ? (
          adminPanel
        ) : (
          <section className="role-panel" id="admin">
            <SectionTitle
              title="Admin"
              description="Yêu cầu đăng nhập để truy cập bảng điều khiển quản trị."
            />
            <div className="result">
              Hãy nhấn “Đăng nhập Admin” ở góc phải phía trên để nhập tài khoản
              `admin / admin123`.
            </div>
          </section>
        )}
      </main>

      <section className="timeline" id="timeline">
        <h2>Hành trình tín chỉ carbon</h2>
        <div className="timeline__wrap">
          <div className="timeline__item">
            <span>01</span>
            <h3>Kết nối xe điện</h3>
            <p>Đồng bộ CSV hoặc telematics để tính baseline CO₂.</p>
          </div>
          <div className="timeline__item">
            <span>02</span>
            <h3>Xác minh</h3>
            <p>Verifier duyệt, tạo báo cáo và cấp credit vào ví.</p>
          </div>
          <div className="timeline__item">
            <span>03</span>
            <h3>Niêm yết</h3>
            <p>Owner đăng listing fixed price hoặc đấu giá.</p>
          </div>
          <div className="timeline__item">
            <span>04</span>
            <h3>Giao dịch & quản trị</h3>
            <p>Buyer mua, nhận certificate; admin giám sát và giải quyết tranh chấp.</p>
          </div>
        </div>
      </section>

      <section className="cta-band" id="cta">
        <div>
          <p className="eyebrow">Sẵn sàng thử?</p>
          <h2>Khởi chạy backend + frontend và thao tác trực tiếp.</h2>
        </div>
        <div className="cta-band__actions">
          <button onClick={() => openRole("owner")}>EV Owner</button>
          <button className="ghost" onClick={() => openRole("buyer")}>
            Buyer
          </button>
          <button className="ghost" onClick={() => openRole("auditor")}>
            Verifier
          </button>
        </div>
      </section>

      <footer className="footer">
        <p>
          Backend base URL: <code>{API_BASE}</code>
        </p>
        <p>Dev server React (npm start) giữ origin http://localhost:3000 để tránh CORS.</p>
      </footer>

      <ChatWidget />
      <AdminLoginModal
        open={loginModalOpen}
        onClose={() => setLoginModalOpen(false)}
        onSuccess={handleAdminLogin}
      />
    </>
  );
}

export default App;

