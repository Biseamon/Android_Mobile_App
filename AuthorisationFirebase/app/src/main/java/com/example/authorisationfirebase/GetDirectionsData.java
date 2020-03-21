package com.example.authorisationfirebase;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetDirectionsData extends AsyncTask<Object, String, String> {

    /**
     * GetDirectionData.class is responsible of creating a directions API link
     * that provides data needed for processing. The data is represented as poly lines on the map.
     * The data used for Directions API is borrowed from the GetNearbyPlaces, which uses Places API.
     */

    private GoogleMap mMap;                     //Google Map
    private String nearbyPlacesUrl;            // an API link
    private String finalUrl;                  //the API link that will be returned from doInBackground method
    private String googleDirectionsData;     //String containing
    private String distanceUrl;             //Distance API link
    @SuppressLint("StaticFieldLeak")       //Annotation that prevents memory leaks caused by TextView within a class and not an Activity.
    private TextView textView;
    private LatLng currentLoc;

    GetDirectionsData(TextView txtView){
        this.textView = txtView;  //constructor
    }

    @Override
    protected String doInBackground(Object... objects) {

        /**
         * doInBackground is an Android method that allows certain processes
         * to be processed in the background avoiding the contact with the UI thread.
         *
         */

        mMap = (GoogleMap) objects[0];          //here the map is loaded in the background after the request from the MapsActivity.class was made.
        nearbyPlacesUrl = (String) objects[1]; //here the nearbyPlacesUrl that returns nearby places is used in order extract from it data needed for Directions API.
        currentLoc = (LatLng) objects[2];

        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleDirectionsData = downloadUrl.readUrl(nearbyPlacesUrl); // reads the JSON file after accessing the API link.

            Parser parser = new Parser();  //Initiate the parser class needed to parse data fro the API link.

            List<Map<String, String>> listOfWaypoints;  //List of maps needed to store the data from the parsed API link.

            listOfWaypoints = parser.parseDirectionData(googleDirectionsData);  //parses the data from the Direction API link.

            distanceUrl = getDistanceUrl(currentLoc.toString().replace("lat/lng:","").replace("(","").replace(")","").replace(" ","")
                    ,currentLoc.toString().replace("lat/lng:","").replace("(","").replace(")","").replace(" ",""),getPlacesId(listOfWaypoints));  //Finally formed the Directions API link.

            DownloadUrl downloadUrl1 = new DownloadUrl();
            finalUrl = "";
            try {
                finalUrl = downloadUrl1.readUrl(distanceUrl);  // Downloads the JSON file from the link created earlier(distanceUrl).
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.i("directionAPIResult", "" + finalUrl); //For test/check purpose.

        } catch (IOException | RuntimeException e) {

            Log.d("errorDirectionApi", "doInBackground: " + e);
            e.printStackTrace();

        }

        return finalUrl; //returns the data needed for the onPostExecute method.
    }

    @Override
    protected void onPostExecute(String s) {

        parseDirection(s); //returns the shortest route between nearby places. It uses Directions API.

        Log.i("parseDirections", "onPostExecute: " + s);  // For test/check purpose only.
    }

    /**
     *A method that returns data from a API call such as starting points, distances
     * and durations and display them on
     * the map as poly lines.
     */

    public void parseDirection(String s){
        PolylineOptions polylineOptions = new PolylineOptions(); //initialise poly lines
        List<LatLng> movements = new ArrayList<>();             // List of poly lines

        DecimalFormat decimalFormat = new DecimalFormat("#.##"); //changes a double number format.

        double start_lat,start_lng; //location coordinates

        double distance;  //stores the distance.
        double duration; //stores the duration.

        List<Double> durationList = new ArrayList<>();   //a list of distances.
        List<Double> distanceList = new ArrayList<>();  //a list of durations.

        String distanceAndDuration;  //String that contains information that will be displayed on the screen.

        LatLng start_latLng;  //contains coordinates start_lat and start_lng.

        List<LatLng> start_LatLng_list = new ArrayList<>();  //a list made of start_latLng.

        String point; //poly line point

        long startTimeDistance;  //To check when a process started.
        long endTimeDistance;   //To check when a process finished.
        long durationMillis;   //To show the time taken to perform something.

        try {
            JSONObject json = new JSONObject(s);                       // Initialise a JSON object.
            JSONArray jsonRoute = json.getJSONArray("routes");  //Checks for an Array called routes.

            int count = jsonRoute.length();   //checks the "routes" length.

            startTimeDistance = System.currentTimeMillis(); //Timer starts here.
            for (int i = 0; i < count; i++) {
                //Iterates through all the objects inside the routes array.
                JSONObject jsonObject = jsonRoute.getJSONObject(i);
                JSONArray jsonArray = jsonObject.getJSONArray("legs"); //Check for nested arrays called "legs".
                int count1 = jsonArray.length();                            //checks the "legs" length.

                for (int j = 0; j < count1; j++) {
                    //Iterates through all the objects inside the "legs".
                    JSONObject jsonObject1 =jsonArray.getJSONObject(j);
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("steps"); //Checks for nested arrays called "steps".
                    int count2 = jsonArray1.length();                              //checks the "steps" length.

                    for (int k = 0; k < count2; k++) {
                        //Iterates through the objects in the "steps" array.
                        JSONObject jsonObject2 = jsonArray1.getJSONObject(k);
                        point = jsonObject2.getJSONObject("polyline").getString("points");  //Checks for points string that is responsible for creating poly lines.

                        start_lat = Double.parseDouble(jsonArray1.getJSONObject(i)
                                .getJSONObject("start_location").getString("lat"));  //parser to double values of the latitude string.
                        start_lng = Double.parseDouble(jsonArray1.getJSONObject(i)
                                .getJSONObject("start_location").getString("lng")); //parser to double values of the longitude string.

                        start_latLng = new LatLng(start_lat, start_lng); //initialises the LatLng from the method's field.
                        start_LatLng_list.add(start_latLng);            //Adds the initialised LatLng to the LatLng list from the method's field.

                        duration = Double.parseDouble(jsonObject2.getJSONObject("duration").getString("value")); //gets the duration.
                        distance = Double.parseDouble(jsonObject2.getJSONObject("distance").getString("value")); //gets the distance.
                        durationList.add(duration);  //adds the duration to the list created in the method's field.
                        distanceList.add(distance); //adds the distance to the list created in the method's field.

                        movements.addAll(PolyUtil.decode(point)); // adds the poly lines codes to a list created in the method's field.
                    }
                }
            }

            polylineOptions.color(Color.RED);        //Sets the colour of the poly lines.
            polylineOptions.width(10);              //sets the width of the poly lines.
            polylineOptions.geodesic(true);        //boolean value (optional).
            polylineOptions.clickable(true);      //makes the polyline clickable.
            polylineOptions.addAll(movements);   //creates the poly lines.

            mMap.clear();                       //clears the map
            mMap.addPolyline(polylineOptions); //adds all the poly lines to the map.

            /**
             * Loops through the list of LatLng in order to display markers on the map.
             */
            for (int j = 0; j < start_LatLng_list.size(); j++) {
                LatLng latLng1 = start_LatLng_list.get(j);
                String name = start_LatLng_list.get(j).toString();
                mMap.addMarker(new MarkerOptions().position(latLng1).title("Start_location " + name) //title for markers.
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));    //sets the icon of the marker.
            }

            mMap.addMarker(new MarkerOptions().position(start_LatLng_list.get(0)).title("Current location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

            double distanceSum = 0;
            double durationSum = 0;

            //Loop for calculating the total distance.
            for (int i = 0; i < distanceList.size(); i++) {
                distanceSum = distanceSum + (distanceList.get(i))/1000; //to km.
            }

            //Loop for calculating the total duration.
            for (int i = 0; i < durationList.size(); i++) {
                durationSum = durationSum + (durationList.get(i)%3600)/60; //to minutes.
            }

            endTimeDistance = System.currentTimeMillis(); //Timer stops here.

            durationMillis = endTimeDistance - startTimeDistance;  //Calculates the time spent on processing the operations.

            Log.i("displayTimeAndDistance", "" + decimalFormat.format(distanceSum) + " km "  + "\n" + decimalFormat.format(durationSum) + " min " + "\n" + durationMillis + " ms");
            distanceAndDuration = "Direction API" + "\n" +decimalFormat.format(distanceSum) +" km "  + "\n" + decimalFormat.format(durationSum) +
                    " min " + "\n"  + durationMillis + " ms";

            updateTxt(distanceAndDuration); //updates the textView field by adding total distance, duration and processing time.

        } catch (JSONException e) {

            Log.d("errorParserDirection", "" + e);  //To check why error.

        }

    }

    //Updates the textView field.
    public void updateTxt(String data){
        textView.setText(data);
    }

    /**
     *
     * @param origin
     * @param destination
     * @param wayPoints
     * @return
     *
     * This method creates an API link for getting distances and duration between up to 20 places.
     * At the moment the limit is set to 10 places, however that to up to 20.
     * It requires an origin place, destination place and waypoints parameters.
     */
    public String getDistanceUrl(String origin, String destination, String wayPoints){

        String alt = "true";

        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        stringBuilder.append("origin=" + origin);                           //origin place parameter.
        stringBuilder.append("&destination=" + destination);               //destination parameter.
        stringBuilder.append("&alternatives=" + alt);                     //searches for alternative routes in case of traffic.
        stringBuilder.append("&mode=driving");                           //modes can be: walking, cycling, driving...
        stringBuilder.append("&waypoints=optimize:true|" + wayPoints);  //this allows google direction API to find the shortest way using efficient algorithm.
        stringBuilder.append("&key=" + "AIzaSyCuRGuOxVFfA2rs5gT-w2Y8K_RSlgzualg"); //API_key provided by Google is needed in order to make the project working.

        return stringBuilder.toString(); //returns the Direction API link that later will be parsed.
    }

    /**
     * This method returns the list of places.
     * There is a limit set to 10. The maximum amount of places is set by default to 20.
     * -10 in the for loop reduces the length of the list by 10.
     *
     * As parameter it uses a list of maps that is initialised and populated in the doInBackground method of this class.
     *
     * @param placesIds
     * @return
     */
    public String getPlacesId(List<Map<String, String>> placesIds){

        List<Map<String,String>> placesIdList = new ArrayList<>();

        for (int i = 0; i < placesIds.size() - 10; i++) {

            placesIdList.add(placesIds.get(i));

            Log.i("listOfPlacesById", placesIdList.toString() //To check in the log if the method returns the right amount of places.
                    .replace("=",":")
                    .replace("{","")
                    .replace("}","")+ "|");
        }

        return placesIdList.toString().replace("[", "") //returns the list of places. Also regex is used to remove brackets and spaces from the map.
                .replace("]", "")
                .replace("{", "")
                .replace("}", "")
                .replace("=", ":")
                .replace(" ", "")
                .replace(",", "|");
    }
}




