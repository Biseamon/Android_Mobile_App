package com.example.authorisationfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Preferences extends AppCompatActivity {
    final static String TAG = "TAG";

    private Button button;
    private Button contBtn;
    private Button logOut;
    private FirebaseFirestore fireStore;
    private FirebaseAuth firebaseAuth;

    private TextView atms;
    private TextView restaurants;
    private TextView bars;

    private Map<String, Object> selectedPlaces;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        restaurants = findViewById(R.id.restaurants);
        atms  = findViewById(R.id.atm);
        bars = findViewById(R.id.bars);

        selectedPlaces = new HashMap<>();

        button = findViewById(R.id.saveButton);
        contBtn = findViewById(R.id.button_continue);
        logOut = findViewById(R.id.logOut);

        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();


       restaurants.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               selectedPlaces.put("PlaceType", restaurants.getText());

               Toast.makeText(Preferences.this, "PLaces added " + selectedPlaces.toString(), Toast.LENGTH_LONG ).show();
           }
       });


       atms.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               selectedPlaces.put("PlaceType", atms.getText());
               selectedPlaces.put("PriceMinPrice", null);
               selectedPlaces.put("PriceMaxPrice", null);

               Toast.makeText(Preferences.this, "PLaces added " + selectedPlaces.toString(), Toast.LENGTH_LONG ).show();
           }
       });


       bars.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               selectedPlaces.put("PlaceType", bars.getText());
               Toast.makeText(Preferences.this, "PLaces added " + selectedPlaces.toString(), Toast.LENGTH_LONG ).show();
           }
       });

        button.setOnClickListener(v -> {

            Spinner spinner1 = findViewById(R.id.list_item2);
            Spinner spinner2 = findViewById(R.id.list_item1);

            String getList = String.valueOf(spinner2.getSelectedItem());
            String getList1 = String.valueOf(spinner1.getSelectedItem());

            selectedPlaces.put("PlaceMinPrice" , getList1);
            selectedPlaces.put("PlaceMaxPrice", getList);

            userId = firebaseAuth.getCurrentUser().getUid();
            DocumentReference documentReference = fireStore.collection("Preferences").document(userId);

            Toast.makeText(Preferences.this, "Successfully added preferences: " +
                    selectedPlaces.toString(), Toast.LENGTH_LONG).show();

            documentReference.set(selectedPlaces).addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess: user Profile is created for "+ userId)).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: " + e.toString());
                }
            });
        });

        contBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

    }



}
