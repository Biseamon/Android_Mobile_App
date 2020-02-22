package com.example.authorisationfirebase;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetDistanceMatrixData extends AsyncTask<Object,String,String> {

    List<Map<String, String>> nearbyPlaceList;
    private String googlePlacesData;
    private GoogleMap mMap;
    private String url;
    private String distanceMatrixUrl;
    private String finalUrl;


    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];

        DownloadUrl downloadURL = new DownloadUrl();
        try {
            googlePlacesData = downloadURL.readUrl(url);
            Parser parser = new Parser();
            nearbyPlaceList = parser.parseTest(googlePlacesData);

            String origins = nearbyPlaceList.get(0).toString()
                         .replace("{", "")
                         .replace("}","")
                         .replace("=", ":");


            distanceMatrixUrl = getDistanceMatrix(origins, getNearbyPlaceId(nearbyPlaceList)
                    .replace("{", "")
                    .replace("}","")
                    .replace("=", ":")
                    .replaceFirst("place_id:ChIJRyztkXO8cEgRaO_-EaZwzm0", ""));

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

        return distanceMatrixUrl;
    }

    @Override
    protected void onPostExecute(String s) {

        Log.i("testi", "onPostExecute: " + s);
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
        stringBuilder.append("&mode=driving");
        stringBuilder.append("&key=" + "AIzaSyCuRGuOxVFfA2rs5gT-w2Y8K_RSlgzualg");

        return stringBuilder.toString().replace("[","")
                .replace("]","")
                .replace(",","|")
                .replace(" ","");
    }

}
