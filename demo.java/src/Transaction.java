public class Transaction {
    enum Type { FIXED_PRICE, AUCTION }
    enum Status { LISTED, SOLD, CANCELLED }

    private int id;
    private double credits;
    private double price;
    private Type type;
    private Status status;
    public Transaction(int id, double credits, double price, Type type) {
        this.id = id;
        this.credits = credits;
        this.price = price;
        this.type = type;
        this.status = Status.LISTED;
    }

    public void markSold() { status = Status.SOLD; }
    public void cancel() { status = Status.CANCELLED; }
    public String toString() {
        return String.format("TX #%d | %.3f credits @ %.2f (%s) [%s]",
            id, credits, price, type, status);
    }
}
