package com.example.authorisationfirebase;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

    private static final String TAG = "TAG";
    private Map<String, Object> listOfPreferences;
    private GoogleMap mMap;
    private FirebaseFirestore fireStore;
    private FirebaseAuth firebaseAuth;
    private String signIn;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private TextView distDuration;

    private Button generateRoute;
    private Double lat;
    private Double lng;

    private Button geneticAlgorithm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(MapsActivity.this);

        if ((ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED)) {

            Toast.makeText(getApplicationContext(), "Please turn on your location!", Toast.LENGTH_SHORT).show();
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        signIn = firebaseAuth.getCurrentUser().getUid();

        distDuration = findViewById(R.id.distDur);

        geneticAlgorithm = findViewById(R.id.geneticAlg);

        generateRoute = findViewById(R.id.generate);



         getDocument();
         gaButtonOnClick();

    }

    public DocumentReference getDocument(){

        DocumentReference documentReference = fireStore.collection("Preferences").document(signIn);
        documentReference.addSnapshotListener(this, (documentSnapshot, e) -> {
            listOfPreferences = new TreeMap<>();
            listOfPreferences.put("PlaceType", documentSnapshot.get("PlaceType"));
            listOfPreferences.put("PlaceMinPrice", documentSnapshot.get("PlaceMinPrice"));
            listOfPreferences.put("PlaceMaxPrice", documentSnapshot.get("PlaceMaxPrice"));

            Toast.makeText(MapsActivity.this, "Places added " + listOfPreferences.toString().replace("]","")
                    .replace(","," ").replace("[", "")
                    .replace("{", "")
                    .replace("}", "")
                    .replace("=", ""), Toast.LENGTH_SHORT).show();

            Log.i(TAG, listOfPreferences.toString());

        });
        return documentReference;
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
        mMap = googleMap;
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
            lat = location.getLatitude();
            lng = location.getLongitude();
            LatLng currentLoc = new LatLng(lat, lng);

            GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();

            Object dataTransfer[] = new Object[2];

            Object placeType = listOfPreferences.get("PlaceType");
            Object minPrice =  listOfPreferences.get("PlaceMinPrice");
            Object maxPrice =  listOfPreferences.get("PlaceMaxPrice");

            String url = getUrl(lat, lng, (String) placeType, minPrice, maxPrice);

            final  String TAG1= "TAG1";
            Log.i(TAG1, placeType.toString() + " " + minPrice + maxPrice);

            mMap.clear();
            dataTransfer[0] = mMap;
            mMap.addMarker(new MarkerOptions().position(currentLoc).title("Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15));
            dataTransfer[1] = url;

            getNearbyPlaces.execute(dataTransfer);


            /**
             * Click on that button to generate a route between current place and nearby places.
             */
            generateRoute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    GetDirectionsData getDirectionsData = new GetDirectionsData(distDuration);

                    Object dataTransfer[] = new Object[3];

                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;

                    getDirectionsData.execute(dataTransfer);

                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    LatLng currentLoc = new LatLng(lat, lng);

                    final  String TAG1= "TAG1";
                    Log.i(TAG1, placeType.toString() + " " + minPrice + maxPrice);

                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(currentLoc).title("Current Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15));


                    Toast.makeText(MapsActivity.this, "Directions between nearby place", Toast.LENGTH_LONG).show();
                }
            });

        });
    }

    public void gaButtonOnClick() {
        geneticAlgorithm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetDistanceMatrixData getDistanceMatrixData = new GetDistanceMatrixData();
                Object[] dataTransfer = new Object[2];

                Object placeType = listOfPreferences.get("PlaceType");
                Object minPrice =  listOfPreferences.get("PlaceMinPrice");
                Object maxPrice =  listOfPreferences.get("PlaceMaxPrice");

                String url = getUrl(lat, lng, (String) placeType, minPrice, maxPrice);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getDistanceMatrixData.execute(dataTransfer);

                Log.i(TAG, "onClick: " + url);

            }
        });
    }


    public String getUrl(double latitude , double longitude , String nearbyPlace, Object minPrice, Object maxPrice)
    {

        int placeRadius = 1000;

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
        googlePlaceUrl.append("query="+nearbyPlace);
        googlePlaceUrl.append("&location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius=" + placeRadius);
        googlePlaceUrl.append("&opennow");
        googlePlaceUrl.append("&minprice=" +minPrice);
        googlePlaceUrl.append("&maxprice=" +maxPrice);
        googlePlaceUrl.append("&key="+"AIzaSyCuRGuOxVFfA2rs5gT-w2Y8K_RSlgzualg");

        Log.d("MapsActivity", "url = "+googlePlaceUrl);

        return googlePlaceUrl.toString();
    }

}