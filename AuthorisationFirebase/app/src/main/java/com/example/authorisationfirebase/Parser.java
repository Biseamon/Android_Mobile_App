package com.example.authorisationfirebase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {

    /**
     * This class is a parser class.
     * Its methods have the purpose of extracting data
     * for different scenarios.
     * @param jsonData
     * @return
     */

    /**
     * Method returns a list of nearby places needed.
     * It is executed in the GetNearbyPlaces.class.
     * @param jsonData
     * @return
     */
    public List<Map<String, String>> parse(String jsonData)
    {
        JSONArray jsonArray = new JSONArray(); //new JSON Array.
        JSONObject jsonObject;

        //Log.d("json_data", jsonData); //checks the parameter.

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results"); //extracts the JSON array called "results".

        } catch (JSONException e) {
           //Log.d("Error_parse ",  "Error " + e);
        }
        return getPlaces(jsonArray);  //uses a method getPlaces() as a return. The method takes a JSON array as parameter and returns a List<Map<String,String>>.
    }

    /**
     * This method parses the Direction Data from
     * the Direction API.
     * @param jsonData
     * @return
     */
    public List<Map<String, String>> parseDirectionData(String jsonData){

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;

        //Log.d("json data1", jsonData); //check the parameter.

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");

        } catch (JSONException e) {
            //Log.d("Error_parseDirections ",  "Error " + e); //Check the error.
        }
        return getPlacesIdFromJSONArray(jsonArray); //it returns a method that takes as parameter a JSON array and returns a List<Map<String,String>>.
    }

    /**
     * This method extracts the places ids from a JSON Array.
     * @param jsonArray
     * @return
     */
    public List<Map<String,String>> getPlacesIdFromJSONArray(JSONArray jsonArray){

        int count = jsonArray.length();                                 //checks the jsonArray length.
        Map<String,String> placesIds;                                  //Map data structure.
        List<Map<String, String>> placeIdList = new ArrayList<>();    //An arraylist that will store the places Id.

        //Loops through the jsonArray.
        for (int i = 0; i < count; i++) {
            try {
                placesIds = getPlaceId((JSONObject) jsonArray.get(i)); //getPlacesId is a method that returns one Id per iteration.
                placeIdList.add(placesIds);                           //add places id to the list created earlier.
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placeIdList;  //returns the list of places Ids.
    }

    /**
     * This method gets one place Id at a time from the JSON file
     * and stores them in a map.
     * @param placeIdObj
     * @return
     */
    public Map<String,String> getPlaceId(JSONObject placeIdObj){

        Map<String, String> idPlaces = new HashMap<>(); //new HashMap.
        String placesIds = "";                         //An empty string.
        String latitude;                               //latitude
        String longitude;                              //longitude
        String latLng;

        try {
            if (!placeIdObj.isNull("name")) {                   //checks the JSON file for name variables.
                placesIds = placeIdObj.getString("place_id");  //if is not null then we get the string called "place_id".
            }

            latitude = String.valueOf(Double.parseDouble(placeIdObj.getJSONObject("geometry").getJSONObject("location").getString("lat")));   //takes the coordinates.
            longitude = String.valueOf(Double.parseDouble(placeIdObj.getJSONObject("geometry").getJSONObject("location").getString("lng"))); //takes the coordinates.

            latLng = latitude + "!" + longitude;

            idPlaces.put("latLngs", latLng);                 //put the results into a Map.


        }catch(JSONException e){
            //Log.d("Error_getPlaceId",  "Error " + e); //checks the error.
        }


        return idPlaces;                                  //returns the map containing the places id.
    }

    /**
     * This method returns a list of places.
     * It is also used in the method "parse" as return.
     * @param jsonArray
     * @return
     */
    private List<Map<String, String>> getPlaces(JSONArray jsonArray)
    {
        int count = jsonArray.length();                           //jsonArray length.
        List<Map<String, String>> placeList = new ArrayList<>(); //new ArrayList of places.
        Map<String, String> placeMap;

        for(int i = 0; i<count;i++)
        {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));  //uses getPlace() method to get one place per iteration.
                placeList.add(placeMap);
            } catch (JSONException e) {
                //Log.d("Error_getPlaces",  "Error " + e); //checks the error.
            }
        }
        return placeList; //returns a list of places.
    }

    /**
     * This method returns multiple attributes from
     * the JSON file. It is use also for the getPlaces() method.
     * @param googlePlaceJson
     * @return
     */
    private Map<String, String> getPlace(JSONObject googlePlaceJson)
    {
        Map<String, String> googlePlaceMap = new HashMap<>(); //new HashMap.
        String placeName = "--NA--";                         // places name.
        String vicinity= "--NA--";                          //vicinity
        String latitude= "";                               //latitude
        String longitude="";                              //longitude
        String place_id="";                              //place id

        //Log.d("getPlace","" + googlePlaceJson.toString());           //check the json file entered as parameter for this method.

        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");      //takes the names.
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity"); //takes the vicinity data.
            }

            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");   //takes the coordinates.
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng"); //takes the coordinates.

            place_id = googlePlaceJson.getString("place_id");  //takes the place_id data.

            googlePlaceMap.put("place_name", placeName);           //stores names in a HashMap.
            googlePlaceMap.put("vicinity", vicinity);             //stores vicinities in a HashMap.
            googlePlaceMap.put("lat", latitude);                 //stores latitudes in a HashMap.
            googlePlaceMap.put("lng", longitude);               //stores longitudes in a HashMap.
            googlePlaceMap.put("place_id", place_id);          //stores places_id in a HashMap.

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return googlePlaceMap; //return a map containing all the attributes from above.

    }

    /**
     * This method gets all the origin places
     * stores in a JSON file using Distance Matrix API.
     * @param jsonData
     * @return
     */
    public List<String> parseOrigins(String jsonData){
        List<String> listOfOriginPlaces = new ArrayList<>();                            //new ArrayList.
        try{
            JSONObject jsonObject = new JSONObject(jsonData);                         //new JSON object.

            JSONArray origins = (JSONArray) jsonObject.get("origin_addresses");     //extracts the origin_addresses.
            for (int i = 0; i < origins.length(); i++) {
                listOfOriginPlaces.add(origins.getString(i));                     //adds the addresses to the list created earlier.
            }

        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return listOfOriginPlaces;                                           //returns a list of origin places.
    }

    /**
     * This method gets all the destination places
     * stores in a JSON file using Distance Matrix API.
     * @param jsonData
     * @return
     */
    public List<String> parseDestinations(String jsonData){
        List<String> listOfDestinationPlaces = new ArrayList<>();                              //new ArrayList.
        try{
            JSONObject jsonObject = new JSONObject(jsonData);                                //new JSON object.

            JSONArray destinations = (JSONArray) jsonObject.get("destination_addresses"); //extracts the destination_addresses.
            for (int i = 0; i < destinations.length(); i++) {
                listOfDestinationPlaces.add(destinations.getString(i));                 //adds the addresses to the list created earlier.
            }

        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return listOfDestinationPlaces;  //returns a list of origin places.
    }

    /**
     * This method returns data from
     * the Distance Matrix JSON file.
     * @param jsonData
     * @return
     */
    public List<String> parseDistanceFromMatrix(String jsonData){

        List<String> listOfDistances = new ArrayList<>();         //new ArrayList.

        try{
            JSONObject jsonObject = new JSONObject(jsonData);  //new JSON object.
            JSONArray rows=(JSONArray)jsonObject.get("rows"); //gets the "rows" array.

            //loops through the json array.
            for (int i = 0; i < rows.length(); i++) {
                JSONObject obj2 = (JSONObject)rows.get(i);              //loops through all the objects inside the json array.
                JSONArray elements = (JSONArray)obj2.get("elements");  //finds and stores "elements" array.

                //loops through the elements array.
                for (int j = 0; j < elements.length(); j++) {
                    JSONObject obj3 = (JSONObject)elements.get(j); //iterates through all the objects in the array.

                    JSONObject obj5=(JSONObject)obj3.get("distance");  // extract the json object called "distance".
                    String distance = obj5.getString("text");   // extract the string value of the "distance" object.
                    listOfDistances.add(distance);                   //add the distance string value to a list created in the method's field.
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return listOfDistances; //returns a list of distances.
    }

    /**
     * This method returns data from
     * the Distance Matrix JSON file.
     * @param jsonData
     * @return
     */
    public List<String> parseDurationFromMatrix(String jsonData){

        List<String> listOfDurations = new ArrayList<>();           //new ArrayList.

        try{
            JSONObject jsonObject = new JSONObject(jsonData);    //new JSON object.
            JSONArray rows = (JSONArray)jsonObject.get("rows"); //gets the "rows" array.

            //loops through the json array.
            for (int i = 0; i < rows.length(); i++) {
                JSONObject obj2 = (JSONObject)rows.get(i);             //loops through all the objects inside the json array.
                JSONArray elements = (JSONArray)obj2.get("elements"); //finds and stores "elements" array.

                //loops through the elements array.
                for (int j = 0; j < elements.length(); j++) {
                    JSONObject obj3 = (JSONObject)elements.get(j); //iterates through all the objects in the array.

                    JSONObject obj5=(JSONObject)obj3.get("duration");// extract the json object called "duration".
                    String duration = obj5.getString("text"); // extract the string value of the "duration" object.
                    listOfDurations.add(duration);                 //add the duration string value to a list created in the method's field.
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return listOfDurations;  //returns a list of durations.
    }
}
