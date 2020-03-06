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

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    /**
     * Login.class is an Activity that performs actions connected to FireBase.
     * It allows new users to login into their accounts.
     */

    private EditText mEmail,mPassword;     //email and password slots.
    private Button mLoginBtn;             //login button.
    private TextView mCreateBtn;         //create an account button.
    private ProgressBar progressBar;    //progress bar.
    private FirebaseAuth fAuth;        //FireBase authentication.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.Email);               //links mEmail variable to Email id in the xml.
        mPassword = findViewById(R.id.password);        //links mPassword variable to password id in the xml.
        progressBar = findViewById(R.id.progressBar);  //links progressBar variable to progressBar id in the xml.
        fAuth = FirebaseAuth.getInstance();           //initialises the FireBase authentication.
        mLoginBtn = findViewById(R.id.loginBtn);     //links mLogin variable to loginBtn id in the xml.
        mCreateBtn = findViewById(R.id.createText); //links mCreateBtn button variable to createText id in the xml.

        /**
         * Sets on click listener for login button.
         */
        mLoginBtn.setOnClickListener(v -> {

            String email = mEmail.getText().toString().trim();         //Gets the email.
            String password = mPassword.getText().toString().trim();  //Gets the password.

            if(TextUtils.isEmpty(email)){
                mEmail.setError("Email is Required.");
                return;  //checks if email slot is empty and if yes asks to fill in the slot.
            }

            if(TextUtils.isEmpty(password)){
                mPassword.setError("Password is Required.");
                return;  //checks if password slot is empty and if yes asks to fill in the slot.
            }

            if(password.length() < 6){
                mPassword.setError("Password Must be >= 6 Characters");
                return;  //checks if the password is at least 6 characters.
            }

            progressBar.setVisibility(View.VISIBLE);  //sets the progress bar to visible.

            /**
             * Sets an action when email and password are ok.
             * It takes the user to a new activity called, Preferences.class.
             */
            fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Preferences.class));
                }else {
                    Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            });

        });


        /**
         * Sets on click listener for mCreate.
         * On click it goes to a new Activity, called Register.class where a new
         * user can register.
         */
        mCreateBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),Register.class)));


    }
}
