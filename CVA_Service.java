package com.carbonmarket.service;

import com.carbonmarket.model.CarbonCredit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CVA_Service {
    // Giả lập cơ sở dữ liệu cho các yêu cầu tín chỉ
    private List<CarbonCredit> creditRequests;

    // Giả lập cơ sở dữ liệu/ví carbon cho các tín chỉ đã được cấp
    private List<CarbonCredit> issuedCredits;

    public CVA_Service() {
        this.creditRequests = new ArrayList<>();
        this.issuedCredits = new ArrayList<>();
    }

    // Giả lập chức năng từ EV Owner: tạo yêu cầu phát hành tín chỉ
    public void submitCreditRequest(CarbonCredit credit) {
        System.out.println("\n[LOG] Nhận yêu cầu tín chỉ từ EV Owner: " + credit.getCreditId());
        creditRequests.add(credit);
    }

    // --- CHỨC NĂNG CỐT LÕI CỦA CVA ---

    /**
     * Chức năng 1: Kiểm tra dữ liệu phát thải & hồ sơ tín chỉ.
     * Trả về danh sách các yêu cầu đang chờ duyệt.
     */
    public List<CarbonCredit> getPendingRequests() {
        return creditRequests.stream()
                             .filter(c -> c.getStatus().equals("PENDING_VERIFICATION"))
                             .collect(Collectors.toList());
    }

    /**
     * Chức năng 2 & 3: Duyệt, từ chối, Cấp tín chỉ và ghi vào ví carbon.
     * @param creditId ID của tín chỉ cần xử lý.
     * @param isApproved true nếu duyệt, false nếu từ chối.
     * @return true nếu xử lý thành công, false nếu không tìm thấy.
     */
    public boolean processCreditRequest(String creditId, boolean isApproved) {
        CarbonCredit credit = creditRequests.stream()
                                            .filter(c -> c.getCreditId().equals(creditId) && 
                                                         c.getStatus().equals("PENDING_VERIFICATION"))
                                            .findFirst()
                                            .orElse(null);

        if (credit == null) {
            System.out.println("❌ Lỗi: Không tìm thấy yêu cầu tín chỉ ID: " + creditId + " hoặc đã được xử lý.");
            return false;
        }

        if (isApproved) {
            // Logic Giả lập Kiểm tra Dữ liệu (Dữ liệu hành trình, GPS, ...):
            // Trong thực tế, đây là bước phức tạp nhất, nơi CVA đối chiếu dữ liệu thô
            System.out.println("Đang tiến hành **Kiểm tra dữ liệu** cho Credit ID: " + creditId + "...");
            // Giả lập việc kiểm tra thành công
            
            credit.setStatus("APPROVED");
            System.out.println("Yêu cầu tín chỉ ID: " + creditId + " **ĐÃ ĐƯỢC DUYỆT**.");
            
            // Cấp tín chỉ và ghi vào ví carbon
            issueCredit(credit); 

        } else {
            credit.setStatus("REJECTED");
            System.out.println(" Yêu cầu tín chỉ ID: " + creditId + " **ĐÃ BỊ TỪ CHỐI**.");
        }
        return true;
    }
    
    // Hàm nội bộ: Cấp tín chỉ và Ghi vào Ví Carbon (thực tế sẽ cập nhật database)
    private void issueCredit(CarbonCredit credit) {
        credit.setStatus("ISSUED");
        issuedCredits.add(credit);
        System.out.println(" **Đã Cấp Tín chỉ** (" + credit.getCreditAmount() + " Credits) cho EV Owner: " + credit.getEvOwnerId());
        // Trong hệ thống thật, logic này sẽ gửi thông báo và cập nhật số dư ví của EV Owner.
    }
    
    /**
     * Chức năng 4: Xuất báo cáo phát hành tín chỉ carbon.
     * @return Danh sách các tín chỉ đã được cấp.
     */
    public List<CarbonCredit> generateIssuanceReport() {
        System.out.println("\n--- BÁO CÁO PHÁT HÀNH TÍN CHỈ ---");
        return issuedCredits;
    }
}
