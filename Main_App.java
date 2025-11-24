package com.carbonmarket.app;

import com.carbonmarket.model.CarbonCredit;
import com.carbonmarket.service.CVA_Service;
import java.util.List;

public class Main_App {
    public static void main(String[] args) {
        // Khởi tạo dịch vụ CVA
        CVA_Service cvaService = new CVA_Service();

        // 1. Giả lập EV Owner gửi yêu cầu phát hành tín chỉ
        CarbonCredit req1 = new CarbonCredit("CC001", "EV001", 1250.0); // 1.25 tấn -> 1 tín chỉ
        CarbonCredit req2 = new CarbonCredit("CC002", "EV002", 500.0);  // 0.5 tấn -> 0 tín chỉ
        CarbonCredit req3 = new CarbonCredit("CC003", "EV003", 2000.0); // 2.0 tấn -> 2 tín chỉ

        cvaService.submitCreditRequest(req1);
        cvaService.submitCreditRequest(req2);
        cvaService.submitCreditRequest(req3);

        // --- Bắt đầu quy trình của CVA ---

        System.out.println("\n=============================================");
        System.out.println("QUY TRÌNH KIỂM TOÁN VÀ XÁC MINH CARBON (CVA)");
        System.out.println("=============================================");

        // 2. CVA kiểm tra và lấy danh sách yêu cầu đang chờ
        System.out.println("\n## 1. Kiểm tra Dữ liệu & Hồ sơ Tín chỉ (Pending Requests)");
        List<CarbonCredit> pending = cvaService.getPendingRequests();
        if (pending.isEmpty()) {
            System.out.println("Hiện không có yêu cầu nào đang chờ.");
        } else {
            pending.forEach(System.out::println);
        }

        // 3. CVA Duyệt (APPROVED) và Cấp tín chỉ (ISSUE)
        System.out.println("\n## 2. Duyệt/Từ chối Yêu cầu & Cấp Tín chỉ");
        // Duyệt và Cấp cho req1
        cvaService.processCreditRequest("CC001", true); 
        // Từ chối req2 (giả sử dữ liệu không hợp lệ hoặc quá ít)
        cvaService.processCreditRequest("CC002", false); 
        // Duyệt và Cấp cho req3
        cvaService.processCreditRequest("CC003", true); 

        // Thử xử lý lại một yêu cầu đã duyệt -> báo lỗi
        cvaService.processCreditRequest("CC001", true); 

        // 4. CVA Xuất báo cáo phát hành
        System.out.println("\n## 3. Xuất Báo cáo Phát hành Tín chỉ Carbon");
        List<CarbonCredit> report = cvaService.generateIssuanceReport();
        if (report.isEmpty()) {
            System.out.println("Chưa có tín chỉ nào được phát hành.");
        } else {
            report.forEach(System.out::println);
        }
        
        System.out.println("\n=============================================");
        System.out.println("KẾT THÚC QUY TRÌNH CVA");
        System.out.println("=============================================");
    }
}
