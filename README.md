# Project Java - EV Carbon

## 1. Chức năng chính

### EV Owner
- Kết nối và đồng bộ hành trình (CSV) để tính CO₂ giảm và tín chỉ.
- Quản lý ví carbon & ví tiền (nạp/rút).
- Niêm yết tín chỉ (fixed price/auction), theo dõi giao dịch và thanh toán.
- Xem báo cáo cá nhân (CO₂ giảm, doanh thu).
- AI gợi ý giá bán dựa trên dữ liệu thị trường.

### Carbon Credit Buyer
- Tìm kiếm & lọc listing theo số lượng, giá, khu vực.
- Mua tín chỉ trực tiếp, thanh toán đa phương thức, nhận certificate.
- Quản lý lịch sử mua tín chỉ.

### Verification / Audit
- Kiểm tra dữ liệu, duyệt/từ chối yêu cầu phát hành tín chỉ.
- Cấp tín chỉ, ghi vào ví và xuất báo cáo audit.

### Admin
- Quản lý người dùng (EV Owner, Buyer, Verifier, Admin).
- Theo dõi giao dịch, xử lý tranh chấp, quản lý dòng tiền và listing.
- Báo cáo tổng hợp giao dịch toàn nền tảng.

## 2. Hướng dẫn chạy nhanh

1. **Backend** or chạy file demo cũng oke 
   - Cài JDK 21+ và đặt `JAVA_HOME`.
   - Chạy `run-backend.bat` (script tự cd vào `demo/` và gọi `mvnw spring-boot:run`).

2. **Frontend**
   - Chạy `run-frontend.bat`. Lần đầu script sẽ `npm install`, sau đó mở React dev server tại `http://localhost:3000`.
   - UI React gồm hero, overview, bảng điều khiển Owner/Buyer/Verifier, trang Admin, timeline và chat AI (nút nổi góc phải).

Sau khi cả hai service chạy, truy cập `http://localhost:3000`, bấm “Kiểm tra kết nối backend” để xác nhận rồi thao tác với các form để gọi API thật.


