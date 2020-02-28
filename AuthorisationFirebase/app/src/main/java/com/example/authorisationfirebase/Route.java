package com.example.authorisationfirebase;

public class Route {

    private Distance route[];
    private Duration routeDuration[];
    private double distance = 0;
    private double duration = 0;

    /**
     * Initialize Route
     *
     * @param individual
     *            A GA individual
     * @param cities
     *            The cities referenced
     */
    public Route(Individual individual, Distance cities[], Duration durations[]) {
        // Get individual's chromosome
        int chromosome[] = individual.getChromosome();
        // Create route
        this.route = new Distance[cities.length];
        this.routeDuration = new Duration[durations.length];
        for (int geneIndex = 0; geneIndex < chromosome.length; geneIndex++) {
            this.route[geneIndex] = cities[chromosome[geneIndex]];
            this.routeDuration[geneIndex] = durations[chromosome[geneIndex]];
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
            totalDistance += this.route[cityIndex].distanceFrom(this.route[cityIndex + 1]);
        }

        totalDistance += this.route[this.route.length - 1].distanceFrom(this.route[0]);
        this.distance = totalDistance;

        return totalDistance;
    }

    public double getDuration() {
        if (this.duration > 0) {
            return this.duration;
        }

        // Loop over cities in route and calculate route distance
        double totalDuration = 0;
        for (int cityIndex = 0; cityIndex + 1 < this.routeDuration.length; cityIndex++) {
            totalDuration += this.routeDuration[cityIndex].durationFrom(this.routeDuration[cityIndex + 1]);
        }

        totalDuration += this.routeDuration[this.routeDuration.length - 1].durationFrom(this.routeDuration[0]);
        this.duration = totalDuration;

        return totalDuration;
    }

}
