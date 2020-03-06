package com.example.authorisationfirebase;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Map;

class GetNearbyPlaces extends AsyncTask<Object, String, String> {

    /**
     * GetNearbyPlace.class is a class that handles
     * operations with Places API.
     * Its mail purpose is to return a JSON file
     * containing data about the nearby places.
     */

    private String googlePlacesData;  //String to store the JSON file.
    private GoogleMap mMap;          //Google maps.
    private String url;             //nearby places API link.


    @Override
    protected String doInBackground(Object... objects) {

        /**
         * doInBackground performs operations
         * based on the data trasfrred from the
         * MapsActivity.class.
         */

        mMap = (GoogleMap) objects[0]; //Google map object.
        url = (String) objects[1];    //Url object.

        DownloadUrl downloadURL = new DownloadUrl();       //initialises the downloadUrl.class.
        try {
            googlePlacesData = downloadURL.readUrl(url); //gets the JSON data and store it in the googlePlacesData.
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;  //returns the JSON data.
    }

    /**
     * onPostExecute takes as parameter the return value of
     * the doInBackground method. Therefore, the JSON file is
     * the input for s. s == googlePlaceData.
     * @param s
     */
    @Override
    protected void onPostExecute(String s) {

        List<Map<String, String>> nearbyPlaceList; //a list with nearby places. Empty for now.
        Parser parser = new Parser();             // a new Parser.
        nearbyPlaceList = parser.parse(s);       //parses the places from the JSON file.

        Log.d("nearbyPlacesData", nearbyPlaceList.toString()); //test purpose only.

        showNearbyPlaces(nearbyPlaceList); //displays all the places on the map using showNearbyPlaces method down below.
    }

    /**
     * This method shows the nearby places as markers on the map.
     * The markers can have different titles containing different
     * information. For instance: name of the place, place_id, LatLng...
     * @param nearbyPlaceList
     */
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
            markerOptions.title(placeName + " : " + place_id + latLng + vicinity);                        //sets its title.
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)); // sets the icon.

            mMap.addMarker(markerOptions);                                //add the marker to the map.
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));      //focuses the camera on the last marker.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));      // zooms in on the marker.

            Log.d("nearbyPlacesList", googlePlace.toString().replace("{place_name=", "")); //Test/Check purpose only.

        }
    }

}