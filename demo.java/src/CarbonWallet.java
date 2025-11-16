public class CarbonWallet {
    private double credits;

    public void addCredits(double amount) { credits += amount; }
    public boolean withdrawCredits(double amount) {
        if (amount <= credits) { credits -= amount; return true; }
        return false;
    }
    public double getBalance() { return credits; }
}
