import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("Ten chu xe: ");
        String name = input.nextLine();

        EVOwner user = new EVOwner(name);

        System.out.print("Quang duong da chay (km): ");
        double distance = input.nextDouble();

        System.out.print("Luong dien tieu thu (kWh): ");
        double kwh = input.nextDouble();

        // đưa dữ liệu vào hệ thống
        EVDataLoader data = new EVDataLoader(distance, kwh);
        user.syncEVData(data);

        user.showWallet();

        // niêm yết bán
        System.out.print("Luong tin chi muon ban: ");
        double amount = input.nextDouble();

        System.out.print("Gia ban moi tin chi: ");
        double price = input.nextDouble();

        System.out.print("Ban theo fixed price? (true/false): ");
        boolean fixed = input.nextBoolean();

        user.listCreditsForSale(amount, price, fixed);

        user.showWallet();
        user.showTransactions();

        input.close();
    }
}
