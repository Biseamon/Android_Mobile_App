package com.example.authorisationfirebase;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {


    public List<Map<String, String>> parse(String jsonData)
    {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;

        Log.d("json data", jsonData);

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");

        } catch (JSONException e) {
           Log.d("Error2 ",  "Error " + e);
        }
        return getPlaces(jsonArray);
    }

    public List<Map<String, String>> parseTest(String jsonData){

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;

        Log.d("json data1", jsonData);

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");

        } catch (JSONException e) {
            Log.d("Error3 ",  "Error " + e);
        }
        return getPlacesIdFromJSONArray(jsonArray);
    }

    public List<Map<String,String>> getPlacesIdFromJSONArray(JSONArray jsonArray){

        int count = jsonArray.length();
        Map<String,String> placesIds;
        List<Map<String, String>> placeIdList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            try {
                placesIds = getPlaceId((JSONObject) jsonArray.get(i));
                placeIdList.add(placesIds);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placeIdList;
    }

    public Map<String,String> getPlaceId(JSONObject placeIdObj){

        Map<String, String> idPlaces = new HashMap<>();
        String placesIds = "";

        try {
            if (!placeIdObj.isNull("name")) {
                placesIds = placeIdObj.getString("place_id");
            }
        }catch(JSONException e){
            Log.d("Error4 ",  "Error " + e);
        }
        idPlaces.put("place_id", placesIds);

        return idPlaces;
    }

    private List<Map<String, String>> getPlaces(JSONArray jsonArray)
    {
        int count = jsonArray.length();
        List<Map<String, String>> placeList = new ArrayList<>();
        Map<String, String> placeMap;

        for(int i = 0; i<count;i++)
        {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placeList.add(placeMap);
            } catch (JSONException e) {
                Log.d("Error5 ",  "Error " + e);
            }
        }
        return placeList;
    }

    private Map<String, String> getPlace(JSONObject googlePlaceJson)
    {
        Map<String, String> googlePlaceMap = new HashMap<>();
        String placeName = "--NA--";
        String vicinity= "--NA--";
        String latitude= "";
        String longitude="";
        String place_id="";

        Log.d("DataParser","jsonobject ="+googlePlaceJson.toString());

        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
            }

            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

            place_id = googlePlaceJson.getString("place_id");

            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("place_id", place_id);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return googlePlaceMap;

    }


    public List<Map<String,String>> parseTesting(String jsonFile){
        JSONObject jsonObject;
        JSONArray jsonArray;

        List<Map<String, String>> list = new ArrayList<>();
        HashMap<String, String> hashMap = new HashMap<>();

        try {
            jsonObject = new JSONObject(jsonFile);
            jsonArray = jsonObject.getJSONArray ("routes");
            JSONObject route = jsonArray.getJSONObject (0);
            JSONObject poly = route.getJSONObject ("overview_polyline");
            String polyline = poly.getString ("points");
            hashMap.put("obj", polyline);
            list.add(hashMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Map<String, String>> parseDirections(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
                jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0)
                        .getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
                Log.i("jsonArr", "parseDirections: " + jsonArray.toString());
        } catch (JSONException e) {
            Log.d("parsedir", "getPaths: " + e);
        }
        return getPaths(jsonArray);
    }

    public List<Map<String,String>> getPaths(JSONArray googleStepsJson )
    {
        int count = googleStepsJson.length();
        List<Map<String, String>> polylines = new ArrayList<>();
        Map<String,String> placePoints;

        for(int i = 0;i<count;i++)
        {
            try {
                placePoints = getPath((JSONObject) googleStepsJson.get(i));
                polylines.add(placePoints);

                Log.i("getpaths", "getPaths: " + polylines.toString());
            } catch (JSONException e) {
                Log.d("getpaths", "getPaths: " + e);
            }
        }

        return polylines;
    }

    public Map<String, String> getPath(JSONObject googlePathJson)
    {
        HashMap<String,String> polylines = new HashMap<>();
        String polyline;

        try {
            if (!googlePathJson.isNull("polyline")) {
                polyline = googlePathJson.getJSONObject("polyline").getString("points");
                polylines.put("points", polyline);

                Log.i("getpath", "getPath: " + polylines.toString());
            }

        } catch (JSONException e) {
            Log.d("getpath", "getPaths: " + e);
        }
        return polylines;
    }
}
