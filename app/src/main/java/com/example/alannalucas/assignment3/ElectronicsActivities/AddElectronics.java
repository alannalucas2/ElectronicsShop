package com.example.alannalucas.assignment3.ElectronicsActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alannalucas.assignment3.CustomerMainActivity;
import com.example.alannalucas.assignment3.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddElectronics extends AppCompatActivity {


    private Button btnSave;
    private EditText mEditTitle, mEditPrice, mEditImage, mEditQuantity, mEditManufacturer, mEditCategory;

    private BottomNavigationView mBottomNav;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseElectronics;

    private static final String TAG = "AddToDatabase";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_electronics);

        //how to get page to say hi with name
        //textViewUserEmail.setText("Welcome " + user.getEmail());


        mEditTitle=(EditText)findViewById(R.id.editTitle);
        mEditCategory=(EditText)findViewById(R.id.editCategory);
        mEditManufacturer=(EditText)findViewById(R.id.editManufacturer);
        mEditQuantity=(EditText)findViewById(R.id.editQuantity);
        mEditPrice=(EditText)findViewById(R.id.editPrice);
        mEditImage=(EditText)findViewById(R.id.editImage);
        btnSave=(Button)findViewById(R.id.btnSave);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        databaseElectronics = mFirebaseDatabase.getReference();

        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectNavigation(item);
                return true;
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
                // ...
            }
        };

        databaseElectronics.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Object value = dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });





        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveElectronics();
            }
        });

    }


    public void onClick(View view) {
        if (view==btnSave){
            mEditTitle.getText().clear();
            mEditCategory.getText().clear();
            mEditManufacturer.getText().clear();
            mEditPrice.getText().clear();
            mEditQuantity.getText().clear();
            mEditImage.getText().clear();
            saveElectronics();

        }
    }

    private void saveElectronics() {
        String title = mEditTitle.getText().toString().trim();
        String manufacturer = mEditManufacturer.getText().toString().trim();
        String category = mEditCategory.getText().toString().trim();
        String quantity = mEditQuantity.getText().toString().trim();
        String price = mEditPrice.getText().toString().trim();
        String image = mEditImage.getText().toString().trim();


        ElectronicGoods electronics = new ElectronicGoods(title, manufacturer, image, category, quantity, price);

        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        databaseElectronics.child(userID).child("Electronic Goods").child(title).setValue(electronics).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddElectronics.this, "Electronic Good saved", Toast.LENGTH_LONG).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddElectronics.this, "Error Electronic good not saved", Toast.LENGTH_LONG).show();

                    }
                });

    }


    private void selectNavigation(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.btmCatalogue:
                Intent intent = new Intent(this, AddElectronics.class);
                this.startActivity(intent);
                break;

            case R.id.btmCart:
                Intent intent1 = new Intent(this, CustomerMainActivity.class);
                this.startActivity(intent1);
                break;

            case R.id.btmProfile:
                Intent intent3 = new Intent(this, CustomerMainActivity.class);
                this.startActivity(intent3);
                break;

        }
    }

    //add a toast to show when successfully signed in
    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


}


