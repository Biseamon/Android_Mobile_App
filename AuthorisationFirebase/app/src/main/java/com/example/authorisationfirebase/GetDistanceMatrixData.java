package com.example.authorisationfirebase;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.RequiresApi;

public class GetDistanceMatrixData extends AsyncTask<Object,String,String> {

    /**
     * GetDistanceMatrixData.class is responsible for extracting data
     * from a PlaceAPI and use it for a distance matrix API.
     */

    @SuppressLint("StaticFieldLeak")
    private TextView textView; //widget displaying distance time and processing time of the GA.

    private List<Map<String, String>> nearbyPlaceList; //List of nearby places. This list is dynamic and changes every time user chooses a new reference.
    private List<String> placesOrigins;               //List of origin places.
    private List<String> placesDestinations;         //List of destination places.
    private String[][] distanceMatrix;              //distance matrix.
    private String[][] durationMatrix;             //duration matrix.
    private String[][] distanceMatrixDetailed;    //a more detailed distance matrix.
    private String[][] durationMatrixDetailed;   //a more detailed duration matrix.

    DecimalFormat decimalFormat = new DecimalFormat("#.##"); //changes a double number format.

    private String googlePlacesData;      //this variable will contain the distance matrix api data.
    private GoogleMap mMap;              //Google maps.
    private String nearbyPlacesUrl;     //nearby Places API.
    public String distanceMatrixUrl;   //distance matrix API link.
    private String finalUrl;          //final end API link sent to onPostExecute method.

    private long startTimeDistance;     //Timer starter.
    private long endTimeDistance;      //Stops the times.
    private long durationMillis;      //calculates the duration between start and finish of the timer.

    public static int maxGenerations = 100; //number of generations that genetic algorithm will perform.
    List<Double> distancesAAA = new ArrayList<>();


    //public GetDistanceMatrixData(){}  //an empty constructor.
    public GetDistanceMatrixData(TextView txtView){this.textView = txtView;}  //textView constructor.

