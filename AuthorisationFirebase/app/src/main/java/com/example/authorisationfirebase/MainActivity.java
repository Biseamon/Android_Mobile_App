package com.example.authorisationfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView fullName,email,phone;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        phone = findViewById(R.id.profilePhone);
        fullName = findViewById(R.id.profileName);
        email    = findViewById(R.id.profileEmail);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, (documentSnapshot, e) -> {
            phone.setText(documentSnapshot.getString("phone"));
            fullName.setText(documentSnapshot.getString("fName"));
            email.setText(documentSnapshot.getString("email"));
        });

        DocumentReference documentReference2 = fStore.collection("users").document("userId2");
        documentReference2.addSnapshotListener(this, (documentSnapshot, e) -> {
            phone.setText(documentSnapshot.getString("phone"));
            fullName.setText(documentSnapshot.getString("fName"));
            email.setText(documentSnapshot.getString("email"));
        });
    }

    public void logout(View view) {

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));

    }
}
