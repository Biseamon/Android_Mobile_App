package com.example.authorisationfirebase;

public class Route2 {

    private City route[];
    private Coordinates[] coordinates;
    private double distance = 0;

    /**
     * Initialize Route
     *
     * @param individual A GA individual
     * @param cities     The cities referenced
     */
    public Route2(Individual2 individual, City cities[]) {
        // Get individual's chromosome
        int chromosome[] = individual.getChromosome();
        // Create route
        this.route = new City[cities.length];
        for (int geneIndex = 0; geneIndex < chromosome.length; geneIndex++) {
            this.route[geneIndex] = cities[chromosome[geneIndex]];
        }
    }

    /**
     * Get route distance
     *
     * @return distance The route's distance
     */
    public double getDistance() {
        if (this.distance > 0) {
            return this.distance;
        }

        // Loop over cities in route and calculate route distance
        double totalDistance = 0;
        for (int cityIndex = 0; cityIndex + 1 < this.route.length; cityIndex++) {
            totalDistance += this.route[cityIndex].distanceBetweenLatLng(this.route[cityIndex + 1]);
        }

        totalDistance += this.route[this.route.length - 1].distanceBetweenLatLng(this.route[0]);
        this.distance = totalDistance;

        return totalDistance;
    }

}
