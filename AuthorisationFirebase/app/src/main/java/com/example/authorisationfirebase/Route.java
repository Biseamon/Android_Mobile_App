package com.example.authorisationfirebase;

import java.text.DecimalFormat;

public class Route {

    private Distance route[];
    private Duration routeDuration[];
    private double distance = 0;
    private double duration = 0;

    DecimalFormat decimalFormat = new DecimalFormat("#.##"); //changes a double number format.

    /**
     * Initialize Route
     *
     * @param individual
     *            A GA individual
     * @param distances
     *            The cities referenced
     */
    public Route(Individual individual, Distance distances[], Duration durations[]) {
        // Get individual's chromosome
        int chromosome[] = individual.getChromosome();
        // Create route
        this.route = new Distance[distances.length];
        this.routeDuration = new Duration[durations.length];
        for (int geneIndex = 0; geneIndex < chromosome.length; geneIndex++) {
            this.route[geneIndex] = distances[chromosome[geneIndex]];
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
        int num = 0;

        for (int cityIndex = 0; cityIndex + 1 < this.route.length; cityIndex++) {
            totalDistance += this.route[cityIndex].distances(this.route[cityIndex + 1]);
            for (int i = 0; i <= 100; i++) {
               num  =  i++;
            }
            //System.out.println(num + " " + "Distance " + decimalFormat.format(totalDistance));
        }



        totalDistance += this.route[this.route.length - 1].distances(  this.route[0]);
        this.distance = totalDistance;

        return totalDistance;
    }

    public double getDuration() {
        if (this.duration > 0) {
           return this.duration;
        }

        // Loop over cities in route and calculate route distance
        double totalDuration = 0;
        int num = 0;

        for (int cityIndex = 0; cityIndex + 1 < this.routeDuration.length; cityIndex++) {
            totalDuration += this.routeDuration[cityIndex].durations(this.routeDuration[cityIndex + 1]);
            for (int i = 0; i <= 100; i++) {
                num = i++;
            }
           //System.out.println(num + " " + "Duration " + decimalFormat.format(totalDuration));
        }



        totalDuration += this.routeDuration[this.routeDuration.length - 1].durations(this.routeDuration[0]);
        this.duration = totalDuration;

        return totalDuration;
    }

}
