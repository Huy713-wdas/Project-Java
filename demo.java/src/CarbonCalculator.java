public class CarbonCalculator {
    private static final double EMISSION_FACTOR = 0.12; // kg CO₂/km
    private static final double KG_PER_CREDIT = 1000.0; // 1 tín chỉ = 1 tấn CO₂

    public static double calculateCO2Saved(EVDataLoader data) {
        return data.getDistanceKm() * EMISSION_FACTOR;
    }

    public static double calculateCredits(double co2Saved) {
        return co2Saved / KG_PER_CREDIT;
    }
}
