package com.example.authorisationfirebase;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetDistanceMatrixData extends AsyncTask<Object,String,String> {

    private List<Map<String, String>> nearbyPlaceList;
    private List<String> po;
    private List<String> pd;
    private String[][] distanceMatrix;
    private String[][] durationMatrix;
    private List<String> pDist;
    private List<String> pDuration;

    public List<String> elementNo1;
    private List<String> elementNo2;
    private List<String> elementNo3;
    private List<String> elementNo4;
    private List<String> elementNo5;
    private List<String> elementNo6;
    private List<String> elementNo7;
    private List<String> elementNo8;
    private List<String> elementNo9;
    private List<String> elementNo10;

    private List<String> elementDuration1;
    private List<String> elementDuration2;
    private List<String> elementDuration3;
    private List<String> elementDuration4;
    private List<String> elementDuration5;
    private List<String> elementDuration6;
    private List<String> elementDuration7;
    private List<String> elementDuration8;
    private List<String> elementDuration9;
    private List<String> elementDuration10;

    private String googlePlacesData;
    private GoogleMap mMap;
    private String url;
    public String distanceMatrixUrl;
    private String finalUrl;

    private long startTimeDistance;
    private long endTimeDistance;
    private long durationMillis;

    public static int maxGenerations = 100;

    public GetDistanceMatrixData(){}

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];

        DownloadUrl downloadURL = new DownloadUrl();
        try {
            googlePlacesData = downloadURL.readUrl(url);
            Parser parser = new Parser();
            nearbyPlaceList = parser.parseTest(googlePlacesData);

            String origins =  getOrigins();

            String destinations = getDestinations();

            distanceMatrixUrl = getDistanceMatrix(origins, destinations);
            Log.i("distMatrix", "doInBackground: " + distanceMatrixUrl);

            DownloadUrl downloadUrl1 = new DownloadUrl();
            String distMat = distanceMatrixUrl;
            finalUrl = "";
            try {
                finalUrl = downloadUrl1.readUrl(distMat);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.i("placesNear", "doInBackground: " + nearbyPlaceList.toString());

            getNearbyPlaceId(nearbyPlaceList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return finalUrl;
    }

    @Override
    protected void onPostExecute(String s) {

        Parser parser = new Parser();

        po = parser.parseOrigins(s);
        pd = parser.parseDestinations(s);
        pDist = parser.parseDistanceFromMatrix(s);
        pDuration = parser.parseDurationFromMatrix(s);

        elementNo1 = pDist.subList(0, 10);
        elementNo2 = pDist.subList(10, 20);
        elementNo3 = pDist.subList(20, 30);
        elementNo4 = pDist.subList(30, 40);
        elementNo5 = pDist.subList(40, 50);
        elementNo6 = pDist.subList(50, 60);
        elementNo7 = pDist.subList(60, 70);
        elementNo8 = pDist.subList(70, 80);
        elementNo9 = pDist.subList(80, 90);
        elementNo10 = pDist.subList(90,100);


        elementDuration1 = pDuration.subList(0, 10);
        elementDuration2 = pDuration.subList(10, 20);
        elementDuration3 = pDuration.subList(20, 30);
        elementDuration4 = pDuration.subList(30, 40);
        elementDuration5 = pDuration.subList(40, 50);
        elementDuration6 = pDuration.subList(50, 60);
        elementDuration7 = pDuration.subList(60, 70);
        elementDuration8 = pDuration.subList(70, 80);
        elementDuration9 = pDuration.subList(80, 90);
        elementDuration10 = pDuration.subList(90,100);


        distanceMatrix = new String[po.size()][pd.size()];
        durationMatrix = new String[po.size()][pd.size()];

//        for (int i = 0; i < distanceMatrix.length; i++) {
//            for (int j = 0; j < distanceMatrix[i].length; j++) {
//                distanceMatrix[0][j] = elementNo1.get(j);
//                distanceMatrix[1][j] = elementNo2.get(j);
//                distanceMatrix[2][j] = elementNo3.get(j);
//                distanceMatrix[3][j] = elementNo4.get(j);
//                distanceMatrix[4][j] = elementNo5.get(j);
//                distanceMatrix[5][j] = elementNo6.get(j);
//                distanceMatrix[6][j] = elementNo7.get(j);
//                distanceMatrix[7][j] = elementNo8.get(j);
//                distanceMatrix[8][j] = elementNo9.get(j);
//                distanceMatrix[9][j] = elementNo10.get(j);
//
//                cities[i] = new City(i,j);
//            }
//        }

//        for (int i = 0; i < durationMatrix.length; i++) {
//            for (int j = 0; j < durationMatrix[i].length; j++) {
//                durationMatrix[0][j] = elementDuration1.get(j);
//                durationMatrix[1][j] = elementDuration2.get(j);
//                durationMatrix[2][j] = elementDuration3.get(j);
//                durationMatrix[3][j] = elementDuration4.get(j);
//                durationMatrix[4][j] = elementDuration5.get(j);
//                durationMatrix[5][j] = elementDuration6.get(j);
//                durationMatrix[6][j] = elementDuration7.get(j);
//                durationMatrix[7][j] = elementDuration8.get(j);
//                durationMatrix[8][j] = elementDuration9.get(j);
//                durationMatrix[9][j] = elementDuration10.get(j);
//            }
//        }

// Create cities
        //int numCities = 10;
        Distance distances[] = new Distance[po.size()];
        Duration durations[] = new Duration[po.size()];

        startTimeDistance = System.currentTimeMillis();

        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                distanceMatrix[0][j] = elementNo1.get(j).replace("m","")
                        .replace("k", "")
                        .replace(" " , "");
                distanceMatrix[1][j] = elementNo2.get(j).replace("m","")
                        .replace("k", "")
                        .replace(" " , "");
                distanceMatrix[2][j] = elementNo3.get(j).replace("m","")
                        .replace("k", "")
                        .replace(" " , "");
                distanceMatrix[3][j] = elementNo4.get(j).replace("m","")
                        .replace("k", "")
                        .replace(" " , "");
                distanceMatrix[4][j] = elementNo5.get(j).replace("m","")
                        .replace("k", "")
                        .replace(" " , "");
                distanceMatrix[5][j] = elementNo6.get(j).replace("m","")
                        .replace("k", "")
                        .replace(" " , "");
                distanceMatrix[6][j] = elementNo7.get(j).replace("m","")
                        .replace("k", "")
                        .replace(" " , "");
                distanceMatrix[7][j] = elementNo8.get(j).replace("m","")
                        .replace("k", "")
                        .replace(" " , "");
                distanceMatrix[8][j] = elementNo9.get(j).replace("m","")
                        .replace("k", "")
                        .replace(" " , "");
                distanceMatrix[9][j] = elementNo10.get(j).replace("m","")
                        .replace("k", "")
                        .replace(" " , "");

                int x = (int) Double.parseDouble(distanceMatrix[i][0]);
                int y = (int) Double.parseDouble(distanceMatrix[0][j]);

              distances[i] = new Distance(x,y);
            }
        }


        for (int i = 0; i < durationMatrix.length; i++) {
            for (int j = 0; j < durationMatrix[i].length; j++) {
                durationMatrix[0][j] = elementDuration1.get(j).replace("min","")
                        .replace("s", "")
                        .replace(" " , "");
                durationMatrix[1][j] = elementDuration2.get(j).replace("min","")
                        .replace("s", "")
                        .replace(" " , "");
                durationMatrix[2][j] = elementDuration3.get(j).replace("min","")
                        .replace("s", "")
                        .replace(" " , "");
                durationMatrix[3][j] = elementDuration4.get(j).replace("min","")
                        .replace("s", "")
                        .replace(" " , "");
                durationMatrix[4][j] = elementDuration5.get(j).replace("min","")
                        .replace("s", "")
                        .replace(" " , "");
                durationMatrix[5][j] = elementDuration6.get(j).replace("min","")
                        .replace("s", "")
                        .replace(" " , "");
                durationMatrix[6][j] = elementDuration7.get(j).replace("min","")
                        .replace("s", "")
                        .replace(" " , "");
                durationMatrix[7][j] = elementDuration8.get(j).replace("min","")
                        .replace("s", "")
                        .replace(" " , "");
                durationMatrix[8][j] = elementDuration9.get(j).replace("min","")
                        .replace("s", "")
                        .replace(" " , "");
                durationMatrix[9][j] = elementDuration10.get(j).replace("min","")
                        .replace("s", "")
                        .replace(" " , "");

                Log.i("rofl", "onPostExecute: " + Integer.parseInt(durationMatrix[i][j]));

                int x = Integer.parseInt(durationMatrix[i][0]);
                int y = Integer.parseInt(durationMatrix[0][j]);

                    durations[i] = new Duration(x,y);

            }
        }

        // Initial GA
        GeneticAlgorithm ga = new GeneticAlgorithm(100, 0.001, 0.9, 2, 10);

        // Initialize population
        Population population = (Population) ga.initPopulation(distances.length, durations.length);

        // Evaluate population
        ga.evalPopulation(population, distances, durations);

        Route startRoute = new Route(population.getFittest(0), distances, durations);

        Log.d("Start Distance: " , "" + startRoute.getDistance());

        // Keep track of current generation
        int generation = 1;
        // Start evolution loop
        while (ga.isTerminationConditionMet(generation, maxGenerations) == false) {
            // Print fittest individual from population
            Route route = new Route(population.getFittest(0), distances, durations);
            System.out.println("G"+generation+" Distance and duration: " + route.getDistance() + " km " + route.getDuration() + " min");

            // Apply crossover
            population = ga.crossoverPopulation(population);

            // Apply mutation
            population = ga.mutatePopulation(population);

            // Evaluate population
            ga.evalPopulation(population, distances, durations);

            // Increment the current generation
            generation++;
        }

        Route route = new Route(population.getFittest(0), distances,durations);

        endTimeDistance = System.currentTimeMillis();

        durationMillis = endTimeDistance - startTimeDistance;

        Log.d("Distance&Duration: ", "distance " + route.getDistance() + " km" + " and duration " + route.getDuration() + " min"
                                                            + " " + durationMillis + " ms" +
                "Stopped after " + maxGenerations + " generations."   );

        System.out.print(getAllDistancesAndDurations());

    }

    public String getAllDistancesAndDurations() {

        String result = "";
        List<String> fromTo = new ArrayList<>();

        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                fromTo.add(originAndDestinationName(i,j) + distanceMatrix[i][j] +" "+ durationMatrix[i][j] + "\n");
                result = fromTo.toString();
            }
        }
            return result;
    }

    public String getDistances(){
        String ss = "";
        List<String> distances = new ArrayList<>();
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                distances.add(distanceMatrix[i][j].replace("m","")
                .replace("k", "")
                .replace(" " , ""));
                ss = distances.toString();
            }
        }
        return ss;
    }

    public String originAndDestinationName(int x, int y){
        String origin;
        String destination ;

            origin = po.get(x);
            destination = pd.get(y);

        return "From " + origin + " To " + destination + " : ";
    }

    public String getOrigins(){
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(nearbyPlaceList.get(i));
        }
        return list.toString().replace("{", "")
                .replace("}","")
                .replace("=", ":");
    }

    public String getDestinations(){
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            list.add(nearbyPlaceList.get(i));
        }
        return list.toString().replace("{", "")
                .replace("}","")
                .replace("=", ":");
    }

    public String getNearbyPlaceId(List<Map<String, String>> nearbyPlaceList) {

        List<Map<String,String>> placesIdList = new ArrayList<>();

        for (int i = 0; i < nearbyPlaceList.size(); i++) {

            placesIdList.add(nearbyPlaceList.get(i));

            Log.i("theList3", placesIdList.toString()
                    .replace("=",":")
                    .replace("{","")
                    .replace("}","")
                    .replace(",","|"));
        }

        return placesIdList.toString().replace("[", "")
                .replace("]", "")
                .replace("{", "")
                .replace("}", "")
                .replace("=", ":")
                .replace(" ", "")
                .replace(",", "|");
    }

    public String getDistanceMatrix(String origins, String destinations){
        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/distancematrix/json?");
        stringBuilder.append("origins=" + origins);
        stringBuilder.append("&destinations=" + destinations);
        stringBuilder.append("&mode=driving");
        stringBuilder.append("&key=" + "AIzaSyCuRGuOxVFfA2rs5gT-w2Y8K_RSlgzualg");

        return stringBuilder.toString().replace("[","")
                .replace("]","")
                .replace(",","|")
                .replace(" ","");
    }

}
