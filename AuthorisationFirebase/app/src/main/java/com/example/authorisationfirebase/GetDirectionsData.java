package com.example.authorisationfirebase;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetDirectionsData extends AsyncTask<Object, String, String> {

    GoogleMap mMap;
    String url;
    String url1;
    String googleDirectionsData;
    String distUrl;
    List<Map<String, String>> list;
    TextView textView;

    GetDirectionsData(TextView txtView){
        this.textView = txtView;
    }
    GetDirectionsData(){ }

    @Override
    protected String doInBackground(Object... objects) {

        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleDirectionsData = downloadUrl.readUrl(url);
            Parser parser = new Parser();

            List<Map<String, String>> trueUrl;

            trueUrl = parser.parseTest(googleDirectionsData);

            String origin = trueUrl.get(0).toString()
                    .replace("{", "")
                    .replace("}","")
                    .replace("=", ":");

            String destination = trueUrl.get(0).toString()
                    .replace("{", "")
                    .replace("}","")
                    .replace("=", ":");

            distUrl = getDistanceUrl(origin,destination,getPlacesId(trueUrl));

            Parser parser1 = new Parser();
            DownloadUrl downloadUrl1 = new DownloadUrl();
            String distObj = distUrl;
            url1 = "";
            try {
                url1 = downloadUrl1.readUrl(distObj);
            } catch (IOException e) {
                e.printStackTrace();
            }

            list = parser1.parseTesting(url1);

            Log.i("places", "places: " + url);
            Log.i("outerUrl", "onClick: " + distUrl + " " + trueUrl.get(0).toString());
        } catch (IOException | RuntimeException e) {
            Log.d("error", "doInBackground: " + e);
            e.printStackTrace();
        }

        return url1;
    }

    @Override
    protected void onPostExecute(String s) {

        parseDirection(s);

        Log.i("test", "onPostExecute: " + s);
    }

    public void parseDirection(String s){
        PolylineOptions polylineOptions = new PolylineOptions();
        List<LatLng> movements = new ArrayList<>();

        Double end_lat,end_lng;
        Double start_lat,start_lng;

        int distance;
        int duration;

        List<Integer> durationList = new ArrayList<>();
        List<Integer> distanceList = new ArrayList<>();
        String distanceAndDuration;

        LatLng end_latLng,start_latLng;

        List<LatLng> end_LatLng_list = new ArrayList<>();
        List<LatLng> start_LatLng_list = new ArrayList<>();

        String point;

        long startTimeDistance;
        long endTimeDistance;
        long durationMillis;

        try {
            JSONObject json = new JSONObject(s);
            JSONArray jsonRoute = json.getJSONArray("routes");

            int count = jsonRoute.length();

            startTimeDistance = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {

                JSONObject jsonObject = jsonRoute.getJSONObject(i);
                JSONArray jsonArray = jsonObject.getJSONArray("legs");
                int count1 = jsonArray.length();

                for (int j = 0; j < count1; j++) {

                    JSONObject jsonObject1 =jsonArray.getJSONObject(j);
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("steps");
                    int count2 = jsonArray1.length();

                    for (int k = 0; k < count2; k++) {
                        JSONObject jsonObject2 = jsonArray1.getJSONObject(k);
                        point = jsonObject2.getJSONObject("polyline").getString("points");

                        end_lat = Double.parseDouble(jsonArray1.getJSONObject(i).getJSONObject("end_location").getString("lat"));
                        end_lng = Double.parseDouble(jsonArray1.getJSONObject(i).optJSONObject("end_location").getString("lng"));
                        start_lat = Double.parseDouble(jsonArray1.getJSONObject(i).getJSONObject("start_location").getString("lat"));
                        start_lng = Double.parseDouble(jsonArray1.getJSONObject(i).getJSONObject("start_location").getString("lng"));

                        end_latLng = new LatLng(end_lat, end_lng);
                        start_latLng = new LatLng(start_lat, start_lng);
                        end_LatLng_list.add(end_latLng);
                        start_LatLng_list.add(start_latLng);

                        duration = Integer.parseInt(jsonObject2.getJSONObject("duration").getString("value"));
                        distance = Integer.parseInt(jsonObject2.getJSONObject("distance").getString("value"));
                        durationList.add(duration);
                        distanceList.add(distance);

                        movements.addAll(PolyUtil.decode(point));
                    }
                }
            }

            polylineOptions.color(Color.RED);
            polylineOptions.width(10);
            polylineOptions.geodesic(true);
            polylineOptions.clickable(true);
            polylineOptions.addAll(movements);

            mMap.clear();
            mMap.addPolyline(polylineOptions);


//            for (int i = 0; i < end_LatLng_list.size(); i++) {
//                LatLng latLng = end_LatLng_list.get(i);
//                String name = end_LatLng_list.get(i).toString();
//                mMap.addMarker(new MarkerOptions().position(latLng).title("End_location " + name)
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
//            }

            for (int j = 0; j < start_LatLng_list.size(); j++) {
                LatLng latLng1 = start_LatLng_list.get(j);
                String name = start_LatLng_list.get(j).toString();
                mMap.addMarker(new MarkerOptions().position(latLng1).title("Start_location " + name)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            }

            double distanceSum = 0;
            double durationSum = 0;


            for (int i = 0; i < distanceList.size(); i++) {
                distanceSum = distanceSum + (distanceList.get(i))/1000;

            }

            for (int i = 0; i < durationList.size(); i++) {
                durationSum = durationSum + (durationList.get(i)%3600)/60;
            }
            endTimeDistance = System.currentTimeMillis();

            durationMillis = endTimeDistance - startTimeDistance;

            Log.i("parser", "parseDirect: " + distanceSum + " km "  + "\n" + durationSum + " min " + "\n" + durationMillis + " ms");
            distanceAndDuration = distanceSum +" km " + "\n" + durationSum + " min " + "\n"  + durationMillis + " ms";
            updateTxt(distanceAndDuration);

        } catch (JSONException e) {
            Log.d("error1", "prs: " + e);
        }

    }

    public void updateTxt(String data){
        textView.setText(data);
    }

    public String getDistanceUrl(String origin, String destination, String wayPoints){

        String alt = "true";

        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        stringBuilder.append("origin=" + origin);
        stringBuilder.append("&destination=" + destination);
        stringBuilder.append("&alternatives=" + alt);
        stringBuilder.append("&mode=driving");
        stringBuilder.append("&waypoints=optimize:true|" + wayPoints);
        stringBuilder.append("&key=" + "AIzaSyCuRGuOxVFfA2rs5gT-w2Y8K_RSlgzualg");

        return stringBuilder.toString();
    }

    public String getPlacesId(List<Map<String, String>> placesIds){

        List<Map<String,String>> placesIdList = new ArrayList<>();

        for (int i = 0; i < placesIds.size(); i++) {

            placesIdList.add(placesIds.get(i));

            Log.i("theList2", placesIdList.toString()
                    .replace("=",":")
                    .replace("{","")
                    .replace("}","")+ "|");
        }

        return placesIdList.toString().replace("[", "")
                .replace("]", "")
                .replace("{", "")
                .replace("}", "")
                .replace("=", ":")
                .replace(" ", "")
                .replace(",", "|");
    }
}




