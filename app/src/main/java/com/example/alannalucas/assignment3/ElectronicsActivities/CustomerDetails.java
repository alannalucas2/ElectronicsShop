package com.example.alannalucas.assignment3.ElectronicsActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alannalucas.assignment3.CustomerMainActivity;
import com.example.alannalucas.assignment3.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerDetails extends AppCompatActivity {

    Button submit;
    EditText name, address, cardNum, cardSpinner;
    FirebaseAuth mAuth;
    DatabaseReference customerDB;
    private BottomNavigationView mBottomNav;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "AddToDatabase";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        mAuth = FirebaseAuth.getInstance();
        customerDB = FirebaseDatabase.getInstance().getReference().child("CustomerDetails");

        final EditText nameField = findViewById(R.id.name);
        final EditText addressField = findViewById(R.id.address);
        final EditText paymentField = findViewById(R.id.cardNum);
        final Spinner mySpinner = findViewById(R.id.cardTypeSpinner);


        Button submit = findViewById(R.id.btnSubmit);
        Button back = findViewById(R.id.btnBack);

        /*mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectNavigation(item);
                return true;
            }
        });*/


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    //toastMessage("Successfully signed out.");
                }
                // ...
            }
        };

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerDetails.this, CustomerMainActivity.class);
                startActivity(intent);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address = addressField.getText().toString();
                String name = nameField.getText().toString();
                String payment = paymentField.getText().toString();
                String cardSpinner = mySpinner.getSelectedItem().toString();

                Customer details = new CustomerBuilder().setName(name).setAddress(address).setPayment(payment).setCardSpinner(cardSpinner).createCustomer();

                FirebaseUser user = mAuth.getCurrentUser();
                String userID = user.getUid();

                customerDB.child(userID).setValue(details).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CustomerDetails.this, "Details saved", Toast.LENGTH_LONG).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CustomerDetails.this, "Error details not saved", Toast.LENGTH_LONG).show();

                            }
                        });


                startActivity(new Intent(CustomerDetails.this, Catalogue.class));
            }
        });


        Spinner dropdown = findViewById(R.id.cardTypeSpinner);
        String[] items = new String[]{"Debit Card", "Credit Card"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }


        private void selectNavigation(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.btmCatalogue:
                    Intent intent = new Intent(this, Catalogue.class);
                    this.startActivity(intent);
                    break;

                case R.id.btmCart:
                    Intent intent1 = new Intent(this, ShoppingCart.class);
                    this.startActivity(intent1);
                    break;

                case R.id.btmProfile:
                    Intent intent3 = new Intent(this, CustomerDetails.class);
                    this.startActivity(intent3);
                    break;

            }
        }

    }
