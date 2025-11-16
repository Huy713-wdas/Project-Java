class EVDataLoader {
      private double distanceKm;
      private double energyKWh;

    public EVDataLoader(double distanceKm, double energyKWh) {
        this.distanceKm = distanceKm;
        this.energyKWh = energyKWh;
    }

    public double getDistanceKm() { return distanceKm; }
    public double getEnergyKWh() { return energyKWh; }
    
}
