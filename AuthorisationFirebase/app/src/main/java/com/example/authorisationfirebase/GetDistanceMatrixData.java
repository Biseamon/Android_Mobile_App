package com.example.authorisationfirebase;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
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

    private List<String> elementDistanceNo1 = new ArrayList<>();  //1st list of elements from the "rows" in the distance matrix API.
    private List<String> elementDistanceNo2 = new ArrayList<>();  //2nd list of elements from the "rows" in the distance matrix API.
    private List<String> elementDistanceNo3 = new ArrayList<>();  //3rd list of elements from the "rows" in the distance matrix API.
    private List<String> elementDistanceNo4 = new ArrayList<>();  //4th list of elements from the "rows" in the distance matrix API.
    private List<String> elementDistanceNo5 = new ArrayList<>();  //5th list of elements from the "rows" in the distance matrix API.
    private List<String> elementDistanceNo6 = new ArrayList<>();  //6th list of elements from the "rows" in the distance matrix API.
    private List<String> elementDistanceNo7 = new ArrayList<>();  //7th list of elements from the "rows" in the distance matrix API.
    private List<String> elementDistanceNo8 = new ArrayList<>();  //8th list of elements from the "rows" in the distance matrix API.
    private List<String> elementDistanceNo9 = new ArrayList<>();  //9th list of elements from the "rows" in the distance matrix API.
    private List<String> elementDistanceNo10 = new ArrayList<>(); //10th list of elements from the "rows" in the distance matrix API.

    private List<String> elementDuration1 = new ArrayList<>();  //1st list of elements from the "rows" in the distance matrix API.
    private List<String> elementDuration2 = new ArrayList<>();  //2nd list of elements from the "rows" in the distance matrix API.
    private List<String> elementDuration3 = new ArrayList<>();  //3rd list of elements from the "rows" in the distance matrix API.
    private List<String> elementDuration4 = new ArrayList<>();  //4th list of elements from the "rows" in the distance matrix API.
    private List<String> elementDuration5 = new ArrayList<>();  //5th list of elements from the "rows" in the distance matrix API.
    private List<String> elementDuration6 = new ArrayList<>();  //6th list of elements from the "rows" in the distance matrix API.
    private List<String> elementDuration7 = new ArrayList<>();  //7th list of elements from the "rows" in the distance matrix API.
    private List<String> elementDuration8 = new ArrayList<>();  //8th list of elements from the "rows" in the distance matrix API.
    private List<String> elementDuration9 = new ArrayList<>();  //9th list of elements from the "rows" in the distance matrix API.
    private List<String> elementDuration10 = new ArrayList<>(); //10th list of elements from the "rows" in the distance matrix API.

    private String googlePlacesData;      //this variable will contain the distance matrix api data.
    private GoogleMap mMap;              //Google maps.
    private String nearbyPlacesUrl;     //nearby Places API.
    public String distanceMatrixUrl;   //distance matrix API link.
    private String finalUrl;          //final end API link sent to onPostExecute method.

    private long startTimeDistance;     //Timer starter.
    private long endTimeDistance;      //Stops the times.
    private long durationMillis;      //calculates the duration between start and finish of the timer.

    public static int maxGenerations = 100; //number of generations that genetic algorithm will perform.

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

        System.out.println("Hello world" + map2.toString());

        Log.d("distances", "onPostExecute: " + placeDistances.toString());

        elementDistanceNo1 = placeDistances.subList(0, 10);   //element of distances contains a sublist of placeDistances.
        elementDistanceNo2 = placeDistances.subList(10, 20);  //element of distances contains a sublist of placeDistances.
        elementDistanceNo3 = placeDistances.subList(20, 30);  //element of distances contains a sublist of placeDistances.
        elementDistanceNo4 = placeDistances.subList(30, 40);  //element of distances contains a sublist of placeDistances.
        elementDistanceNo5 = placeDistances.subList(40, 50);  //element of distances contains a sublist of placeDistances.
        elementDistanceNo6 = placeDistances.subList(50, 60);  //element of distances contains a sublist of placeDistances.
        elementDistanceNo7 = placeDistances.subList(60, 70);  //element of distances contains a sublist of placeDistances.
        elementDistanceNo8 = placeDistances.subList(70, 80);  //element of distances contains a sublist of placeDistances.
        elementDistanceNo9 = placeDistances.subList(80, 90);  //element of distances contains a sublist of placeDistances.
        elementDistanceNo10 = placeDistances.subList(90,100); //element of distances contains a sublist of placeDistances.

        elementDuration1 = placeDurations.subList(0, 10);  //element of distances contains a sublist of placeDurations.
        elementDuration2 = placeDurations.subList(10, 20); //element of distances contains a sublist of placeDurations.
        elementDuration3 = placeDurations.subList(20, 30); //element of distances contains a sublist of placeDurations.
        elementDuration4 = placeDurations.subList(30, 40); //element of distances contains a sublist of placeDurations.
        elementDuration5 = placeDurations.subList(40, 50); //element of distances contains a sublist of placeDurations.
        elementDuration6 = placeDurations.subList(50, 60); //element of distances contains a sublist of placeDurations.
        elementDuration7 = placeDurations.subList(60, 70); //element of distances contains a sublist of placeDurations.
        elementDuration8 = placeDurations.subList(70, 80); //element of distances contains a sublist of placeDurations.
        elementDuration9 = placeDurations.subList(80, 90); //element of distances contains a sublist of placeDurations.
        elementDuration10 = placeDurations.subList(90,100); //element of distances contains a sublist of placeDurations.

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
                distanceMatrixDetailed[0][j] = elementDistanceNo1.get(j);    //first 10 elements stored.
                distanceMatrixDetailed[1][j] = elementDistanceNo2.get(j);   //second dozen of elements stored.
                distanceMatrixDetailed[2][j] = elementDistanceNo3.get(j);  //third dozen of elements stored.
                distanceMatrixDetailed[3][j] = elementDistanceNo4.get(j); //and so on...
                distanceMatrixDetailed[4][j] = elementDistanceNo5.get(j);
                distanceMatrixDetailed[5][j] = elementDistanceNo6.get(j);
                distanceMatrixDetailed[6][j] = elementDistanceNo7.get(j);
                distanceMatrixDetailed[7][j] = elementDistanceNo8.get(j);
                distanceMatrixDetailed[8][j] = elementDistanceNo9.get(j);
                distanceMatrixDetailed[9][j] = elementDistanceNo10.get(j);
            }
        }

        /**
         * the loop goes through a detailed duration matrix and populates
         * the element list with data. 10 per element.
         * There are 100 elements in the detailed duration matrix, and every 10 are stored in the elementDurationNo# list.
         */
        for (int i = 0; i < durationMatrixDetailed.length; i++) {
            for (int j = 0; j < durationMatrixDetailed[i].length; j++) {
                durationMatrixDetailed[0][j] = elementDuration1.get(j);     //first 10 elements stored.
                durationMatrixDetailed[1][j] = elementDuration2.get(j);    //second dozen of elements stored.
                durationMatrixDetailed[2][j] = elementDuration3.get(j);   //third dozen of elements stored.
                durationMatrixDetailed[3][j] = elementDuration4.get(j);  //and so on...
                durationMatrixDetailed[4][j] = elementDuration5.get(j);
                durationMatrixDetailed[5][j] = elementDuration6.get(j);
                durationMatrixDetailed[6][j] = elementDuration7.get(j);
                durationMatrixDetailed[7][j] = elementDuration8.get(j);
                durationMatrixDetailed[8][j] = elementDuration9.get(j);
                durationMatrixDetailed[9][j] = elementDuration10.get(j);
            }
        }

        Duration[] durations = new Duration[placesOrigins.size()];  //An array of durations.
        Distance[] distances = new Distance[placesOrigins.size()]; //An array of distances.

        startTimeDistance = System.currentTimeMillis(); //starts the timer.

        /**
         * Stores 10 elements per list "elementDistanceNo1" from distance matrix array.
         */
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                distanceMatrix[0][j] = elementDistanceNo1.get(j).replace("m","")
                        .replace("k", "")    //first 10 of j elements stored.
                        .replace(" " , "");
                distanceMatrix[1][j] = elementDistanceNo2.get(j).replace("m","")
                        .replace("k", "")
                        .replace(" " , "");
                distanceMatrix[2][j] = elementDistanceNo3.get(j).replace("m","")
                        .replace("k", "")
                        .replace(" " , "");
                distanceMatrix[3][j] = elementDistanceNo4.get(j).replace("m","")
                        .replace("k", "")
                        .replace(" " , "");
                distanceMatrix[4][j] = elementDistanceNo5.get(j).replace("m","")
                        .replace("k", "")
                        .replace(" " , "");
                distanceMatrix[5][j] = elementDistanceNo6.get(j).replace("m","")
                        .replace("k", "")
                        .replace(" " , "");
                distanceMatrix[6][j] = elementDistanceNo7.get(j).replace("m","")
                        .replace("k", "")
                        .replace(" " , "");
                distanceMatrix[7][j] = elementDistanceNo8.get(j).replace("m","")
                        .replace("k", "")
                        .replace(" " , "");
                distanceMatrix[8][j] = elementDistanceNo9.get(j).replace("m","")
                        .replace("k", "")
                        .replace(" " , "");
                distanceMatrix[9][j] = elementDistanceNo10.get(j).replace("m","")
                        .replace("k", "")
                        .replace(" " , "");

                double x = Double.parseDouble(distanceMatrix[i][0]);   //Stores the distances one by one in a double variable needed for Distance.class.
                double y = Double.parseDouble(distanceMatrix[0][j]);  //Stores the distances one by one in a double variable needed for Distance.class.

                double xAndY = Double.parseDouble(distanceMatrix[i][j]);

              distances[i] = new Distance(xAndY); //Creates a new object of a class containing x,y as parameters.
            }
        }

        /**
         * Stores 10 elements per list "elementDurationNo1" from duration matrix array.
         */
        for (int i = 0; i < durationMatrix.length; i++) {
            for (int j = 0; j < durationMatrix[i].length; j++) {
                durationMatrix[0][j] = elementDuration1.get(j).replace("min","")
                        .replace("s", "")  //first 10 of j elements stored.
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

                double x = Double.parseDouble(durationMatrix[i][0]);   //Stores the durations one by one in a double variable needed for Duration.class.
                double y = Double.parseDouble(durationMatrix[0][j]);  //Stores the durations one by one in a double variable needed for Duration.class.

                double xAndY = Double.parseDouble(durationMatrix[i][j]);

                    durations[i] = new Duration(xAndY);   //Creates a new object of a class containing x,y as parameters.
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
    public String getAllDistancesAndDurations() {

        String result = "";
        List<String> fromTo = new ArrayList<>();

        for (int i = 0; i < distanceMatrixDetailed.length; i++) {
            for (int j = 0; j < distanceMatrixDetailed[i].length; j++) {
                fromTo.add(originAndDestinationName(i,j) + distanceMatrixDetailed[i][j] +" "+ durationMatrixDetailed[i][j] + "\n");
                result = fromTo.toString();
            }
        }
            return result;
    }

    /**
     * Returns a list of origins and destinations.
     * Later is used in the method above to match the distance and duration each pair has.
     * @param x
     * @param y
     * @return
     */
    public String originAndDestinationName(int x, int y){

          String origin = placesOrigins.get(x);
          String destination = placesDestinations.get(y);

          return "From " + origin + " To " + destination + " : ";
    }

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
