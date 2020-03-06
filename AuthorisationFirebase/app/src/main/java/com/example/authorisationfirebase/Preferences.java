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

    /**
     * Preferences.class is an activity that contains
     * a list of places that a user can choose from.
     * Also, there is an option of filtering the chosen places.
     * After clicking on the save saveButton the place is stored
     * to a FireStore and later is used for maps activities.
     */

    final static String TAG = "TAG";                       //Tag for logs.

    private Button saveButton;                          //save_button
    private Button continueButton;                     //continue_button.
    private Button logOutButton;                      //log_out_button.
    private FirebaseFirestore fireStore;             //FireStore class.
    private FirebaseAuth firebaseAuth;              //FireBase authentication class.

    private TextView atm;               //ATM text
    private TextView restaurants;      //restaurants text.
    private TextView bars;            //bars text.

    private Map<String, Object> selectedPlaces; // Map containing the selected places.

    private String userId; //user ID variable.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        restaurants = findViewById(R.id.restaurants);      //links restaurants to restaurants in XML.
        atm = findViewById(R.id.atm);                     //links atm to atm in XML.
        bars = findViewById(R.id.bars);                  //links bars to bars in XML.

        selectedPlaces = new HashMap<>();  //initialise the Map.

        saveButton = findViewById(R.id.saveButton);           //links saveButton to saveButton in XML.
        continueButton = findViewById(R.id.button_continue); //links continueButton to button_continue in XML.
        logOutButton = findViewById(R.id.logOut);           //links logOutButton to logOut in XML.

        firebaseAuth = FirebaseAuth.getInstance();    //initialises the FireBase authentication.
        fireStore = FirebaseFirestore.getInstance(); //initialises the FireStore.

        /**
         * Sets on click listener for restaurants.
         */
       restaurants.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               selectedPlaces.put("PlaceType", restaurants.getText());    //gets the text.

               Toast.makeText(Preferences.this, "PLaces added " + selectedPlaces.toString().replace("{","")
                       .replace("}","").replace("=","")
                       .replace(" ",""), Toast.LENGTH_LONG ).show();
           }
       });

        /**
         * Sets on click listener for restaurants.
         */
       atm.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               selectedPlaces.put("PlaceType", atm.getText()); //gets the text.
               selectedPlaces.put("PriceMinPrice", null);     //sets the prices to null, because it does not have a price range.
               selectedPlaces.put("PriceMaxPrice", null);    //sets the prices to null, because it does not have a price range.

               Toast.makeText(Preferences.this, "PLaces added " + selectedPlaces.toString().replace("{","")
                       .replace("}","").replace("=","")
                       .replace(" ",""), Toast.LENGTH_LONG ).show();
           }
       });

        /**
         * Sets on click listener for restaurants.
         */
       bars.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               selectedPlaces.put("PlaceType", bars.getText()); //gets the text.

               Toast.makeText(Preferences.this, "PLaces added " + selectedPlaces.toString().replace("{","")
                       .replace("}","").replace("=","")
                       .replace(" ",""), Toast.LENGTH_LONG ).show();
           }
       });

        /**
         * Sets on click listener for restaurants.
         */
        saveButton.setOnClickListener(v -> {

            Spinner spinner1 = findViewById(R.id.list_item2);  //Spinner linked to list_item2 which is the price range from 1 to 4 or null.
            Spinner spinner2 = findViewById(R.id.list_item1); //Spinner linked to list_item2 which is the price range from 1 to 4 or null.

            String getList = String.valueOf(spinner2.getSelectedItem());   //gets the selected value from the spinner. Anything from 1 to 4 or null.
            String getList1 = String.valueOf(spinner1.getSelectedItem()); //gets the selected value from the spinner. Anything from 1 to 4 or null.

            selectedPlaces.put("PlaceMinPrice" , getList1); //adds the price value to selectedPlaces map.
            selectedPlaces.put("PlaceMaxPrice", getList);  //adds the price value to selectedPlaces map.

            userId = firebaseAuth.getCurrentUser().getUid();  //initialising the user ID. Checks if the user already exists.

            //Creates a new collection in the FireStore.
            DocumentReference documentReference = fireStore.collection("Preferences").document(userId);

            Toast.makeText(Preferences.this, "Successfully added preferences: " +
                    selectedPlaces.toString().replace("{","")
                            .replace("}","").replace("=","")
                            .replace(" ",""), Toast.LENGTH_LONG).show();

            documentReference.set(selectedPlaces).addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess: user Profile is created for "+ userId)).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: " + e.toString());
                }
            });
        });

        /**
         * Sets on click listener for restaurants.
         */
        continueButton.setOnClickListener(v -> {

            //opens on click a new Activity.
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);

        });

        /**
         * Sets on click listener for restaurants.
         */
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();                             //signs out the user.
                startActivity(new Intent(getApplicationContext(), Login.class)); //returns the user to the Login page.
            }
        });

    }
}
