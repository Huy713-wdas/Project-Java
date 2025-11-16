import java.util.ArrayList;
import java.util.List;

public class EVOwner {
    private String name;
    private CarbonWallet wallet = new CarbonWallet();
    private List<Transaction> transactions = new ArrayList<>();
    private AIPriceAdvisor advisor = new AIPriceAdvisor();
    private static int txCounter = 1;

    public EVOwner(String name) { this.name = name; }

    public void syncEVData(EVDataLoader data) {
        double co2Saved = CarbonCalculator.calculateCO2Saved(data);
        double credits = CarbonCalculator.calculateCredits(co2Saved);
        wallet.addCredits(credits);
        System.out.printf("%s giảm được %.2f kg CO₂ (%.3f tín chỉ)%n", name, co2Saved, credits);
    }
public void listCreditsForSale(double credits, double marketAvg, boolean isAuction) {
        if (!wallet.withdrawCredits(credits)) {
            System.out.println("Không đủ tín chỉ để niêm yết.");
            return;
        }
        double price = advisor.suggestPrice(marketAvg);
        Transaction tx = new Transaction(txCounter++, credits, price,
                isAuction ? Transaction.Type.AUCTION : Transaction.Type.FIXED_PRICE);
        transactions.add(tx);
        System.out.println("Đã niêm yết: " + tx);
    }
 public void showWallet() {
        System.out.println("💰 Số dư ví: " + wallet.getBalance() + " tín chỉ.");
    }

    public void showTransactions() {
        System.out.println("--- Lịch sử giao dịch ---");
        transactions.forEach(System.out::println);
    }
}
