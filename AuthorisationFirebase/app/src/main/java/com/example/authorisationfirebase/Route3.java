package com.example.authorisationfirebase;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Route3 {

    private City3[] route;
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
    public Route3(Individual3 individual, City3[] cities) {
        // Get individual's chromosome
        int[] chromosome = individual.getChromosome();
        // Create route
        this.route = new City3[cities.length];
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
            City3 c1 = this.route[cityIndex];
            City3 c2 = this.route[cityIndex + 1];
            double d = c1.distanceFrom(c2);
            totalDistance += d;
        }

//		totalDistance += this.route[this.route.length - 1].distanceFrom(this.route[0]);
        this.distance = totalDistance;

        return totalDistance;
    }



    /**
     * Get route duration
     *
     * @return distance The route's duration
     */
    public double getDuration() {
        if (this.duration > 0) {
            return this.duration;
        }

        // Loop over cities in route and calculate route distance
        double totalDuration = 0;
        for (int cityIndex = 0; cityIndex + 1 < this.route.length; cityIndex++) {
            City3 c1 = this.route[cityIndex];
            City3 c2 = this.route[cityIndex + 1];
            double d = c1.durationFrom(c2);
            totalDuration += d;
        }

//		totalDistance += this.route[this.route.length - 1].distanceFrom(this.route[0]);
        this.duration = totalDuration;

        return totalDuration;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String[] firstAndLastPlace = null;
        for (int i = 0; i < route.length; i++) {

            String[] shortNames = route[i].name.split("-");
             firstAndLastPlace = route[0].name.split("-");

            sb.append(" " + "-" + route[i] + "-" +  "[" + shortNames[0] + "]");

//            sb.append(":  " + distance);
//            sb.append(":  " + duration);
        }
        sb.append(" " + "-0-" + "[" + firstAndLastPlace[0] + "]");

        return sb.toString();
    }

    public List<LatLng> getLatLngRoute(){

        List<LatLng> list = new ArrayList<>();

        for (int i = 0; i < route.length; i++) {
            list.add(route[i].latLng);
        }
        return list;
    }

    public String getNamesOfTheRoute(){

        List<String> list = new ArrayList<>();
        String[] firstAndLastPlace = null;


        for (int i = 0; i < route.length; i++) {

            String[] shortNames = route[i].name.split("-");
            firstAndLastPlace = route[0].name.split("-");
            list.add(" " + "-" + route[i] + "-" + "[" + shortNames[0] + "]");
        }

        list.add(" " + "-0-" + "[" + firstAndLastPlace[0] + "]");

        return list.toString();
    }



}
