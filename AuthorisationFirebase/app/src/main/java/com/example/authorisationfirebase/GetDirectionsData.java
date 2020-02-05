package com.example.authorisationfirebase;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.LatLng;

import java.io.IOException;

public class GetDirectionsData extends AsyncTask<Object,String, String> {


    GoogleMap mMap;
    String url;
    String googleDirectionsData;
    String duration, distance;
    LatLng latLng;

    @Override
    protected String doInBackground(Object[] objects) {

        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];
        latLng = (LatLng) objects[2];


        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleDirectionsData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googleDirectionsData;
    }

    @Override
    protected void onPostExecute(String s) {
        String[] directionList;
        Parser parser = new Parser();
        directionList = parser.parseDirections(s);
        displayDirection(directionList);
    }

    private void displayDirection(String[] directionList) {

        int count = directionList.length;

        for (int i = 0; i < count; i++){
            PolylineOptions options = new PolylineOptions();
            options.color(Color.CYAN);
            options.width(10);
            options.addAll(PolyUtil.decode(directionList[i]));

            mMap.addPolyline(options);
        }

    }


}
