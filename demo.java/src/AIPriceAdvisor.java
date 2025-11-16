import java.util.Random;

public class AIPriceAdvisor {
    private Random random = new Random();
    public double suggestPrice(double avgPrice) {
        return avgPrice * (0.95 + 0.1 * random.nextDouble());
    }
}
