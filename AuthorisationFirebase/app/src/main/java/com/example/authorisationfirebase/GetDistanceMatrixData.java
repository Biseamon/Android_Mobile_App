package com.example.authorisationfirebase;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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

    private String googlePlacesData;      //this variable will contain the distance matrix api data.
    private GoogleMap mMap;              //Google maps.
    private String nearbyPlacesUrl;     //nearby Places API.
    public String distanceMatrixUrl;   //distance matrix API link.
    private String finalUrl;          //final end API link sent to onPostExecute method.


    public GetDistanceMatrixData(TextView txtView) {
        this.textView = txtView;
    }  //textView constructor.

    @Override
    protected String doInBackground(Object... objects) {

        /**
         * All the background work is done here. Here is happening the
         * download and parse process.
         */

        mMap = (GoogleMap) objects[0];           //map initialisation from the MapsActivity.class
        nearbyPlacesUrl = (String) objects[1];  //link to Places API from MapsActivity.class

        DownloadUrl downloadURL = new DownloadUrl();  //initialising the downloadUrl.class
        try {
            googlePlacesData = downloadURL.readUrl(nearbyPlacesUrl);          //reading and storing the json file from the link into googlePlaceData.
            Parser parser = new Parser();                                    //initialise a new parser.
            nearbyPlaceList = parser.parseDirectionData(googlePlacesData);  //parses the data from the JSON file into nearbyPlaceList.

           // String origins = getOrigins(); //stores all the origin places.

            String destinations = getDestinations();  //stores all the destination places.

            distanceMatrixUrl = getDistanceMatrix(getOrigins(), getDestinations());            //distance matrix API link.
            Log.d("distMatrix", "doInBackground: " + distanceMatrixUrl);  //Test/check the result.

            DownloadUrl downloadUrl1 = new DownloadUrl(); //initialise a new downloadUrl object.

            finalUrl = "";  //final created url.
            try {
                finalUrl = downloadUrl1.readUrl(distanceMatrixUrl); //reads the distance matrix API link.
            } catch (IOException e) {
                e.printStackTrace();
            }

           // Log.d("distanceMatrix", "doInBackground: " + distanceMatrixUrl);  //Test/checks if link returns anything.

        } catch (IOException e) {
            e.printStackTrace();
        }

        return finalUrl; //returns a distance matrix link.
    }

    /**
     * persorms actions after the doInBackground has finished its work.
     *
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


//        System.out.println(placeDistances.toString());
//
//        System.out.println(placeDurations.toString());


        String text = getOrigins();
        List<String> list = new ArrayList<>();

        StringTokenizer tokenizer = new StringTokenizer(text, "|");

        while (tokenizer.hasMoreTokens()) {
            list.add(tokenizer.nextToken());
            //System.out.println(tokenizer.nextToken().replace("[","").replace("]",""));
        }

        System.out.println("test2 " + list.toString());


        String text1 = list.toString()
                        .replace("[","")
                        .replace("]","");

        List<Double> doubles = new ArrayList<>();

        StringTokenizer stringTokenizer = new StringTokenizer(text1, ",");

        while (stringTokenizer.hasMoreTokens()){
            doubles.add(Double.parseDouble(stringTokenizer.nextToken()));
        }

        List<LatLng> listOfLats = new ArrayList<>();
        double validSize = doubles.size() & ~1;

        for (int i = 0; i < validSize; i += 2) {
            double lat = doubles.get(i);
            double lng = doubles.get(i + 1);
            LatLng latLng = new LatLng(lat,lng);
            listOfLats.add(latLng);
        }

        System.out.println(listOfLats.toString());
        System.out.println(doubles.get(0));

        MarkerOptions markerOptions = new MarkerOptions();
        PolylineOptions polylineOptions = new PolylineOptions();
        mMap.clear();

        TSP tsp = new TSP();
        tsp.main(listOfLats,placesOrigins, placeDistances, placeDurations);

        updateTxt(tsp.getDetailsGA());

        for (int i = 0; i < tsp.getRouteLatLng().size(); i++) {
            mMap.addMarker(markerOptions.position(tsp.getRouteLatLng().get(i)).title("test" + i));
            polylineOptions.addAll(tsp.getRouteLatLng());
            mMap.addPolyline(polylineOptions);
        }

        //Log.d("origins", "onPostExecute: " + placesOrigins.toString());

        Log.d("origins", "onPostExecute: " + getOrigins());
        Log.d("routes", "onPostExecute: " + tsp.getRoute());

    }

    //Updates the textView field.
    public void updateTxt(String data){
        textView.setText(data);
    }

    /**
     * Gets a list of origin places only.
     * @return
     */
    public String getOrigins(){
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add(nearbyPlaceList.get(i).toString());

        }

        return list.toString().replace("{","")
                .replace("}","")
                .replace(",","|")
                .replace("!",",")
                .replace(" ","")
                .replace("latLngs=","");
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
        return list.toString().replace("[", "")
                .replace("]", "")
                .replace("{", "")
                .replace("}","")
                .replace("=", ":")
                .replace(" ", "")
                .replace(",", "|")
                .replace("!", ",")
                .replace("latLngs:", "");
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
               // .replace(",","|")
                .replace(" ","");
    }
}
