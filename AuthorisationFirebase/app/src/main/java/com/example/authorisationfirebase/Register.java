package com.example.authorisationfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    /**
     * Register.class is an activity that
     * allows new users to register into the system.
     * After registration they are transferred to the
     * Login page where they can use their new login
     * and password to authorise.
     */

    public static final String TAG = "TAG";                               //Tag for logs.
    private EditText mFullName,mEmail,mPassword,mPhone;                  //name, email, password, phone_number fields.
    private Button mRegisterBtn;                                        //registration button.
    private TextView mLoginBtn;                                        //Login field.
    private FirebaseAuth fAuth;                                       //FireBase authentication.
    private ProgressBar progressBar;                                 //Progress bar.
    private FirebaseFirestore fStore;                               //FireStore
    private String userID;                                         //User ID variable.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName   = findViewById(R.id.fullName);          //links mFullName to fullName in XML.
        mEmail      = findViewById(R.id.Email);            //links mEmail to Email in XML.
        mPassword   = findViewById(R.id.password);        //links mPassword to password in XML.
        mPhone      = findViewById(R.id.phone);          //links mPhone to phone is XML.
        mRegisterBtn= findViewById(R.id.registerBtn);   //links mRegisterBtn to registerBtn in XML.
        mLoginBtn   = findViewById(R.id.createText);   //links mLoginBtn to createText in XML.

        fAuth = FirebaseAuth.getInstance();                //initialises the FireBase authentication.
        fStore = FirebaseFirestore.getInstance();         //initialises the FireStore.
        progressBar = findViewById(R.id.progressBar);    //links the progressBar to progressBar in XML.

        if(fAuth.getCurrentUser() != null){                                        //if the user did not log out the account is open.
            startActivity(new Intent(getApplicationContext(),Preferences.class)); //It takes the user straight to the preferences page if the previous user did not log out.
            finish();
        }


        /**
         * Sets on click listener for restaurants.
         */
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();   //gets the text.
                String password = mPassword.getText().toString().trim();  //gets the text.
                final String fullName = mFullName.getText().toString();  //gets the text.
                final String phone    = mPhone.getText().toString();    //gets the text.

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");                      //requires the Email field to be filled in.
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");               //requires the password field to be filled in.
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password Must be >= 6 Characters");   //requires the password to be more than 6 characters field to be filled in.
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);  //sets the progress bar to visible.

                // register the user in FireBase.
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();
                        userID = fAuth.getCurrentUser().getUid(); //gets the current user.
                        //creates a new collection in the FireStore.
                        DocumentReference documentReference = fStore.collection("users").document(userID);

                        Map<String,Object> user = new HashMap<>(); //new HashMap.
                        user.put("fName",fullName);               //put names.
                        user.put("email",email);                 //put emails.
                        user.put("phone",phone);                //put phones.

                        //checks if the user successfully registered in the FireStore.
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                               // Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                               // Log.d(TAG, "onFailure: " + e.toString());
                            }
                        });
                        startActivity(new Intent(getApplicationContext(),Preferences.class)); //starts the preferences activity if successfully registered.

                    }else {
                        Toast.makeText(Register.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE); //Hides the progress bar.
                    }
                });
            }
        });


        /**
         * Sets on click listener for restaurants.
         */
        mLoginBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),Login.class))); //opens in the login.class activity.

    }
}
