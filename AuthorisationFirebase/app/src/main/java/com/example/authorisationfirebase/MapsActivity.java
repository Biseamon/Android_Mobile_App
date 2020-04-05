package com.example.authorisationfirebase;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.TreeMap;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    /**
     * This is the main activity of this project.
     * Here the google maps are initialised first of all.
     * Here are performed most of the UI actions.
     */

    private static final String TAG = "TAG";          //a tag needed for logs.
    private Map<String, Object> listOfPreferences;   //Map with a list of preferences.
    private GoogleMap mMap;                         //Google maps
    private FirebaseFirestore fireStore;          //FireStore variable.
    private FirebaseAuth firebaseAuth;           //FireBase authentication.
    private String signIn;                      //Sign in
    private FusedLocationProviderClient fusedLocationProviderClient;  //gets the current location.

    private TextView distanceAndDuration;  //Displays the distance and duration in a text view.
    private TextView distanceAndDuration2;  //Displays the distance and duration in a text view.

    private Button generateRoute;  //generates route button.
    private Double lat;           //latitude - global variable.
    private Double lng;          //longitude - global variable.

    private Button geneticAlgorithm;  //genetic algorithm button.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(MapsActivity.this);

        /**
         * Checks if the user has turned on the location on the device.
         * If not, it will ask for a permission to turn on.
         */
        if ((ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED)) {

            Toast.makeText(getApplicationContext(), "Please turn on your location!", Toast.LENGTH_SHORT).show();
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this); //initialises and gets the current location of the device.
        firebaseAuth = FirebaseAuth.getInstance();            //initialises the FireBase authentication.
        fireStore = FirebaseFirestore.getInstance();         //initialises the FireStore instance.
        signIn = firebaseAuth.getCurrentUser().getUid();    //checks for already logged in users.

        distanceAndDuration = findViewById(R.id.distDur);    //links distanceAndDuration to distDur in XML.
        distanceAndDuration2 = findViewById(R.id.distDur2);    //links distanceAndDuration to distDur in XML.

        geneticAlgorithm = findViewById(R.id.geneticAlg);  //links geneticAlgorithm to geneticAlg in XML.

        generateRoute = findViewById(R.id.generate);     //links generateRoute to generate in XML.

         getDocument();      //getDocument method.
         //gaButtonOnClick(); //gaButtonOnClick method.

    }

    /**
     * This method creates a new collection in the FireStore.
     * It stores attributes like PlaceType, PlaceMinPrice, PlaceMaxPrice.
     * @return
     */
    public DocumentReference getDocument(){

        DocumentReference documentReference = fireStore.collection("Preferences").document(signIn);
        documentReference.addSnapshotListener(this, (documentSnapshot, e) -> {
            listOfPreferences = new TreeMap<>();
            listOfPreferences.put("PlaceType", documentSnapshot.get("PlaceType"));           //takes  the value of the map and stores in the FireStore.
            listOfPreferences.put("PlaceMinPrice", documentSnapshot.get("PlaceMinPrice"));  //takes  the value of the map and stores in the FireStore.
            listOfPreferences.put("PlaceMaxPrice", documentSnapshot.get("PlaceMaxPrice")); //takes  the value of the map and stores in the FireStore.

            Toast.makeText(MapsActivity.this, "Places added " + listOfPreferences.toString().replace("]","")
                    .replace(","," ").replace("[", "")
                    .replace("{", "")
                    .replace("}", "")
                    .replace("=", ""), Toast.LENGTH_SHORT).show();

            //Log.i(TAG, listOfPreferences.toString());

        });
        return documentReference;  //returns a ready collection for use.
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void  onMapReady(GoogleMap googleMap) {
        mMap = googleMap; //Creates a map.

        /**
         * Gets the current location of the device and displays it on the map.
         */
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
            lat =  location.getLatitude();//40.706804;
            lng = location.getLongitude();//-73.620917;
            LatLng currentLoc = new LatLng(lat, lng);  //lat + lng

            GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces(); //getNearbyPlaces object.

            Object dataTransfer[] = new Object[2];  //creates to empty objects that will store data and eventually will be transferred to another class extending AsyncTask class.

            Object placeType = listOfPreferences.get("PlaceType");       //Place Type Object.
            Object minPrice =  listOfPreferences.get("PlaceMinPrice");  //Minimum Price Object.
            Object maxPrice =  listOfPreferences.get("PlaceMaxPrice"); //Maximum Price Object.

            String url = getUrl(lat, lng, (String) placeType, minPrice, maxPrice); //nearbyPlaces url (Places API).

            final  String TAG1= "TAG1";   //Tag for logs.
            //Log.i(TAG1, placeType.toString() + " " + minPrice + maxPrice); //Test/Check the results.

            mMap.clear(); //Clear the map.
            dataTransfer[0] = mMap;  //Use one of the objects to pass the google maps value into it.

            mMap.addMarker(new MarkerOptions().position(currentLoc).title("Current Location")); //add a marker to th map showing the current location.
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));                        //sets the camera fixed on the current location.
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15));            //zooms in the camera on the current location.

            dataTransfer[1] = url; //transfers the second object containing the nearbyPlaces url link. (Places API)

            getNearbyPlaces.execute(dataTransfer); //takes the two objects initialised earlier, object[0] = google map, object[1] = url. Transfers them to the class extending AsyncTask.


            /**
             * Click on that button to generate a route between current place and nearby places.
             */
            generateRoute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Initialising the GetDirectionsData.class/
                    GetDirectionsData getDirectionsData = new GetDirectionsData(distanceAndDuration);

                    Object dataTransfer[] = new Object[3]; //prepares 2 objects for a data transfer.

                    dataTransfer[0] = mMap; //Google maps transfer.
                    dataTransfer[1] = url; //Places API link transfer.

                    getDirectionsData.execute(dataTransfer); //transfers the two objects to a class extending AsyncTask.

                    lat =  location.getLatitude();//40.706804;   //this finds the current latitude the device is located at. The latitude can be entered manually too.
                    lng = location.getLongitude(); //-73.620917; //this finds the current longitude the device is located at. The longitude can be entered manually too.
                    LatLng currentLoc = new LatLng(lat, lng);  //lat + lng

                    dataTransfer[2] = currentLoc;

                    final  String TAG2= "TAG2";  //tag for logs.
                    //Log.i(TAG2, placeType.toString() + " " + minPrice + maxPrice); //test/check the result.

                    mMap.clear(); //clear the map.
                    mMap.addMarker(new MarkerOptions().position(currentLoc).title("Current Location")); //title for the marker.
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc)); //focus the camera on the current location.
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15));  //zooms in the current location.

                    Toast.makeText(MapsActivity.this, "Directions between nearby place", Toast.LENGTH_LONG).show();
                }
            });

            geneticAlgorithm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Initialises the GetDistanceMatrix.class.
                    GetDistanceMatrixData getDistanceMatrixData = new GetDistanceMatrixData(distanceAndDuration2);
                    Object[] dataTransfer = new Object[3];  //prepares two objects for data transfer.

                    lat =  location.getLatitude();//40.706804;   //this finds the current latitude the device is located at. The latitude can be entered manually too.
                    lng = location.getLongitude(); //-73.620917; //this finds the current longitude the device is located at. The longitude can be entered manually too.
                    LatLng currentLoc = new LatLng(lat, lng);  //lat + lng

                    Object placeType = listOfPreferences.get("PlaceType");        //Place type object.
                    Object minPrice =  listOfPreferences.get("PlaceMinPrice");   //minPrice object.
                    Object maxPrice =  listOfPreferences.get("PlaceMaxPrice");  //maxPrice object.

                    String url = getUrl(lat, lng, (String) placeType, minPrice, maxPrice); //Places API link.
                    dataTransfer[0] = mMap;   //transfer the first object containing the google maps.
                    dataTransfer[1] = url;   //transfers the second object containing the Places API link.
                    dataTransfer[2] = currentLoc;

                    getDistanceMatrixData.execute(dataTransfer);

                    // Log.i("placesApi", "Places API " + url);  //test/check the Places API result.

                    //startActivity(new Intent(getApplicationContext(),GAMaps.class));

                }
            });

        });
    }

    /**
     * Method that sets on click listener
     * for the genetic algorithm button.
     */