    @Override
    protected String doInBackground(Object... objects) {

        /**
         * All the background work is done here. Here is happening the
         * download and parse process.
         */

        mMap = (GoogleMap)objects[0];           //map initialisation from the MapsActivity.class
        nearbyPlacesUrl = (String)objects[1];  //link to Places API from MapsActivity.class

        DownloadUrl downloadURL = new DownloadUrl();  //initialising the downloadUrl.class
        try {
            googlePlacesData = downloadURL.readUrl(nearbyPlacesUrl);          //reading and storing the json file from the link into googlePlaceData.
            Parser parser = new Parser();                                    //initialise a new parser.
            nearbyPlaceList = parser.parseDirectionData(googlePlacesData);  //parses the data from the JSON file into nearbyPlaceList.

            String origins =  getOrigins(); //stores all the origin places.

            String destinations = getDestinations();  //stores all the destination places.

            distanceMatrixUrl = getDistanceMatrix(origins, destinations);            //distance matrix API link.
            Log.d("distMatrix", "doInBackground: " + distanceMatrixUrl);  //Test/check the result.

            DownloadUrl downloadUrl1 = new DownloadUrl(); //initialise a new downloadUrl object.

            finalUrl = "";  //final created url.
            try {
                finalUrl = downloadUrl1.readUrl(distanceMatrixUrl); //reads the distance matrix API link.
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d("distanceMatrix", "doInBackground: " + distanceMatrixUrl);  //Test/checks if link returns anything.

        } catch (IOException e) {
            e.printStackTrace();
        }

        return finalUrl; //returns a distance matrix link.
    }

    /**
     * persorms actions after the doInBackground has finished its work.
     * @param s where s is finalUrl returned by the doInBackground method.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onPostExecute(String s) {

        Parser parser = new Parser();  //Initiates a parser.

        placesOrigins = parser.parseOrigins(s);                             //parses origin places.
        placesDestinations = parser.parseDestinations(s);                  //parses destination places.
        List<String> placeDistances = parser.parseDistanceFromMatrix(s);  //parses distances between origin and destinations.
        List<String> placeDurations = parser.parseDurationFromMatrix(s); //parses durations between origin and destinations.

        Map<List<String>, Map<List<String>,List<String>>> mapMap = new HashMap<>();
        Map<List<String>,List<String>> map = new HashMap<>();
        map.put(placesDestinations, placeDistances);
        mapMap.put(placesOrigins, map);

        for ( Map.Entry<List<String>, Map<List<String>, List<String>>> entry : mapMap.entrySet()) {
            List<String> key = entry.getKey(); //origins
            Map<List<String>, List<String>> tab = entry.getValue(); //destinations and distances
            // do something with key and/or tab
            System.out.println(key.toString() + " " + tab.toString());
        }

        Map<List<String>,List<String>> map1 = new LinkedHashMap<>();  // ordered
        Map<List<String>,Map<List<String>,List<String>>> map2 = new LinkedHashMap<>();  // ordered
        map1.put(placesDestinations, placeDistances);
        map2.put(placesOrigins, map1);

        Log.d("distances", "onPostExecute: " + placeDistances.toString());

        distanceMatrix = new String[placesOrigins.size()][placesDestinations.size()];  //distance matrix initialisation.
        durationMatrix = new String[placesOrigins.size()][placesDestinations.size()];  //duration matrix initialisation.
        distanceMatrixDetailed = new String[placesOrigins.size()][placesDestinations.size()];  //detailed distance matrix initialisation.
        durationMatrixDetailed = new String[placesOrigins.size()][placesDestinations.size()];  //detailed duration matrix initialisation.

        /**
         * the loop goes through a detailed distance matrix and populates
         * the element list with data. 10 per element.
         * There are 100 elements in the detailed distance matrix, and every 10 are stored in the elementDistanceNo# list.
         */
        for (int i = 0; i < distanceMatrixDetailed.length; i++) {
            for (int j = 0; j < distanceMatrixDetailed[i].length; j++) {
                distanceMatrixDetailed[0][j] = placeDistances.subList(0, 10).get(j);    //first 10 elements stored.
                distanceMatrixDetailed[1][j] = placeDistances.subList(10, 20).get(j);   //second dozen of elements stored.
                distanceMatrixDetailed[2][j] = placeDistances.subList(20, 30).get(j);  //third dozen of elements stored.
                distanceMatrixDetailed[3][j] = placeDistances.subList(30, 40).get(j); //and so on...
                distanceMatrixDetailed[4][j] = placeDistances.subList(40, 50).get(j);
                distanceMatrixDetailed[5][j] = placeDistances.subList(50, 60).get(j);
                distanceMatrixDetailed[6][j] = placeDistances.subList(60, 70).get(j);
                distanceMatrixDetailed[7][j] = placeDistances.subList(70, 80).get(j);
                distanceMatrixDetailed[8][j] = placeDistances.subList(80, 90).get(j);
                distanceMatrixDetailed[9][j] = placeDistances.subList(90, 100).get(j);
            }
        }

        /**
         * the loop goes through a detailed duration matrix and populates
         * the element list with data. 10 per element.
         * There are 100 elements in the detailed duration matrix, and every 10 are stored in the elementDurationNo# list.
         */
        for (int i = 0; i < durationMatrixDetailed.length; i++) {
            for (int j = 0; j < durationMatrixDetailed[i].length; j++) {
                durationMatrixDetailed[0][j] = placeDurations.subList(0, 10).get(j);     //first 10 elements stored.
                durationMatrixDetailed[1][j] = placeDurations.subList(10, 20).get(j);    //second dozen of elements stored.
                durationMatrixDetailed[2][j] = placeDurations.subList(20, 30).get(j);   //third dozen of elements stored.
                durationMatrixDetailed[3][j] = placeDurations.subList(30, 40).get(j);  //and so on...
                durationMatrixDetailed[4][j] = placeDurations.subList(40, 50).get(j);
                durationMatrixDetailed[5][j] = placeDurations.subList(50, 60).get(j);
                durationMatrixDetailed[6][j] = placeDurations.subList(60, 70).get(j);
                durationMatrixDetailed[7][j] = placeDurations.subList(70, 80).get(j);
                durationMatrixDetailed[8][j] = placeDurations.subList(80, 90).get(j);
                durationMatrixDetailed[9][j] = placeDurations.subList(90, 100).get(j);
            }
        }

        Duration[] durations = new Duration[placesOrigins.size()];  //An array of durations.
        Distance[] distances = new Distance[placesDestinations.size()]; //An array of distances.

        startTimeDistance = System.currentTimeMillis(); //starts the timer.

        /**
         * Stores 10 elements per list "elementDistanceNo1" from distance matrix array.
         */
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix[i].length; j++) {

                distanceMatrix[0][j] = placeDistances.subList(0, 10).get(j).replaceAll("[^0-9.]","");
                distanceMatrix[1][j] = placeDistances.subList(10, 20).get(j).replaceAll("[^0-9.]","");
                distanceMatrix[2][j] = placeDistances.subList(20, 30).get(j).replaceAll("[^0-9.]","");
                distanceMatrix[3][j] = placeDistances.subList(30, 40).get(j).replaceAll("[^0-9.]","");
                distanceMatrix[4][j] = placeDistances.subList(40, 50).get(j).replaceAll("[^0-9.]","");
                distanceMatrix[5][j] = placeDistances.subList(50, 60).get(j).replaceAll("[^0-9.]","");
                distanceMatrix[6][j] = placeDistances.subList(60, 70).get(j).replaceAll("[^0-9.]","");
                distanceMatrix[7][j] = placeDistances.subList(70, 80).get(j).replaceAll("[^0-9.]","");
                distanceMatrix[8][j] = placeDistances.subList(80,90).get(j).replaceAll("[^0-9.]","");
                distanceMatrix[9][j] = placeDistances.subList(90, 100).get(j).replaceAll("[^0-9.]","");

//                double x = Double.parseDouble(distanceMatrix[i][0]);   //Stores the distances one by one in a double variable needed for Distance.class.
//                double y = Double.parseDouble(distanceMatrix[0][j]);  //Stores the distances one by one in a double variable needed for Distance.class.

                double xAndY2 = Double.parseDouble(distanceMatrix[i][j]);

                 if(xAndY2 == 1.0){
                    xAndY2 = 0;
                 }

                 //distancesAAA.add(xAndY2);

                System.out.println(xAndY2 + "\n");

              distances[j] = new Distance(xAndY2); //Creates a new object of a class containing x,y as parameters.
            }
        }
        System.out.println(" check this out " + distancesAAA.toString());


        /**
         * Stores 10 elements per list "elementDurationNo1" from duration matrix array.
         */
        for (int i = 0; i < durationMatrix.length; i++) {
            for (int j = 0; j < durationMatrix[i].length; j++) {
                durationMatrix[0][j] = placeDurations.subList(0, 10).get(j).replaceAll("[^0-9]","");
                durationMatrix[1][j] = placeDurations.subList(10, 20).get(j).replaceAll("[^0-9]","");
                durationMatrix[2][j] = placeDurations.subList(20, 30).get(j).replaceAll("[^0-9]","");
                durationMatrix[3][j] = placeDurations.subList(30, 40).get(j).replaceAll("[^0-9]","");
                durationMatrix[4][j] = placeDurations.subList(40, 50).get(j).replaceAll("[^0-9]","");
                durationMatrix[5][j] = placeDurations.subList(50, 60).get(j).replaceAll("[^0-9]","");
                durationMatrix[6][j] = placeDurations.subList(60, 70).get(j).replaceAll("[^0-9]","");
                durationMatrix[7][j] = placeDurations.subList(70, 80).get(j).replaceAll("[^0-9]","");
                durationMatrix[8][j] = placeDurations.subList(80, 90).get(j).replaceAll("[^0-9]","");
                durationMatrix[9][j] = placeDurations.subList(90, 100).get(j).replaceAll("[^0-9]","");

                double xAndY = Double.parseDouble(durationMatrix[i][j]);

                    durations[j] = new Duration(xAndY);   //Creates a new object of a class containing x,y as parameters.
            }
        }

        // Initial GA
        GeneticAlgorithm ga = new GeneticAlgorithm(100, 0.001, 0.9, 2, 5);

        // Initialize population
        Population population = ga.initPopulation(distances.length, durations.length);

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
            System.out.println("G"+generation+" Distance and duration: " + decimalFormat.format(route.getDistance()) + " km " + decimalFormat.format(route.getDuration()) + " min");

            // Apply crossover
            population = ga.crossoverPopulation(population);

            // Apply mutation
            population = ga.mutatePopulation(population);

            // Evaluate population
            ga.evalPopulation(population, distances, durations);

            // Increment the current generation
            generation++;
        }

        Route route = new Route(population.getFittest(0), distances,durations); //new route

        endTimeDistance = System.currentTimeMillis();  //stops the timer.

        durationMillis = endTimeDistance - startTimeDistance;  //calculates the time spent for processing.

        Log.d("Distance&Duration: ", "distance " + route.getDistance() + " km" + " and duration " + route.getDuration() + " min"
                                                            + " " + durationMillis + " ms" + " "
                + "Stopped after " + maxGenerations + " generations. " );

        updateTxt("Genetic Algorithm " + "\n" + decimalFormat.format(route.getDistance()) + " km " + "\n" +
                       decimalFormat.format(route.getDuration()) + " min " + "\n" +
                       durationMillis + " ms" ); //updates the textView field by adding total distance, duration and processing time.


        //System.out.print(getAllDistancesAndDurations());   //print out all the origins and destinations with their distances and duration. Each pair of origin and destination will have its corresponding time and distance.

    }

    //Updates the textView field.
    public void updateTxt(String data){
        textView.setText(data);
    }

    /**
     * Gets all the origins and destinations and attaches to
     * each pair their correct distance and time according to
     * the JSON file.
     * @return
     */
