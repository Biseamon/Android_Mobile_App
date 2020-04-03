package com.example.authorisationfirebase;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.List;

public class TSP {


    public static int maxGenerations = 100;
    //public  List<String> cities;
    public String data;

    private String getRoute;
    private List<LatLng> getRouteLatLng;

    DecimalFormat decimalFormat = new DecimalFormat("#.##"); //changes a double number format.

    private long startTimeDistance;     //Timer starter.
    private long endTimeDistance;      //Stops the times.
    private long durationMillis;      //calculates the duration between start and finish of the timer.


    public TSP(){}

    public void main(List<LatLng> listOfLatLngs, List<String> cityNames, List<String> distances, List<String> durations) {

        // check for consistency
        if (cityNames.size() * cityNames.size() != distances.size()
                && cityNames.size() * cityNames.size() != durations.size()) {
            throw new RuntimeException("data do not match!");
        }

        // Create cities
        int numCities = cityNames.size();
        City3[] cities = new City3[numCities];

        // Loop to create random cities
        for (int i = 0; i < numCities; i++) {
            // Add city
            cities[i] = new City3(i, listOfLatLngs.get(i), cityNames.get(i), distances.subList(i * numCities, (i + 1) * numCities), durations.subList(i * numCities, (i + 1) * numCities));
        }

        startTimeDistance = System.currentTimeMillis(); //starts the timer.

        // Initial GA
//        GeneticAlgorithm3 ga = new GeneticAlgorithm3(numCities, 0.001, 0.9, 2, 5);
        GeneticAlgorithm3 ga = new GeneticAlgorithm3(numCities, 0.001, 0.9, 2, Math.min(numCities, 5));
        // Initialize population
        Population3 population = ga.initPopulation(cities.length);

        // Evaluate population
        ga.evalPopulation(population, cities);

        Route3 startRoute = new Route3(population.getFittest(0), cities);
        System.out.println("Start Distance: " + startRoute.getDistance() + " " + startRoute.getDuration());

        // Keep track of current generation
        int generation = 1;
        // Start evolution loop
        while (ga.isTerminationConditionMet(generation, maxGenerations) == false) {
            // Print fittest individual from population
            Route3 route = new Route3(population.getFittest(0), cities);
            System.out.println("G" + generation + "  route: " + route + " Best distance: " + route.getDistance() +
                               " " + route.getDuration() + route.getLatLngRoute().toString());

            // Apply crossover
            population = ga.crossoverPopulation(population);

            // Apply mutation
            population = ga.mutatePopulation(population);

            // Evaluate population
            ga.evalPopulation(population, cities);

            // Increment the current generation
            generation++;
        }

        System.out.println("Stopped after " + maxGenerations + " generations.");
        Route3 route = new Route3(population.getFittest(0), cities);
        System.out.println("Best distance: " + route.getDistance() + " " + route.getDuration() + "\n"
                           + route.toString() +" " + population.getPopulationFitness());

        setRoute(route.toString());
        setRouteLatLng(route.getLatLngRoute());

        endTimeDistance = System.currentTimeMillis();  //stops the timer.

        durationMillis = endTimeDistance - startTimeDistance;  //calculates the time spent for processing.

        data = "Genetic Algorithm " + "\n" + decimalFormat.format(route.getDistance())
                + " km " + "\n" + decimalFormat.format(route.getDuration())  + " min " + "\n" +
                durationMillis + " ms"; //updates the textView field by adding total distance, duration and processing time.


    }

    public String setRoute(String route){
        this.getRoute = route;
        return route;
    }

    public String getRoute(){
        return this.getRoute;
    }

    public String getDetailsGA(){
        return this.data;
    }

    public List<LatLng> getRouteLatLng(){
        return this.getRouteLatLng;
    }

    public List<LatLng> setRouteLatLng(List<LatLng> route){
        this.getRouteLatLng = route;
        return route;
    }


}