//    public void gaButtonOnClick() {
//        geneticAlgorithm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//               // Initialises the GetDistanceMatrix.class.
//                GetDistanceMatrixData getDistanceMatrixData = new GetDistanceMatrixData(distanceAndDuration2);
//                Object[] dataTransfer = new Object[2];  //prepares two objects for data transfer.
//
//                Object placeType = listOfPreferences.get("PlaceType");        //Place type object.
//                Object minPrice =  listOfPreferences.get("PlaceMinPrice");   //minPrice object.
//                Object maxPrice =  listOfPreferences.get("PlaceMaxPrice");  //maxPrice object.
//
//                String url = getUrl(lat, lng, (String) placeType, minPrice, maxPrice); //Places API link.
//                dataTransfer[0] = mMap;   //transfer the first object containing the google maps.
//                dataTransfer[1] = url;   //transfers the second object containing the Places API link.
//
//                getDistanceMatrixData.execute(dataTransfer);
//
//               // Log.i("placesApi", "Places API " + url);  //test/check the Places API result.
//
//                //startActivity(new Intent(getApplicationContext(),GAMaps.class));
//
//            }
//        });
//    }


    /**
     * This method returns a link constructed using string builder class.
     * Its link is an API call request that that has a form of a JSON file.
     * The JSON file is later used for other API manipulations such as Directions API, Distance Matrix API.
     * @param latitude
     * @param longitude
     * @param nearbyPlace
     * @param minPrice
     * @param maxPrice
     * @return
     */
    public String getUrl(double latitude , double longitude , String nearbyPlace, Object minPrice, Object maxPrice)
    {

        int placeRadius = 2000; //sets the radius of the search.

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
        googlePlaceUrl.append("query="+nearbyPlace);
        googlePlaceUrl.append("&location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius=" + placeRadius);
        //googlePlaceUrl.append("&opennow");  //only displays places that are open.
        googlePlaceUrl.append("&minprice=" +minPrice);
        googlePlaceUrl.append("&maxprice=" +maxPrice);
        googlePlaceUrl.append("&key="+"AIzaSyCuRGuOxVFfA2rs5gT-w2Y8K_RSlgzualg"); //API_KEY is supposed to be personal and unique. Do not share it.

        //Log.d("MapsActivity", "url = "+googlePlaceUrl);

        return googlePlaceUrl.toString();  //returns a fully constructed link.
    }

}