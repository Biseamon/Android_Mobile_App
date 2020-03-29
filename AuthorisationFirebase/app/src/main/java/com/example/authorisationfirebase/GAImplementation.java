package com.example.authorisationfirebase;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GAImplementation extends AsyncTask<Object,String,String> {

    /**
     * GetNearbyPlace.class is a class that handles
     * operations with Places API.
     * Its mail purpose is to return a JSON file
     * containing data about the nearby places.
     */

    private String googlePlacesData;  //String to store the JSON file.
    private GoogleMap mMap;          //Google maps.
    private String url;             //nearby places API link.
    private LatLng currentLoc;
    private String currentLocation = "Current location";
    public static int maxGenerations = 100; //number of generations that genetic algorithm will perform.

    public GAImplementation(LatLng latLng){
        this.currentLoc = latLng;
    }

    public GAImplementation(){ }

    @Override
    protected String doInBackground(Object[] objects) {
        /**
         * doInBackground performs operations
         * based on the data trasfrred from the
         * MapsActivity.class.
         */

        mMap = (GoogleMap) objects[0]; //Google map object.
        url = (String) objects[1];    //Url object.
        currentLoc = (LatLng) objects[2];

        DownloadUrl downloadURL = new DownloadUrl();       //initialises the downloadUrl.class.
        try {
            googlePlacesData = downloadURL.readUrl(url); //gets the JSON data and store it in the googlePlacesData.
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;  //returns the JSON data.
    }

    @Override
    protected void onPostExecute(String s) {
        List<Map<String, String>> nearbyPlaceList; //a list with nearby places. Empty for now.
        Parser parser = new Parser();             // a new Parser.
        nearbyPlaceList = parser.parse(s);       //parses the places from the JSON file.

        List<LatLng> listOfCoordinates = new ArrayList<>();
        List<String> listOfNames = new ArrayList<>();
        listOfNames.add(0,currentLocation);

        Log.d("GANames", "onPostExecute: " + listOfNames.addAll(placeNames(nearbyPlaceList)));

        listOfCoordinates.add(0, currentLoc);
        listOfCoordinates.addAll(coordinates(nearbyPlaceList));

        Coordinates[] latlngs = new Coordinates[listOfCoordinates.size()]; //An array of distances.
        City[] latlngs1 = new City[listOfCoordinates.size()]; //An array of distances.

        MarkerOptions markerOptions = new MarkerOptions();

        for (int cityIndex = 0; cityIndex < listOfCoordinates.size(); cityIndex++) {
            for (int i = 0; i < listOfCoordinates.size(); i++) {
                // Generate x,y position
                LatLng xPos = listOfCoordinates.get(cityIndex);
                LatLng yPos = listOfCoordinates.get(i);

                // Add city
                latlngs1[cityIndex] = new City(xPos, yPos);
            }

            for (int i = 0; i < listOfCoordinates.size(); i++) {
                LatLng latLng = listOfCoordinates.get(i);
                latlngs[i] = new Coordinates(latLng);
            }

                mMap.addMarker(markerOptions.position(listOfCoordinates.get(0))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        }

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.RED);        //Sets the colour of the poly lines.
        polylineOptions.width(7);              //sets the width of the poly lines.
        polylineOptions.geodesic(true);        //boolean value (optional).
        polylineOptions.clickable(true);
        polylineOptions.addAll(listOfCoordinates);

        mMap.addPolyline(polylineOptions);

        Log.d("nearbyPlacesData", nearbyPlaceList.toString()); //test purpose only.
        Log.d("coordinates", "onPostExecute: " + listOfCoordinates.toString());

        //displays all the places on the map using showNearbyPlaces method down below.
        showNearbyPlaces(nearbyPlaceList);

        // Initial GA
        GeneticAlgorithm2 ga = new GeneticAlgorithm2(100, 0.001, 0.9, 2, 5);

        // Initialize population
        Population2 population = ga.initPopulation(latlngs.length);

        // Evaluate population
        ga.evalPopulation(population, latlngs1);

        Route2 startRoute = new Route2(population.getFittest(0), latlngs1);
        System.out.println("Start Distance: " + startRoute.getDistance());

        // Keep track of current generation
        int generation = 1;
        // Start evolution loop
        while (ga.isTerminationConditionMet(generation, maxGenerations) == false) {
            // Print fittest individual from population

            Route2 route = new Route2(population.getFittest(0), latlngs1);
            System.out.println("G"+generation+" Best distance: " + route.getDistance());

            // Apply crossover
            population = ga.crossoverPopulation(population);

            // Apply mutation
            population = ga.mutatePopulation(population);

            // Evaluate population
            ga.evalPopulation(population, latlngs1);

            // Increment the current generation
            generation++;
        }

        System.out.println("Stopped after " + maxGenerations + " generations.");
        Route2 route = new Route2(population.getFittest(0), latlngs1);
        System.out.println("Best distance: " + route.getDistance());

    }

    private void showNearbyPlaces(List<Map<String, String>> nearbyPlaceList) {

        for (int i = 0; i < nearbyPlaceList.size() - 10; i++) {
            MarkerOptions markerOptions = new MarkerOptions();          //initialise the MarkerOption.class.
            Map<String, String> googlePlace = nearbyPlaceList.get(i);  //loops through the list of parsed places from the onPostExecute method.

            String placeName = googlePlace.get("place_name");               //extracts the place_name.
            String vicinity = googlePlace.get("vicinity");                 //extracts the vicinity.
            double lat = Double.parseDouble(googlePlace.get("lat"));      //extracts the latitude.
            double lng = Double.parseDouble(googlePlace.get("lng"));     //extracts the longitude.
            String place_id = googlePlace.get("place_id");              //extracts the place_id.

            LatLng latLng = new LatLng(lat, lng);  //lat + lng.

            markerOptions.position(latLng);                                                                //sets the position of the marker.
            markerOptions.title(placeName + " " + latLng + place_id  + vicinity);                        //sets its title.
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)); // sets the icon.

            mMap.addMarker(markerOptions);                                //add the marker to the map.
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));      //focuses the camera on the last marker.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));      // zooms in on the marker.



            Log.d("nearbyPlacesList", googlePlace.toString().replace("{place_name=", "")); //Test/Check purpose only.

        }
    }

    private List<LatLng> coordinates(List<Map<String, String>> nearbyPlaceList) {

        List<LatLng> listOfLatLng = new ArrayList<>();
        Map<String, LatLng> placeNamesAndTheirCoordinates = new HashMap<>();

        for (int i = 0; i < nearbyPlaceList.size() - 10; i++) {
            Map<String, String> googlePlace = nearbyPlaceList.get(i);  //loops through the list of parsed places from the onPostExecute method.

            String placeName = googlePlace.get("place_name");
            double lat = Double.parseDouble(googlePlace.get("lat"));      //extracts the latitude.
            double lng = Double.parseDouble(googlePlace.get("lng"));     //extracts the longitude.

            LatLng latLng = new LatLng(lat, lng);  //lat + lng.

            listOfLatLng.add(latLng);

            placeNamesAndTheirCoordinates.put(placeName,latLng);

            Log.d("coordinates", googlePlace.toString().replace("{place_name=", "")); //Test/Check purpose only.
            Log.d("namesCoords", "names and coordinates " + listOfLatLng.toString());

        }
        return listOfLatLng;
    }

    private List<String> placeNames(List<Map<String, String>> nearbyPlaceList) {

        List<String> listOfNames = new ArrayList<>();

        for (int i = 0; i < nearbyPlaceList.size() - 10; i++) {
            Map<String, String> googlePlace = nearbyPlaceList.get(i);  //loops through the list of parsed places from the onPostExecute method.


            String placeName = googlePlace.get("place_name");        //extracts the place_name.

            listOfNames.add(placeName);

            Log.d("coordinates", googlePlace.toString().replace("{place_name=", "")); //Test/Check purpose only.

        }
        return listOfNames;
    }

}
