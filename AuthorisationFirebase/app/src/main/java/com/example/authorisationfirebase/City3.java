package com.example.authorisationfirebase;

import java.util.List;

public class City3 {

    int index;
    String name;
    double[] distances;
    double[] durations;

    public City3(int index, String name, List<String> distanceData, List<String> durationData) {
        this.index = index;
        this.name = name;
        processDistances(distanceData);
        processDurations(durationData);
    }

    private void processDistances(List<String> distanceData) {
        distances = new double[distanceData.size()];
        for (int i = 0; i < distances.length; i++) {
            String[] split = distanceData.get(i).split("\\s+");
            if (split[1].equals("km"))
                distances[i] = Double.parseDouble(split[0]);
            else distances[i] = 0;
        }
    }

    private void processDurations(List<String> durationData) {
        durations = new double[durationData.size()];
        for (int i = 0; i < durations.length; i++) {
            String[] split = durationData.get(i).split("\\s+");
            if (split[1].equals("mins"))
                durations[i] = Double.parseDouble(split[0]);
            else durations[i] = 0;
        }
    }

    /**
     * Calculate distance from another city
     * @param city The city to calculate the distance from
     * @return distance The distance from the given city
     */
    public double distanceFrom(City3 city) {
        return distances[city.index];
    }

    public double durationFrom(City3 city){
        return durations[city.index];
    }

//    /**
//     * Get x position of city
//     *
//     * @return x X position of city
//     */
//    public int getX() {
//        return this.x;
//    }

    //    /**
//     * Get y position of city
//     *
//     * @return y Y position of city
//     */
//    public int getY() {
//        return this.y;
//    }
    @Override
    public String toString() {
        return "" + index;
    }

}