//    public String getAllDistancesAndDurations() {
//
//        String result = "";
//        List<String> fromTo = new ArrayList<>();
//
//        for (int i = 0; i < distanceMatrixDetailed.length; i++) {
//            for (int j = 0; j < distanceMatrixDetailed[i].length; j++) {
//                fromTo.add(originAndDestinationName(i,j) + distanceMatrixDetailed[i][j] +" "+ durationMatrixDetailed[i][j] + "\n");
//                result = fromTo.toString();
//            }
//        }
//            return result;
//    }

    /**
     * Returns a list of origins and destinations.
     * Later is used in the method above to match the distance and duration each pair has.
     * @param x
     * @param y
     * @return
     */
//    public String originAndDestinationName(int x, int y){
//
//          String origin = placesOrigins.get(x);
//          String destination = placesDestinations.get(y);
//
//          return "From " + origin + " To " + destination + " : ";
//    }

    /**
     * Gets a list of origin places only.
     * @return
     */
    public String getOrigins(){
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(nearbyPlaceList.get(i));
        }
        return list.toString().replace("{", "")
                .replace("}","")
                .replace("=", ":");
    }

    /**
     * Gets a list of destination places only.
     * @return
     */
    public String getDestinations(){
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            list.add(nearbyPlaceList.get(i));
        }
        return list.toString().replace("{", "")
                .replace("}","")
                .replace("=", ":");
    }

    /**
     * Method returns a link that is Distance matrix API.
     * @param origins
     * @param destinations
     * @return
     */
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
