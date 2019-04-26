package com.example.alannalucas.assignment3.ElectronicsActivities;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alannalucas.assignment3.AdminMainActivity;
import com.example.alannalucas.assignment3.CustomerMainActivity;
import com.example.alannalucas.assignment3.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddElectronics extends AppCompatActivity {


    private Button btnSave, btnReturn;
    private EditText mEditTitle, mEditPrice, mEditImage, mEditQuantity, mEditManufacturer, mEditCategory;
    private Spinner mSpinner;

    private BottomNavigationView mBottomNav;
    private static final int CHOOSE_IMAGE = 101;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseElectronics;
    ImageView imageView;
    Uri uriProfileImage;
    String profileImageUrl;
    private String saveCurrentDate, saveCurrentTime, postRandomName, downloadUrl;
    String title, price, quantity, manufacturer, category, image;
    StorageReference imageReference;
    DatabaseReference imageRef;

    private static final String TAG = "AddToDatabase";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_electronics);

        //how to get page to say hi with name
        //textViewUserEmail.setText("Welcome " + user.getEmail());


        mEditTitle = (EditText) findViewById(R.id.editTitle);
        //mEditCategory = (EditText) findViewById(R.id.editCategory);
        mEditManufacturer = (EditText) findViewById(R.id.editManufacturer);
        mEditQuantity = (EditText) findViewById(R.id.editQuantity);
        mEditPrice = (EditText) findViewById(R.id.editPrice);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnReturn = (Button) findViewById(R.id.btnGoBack);
        imageView = (ImageView) findViewById(R.id.imageView);
        mSpinner = (Spinner) findViewById(R.id.categorySpinner);

        imageReference = FirebaseStorage.getInstance().getReference();

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

        Spinner spinner = findViewById(R.id.categorySpinner);
        String[] items = new String[]{"Laptop", "Phone", "Earphones"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        databaseElectronics = mFirebaseDatabase.getReference();


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

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddElectronics.this, AdminMainActivity.class);
                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                saveElectronics();
            }
        });

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
    }

    public void clearDetails(){
        mEditTitle.setText("");
        mEditManufacturer.setText("");
        mEditQuantity.setText("");
        mEditPrice.setText("");
    }



    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), CHOOSE_IMAGE);

    }


    public void onClick(View view) {
        if (view==btnSave){
            mEditTitle.getText().clear();
            //mSpinner.getSelectedItem()
            mEditManufacturer.getText().clear();
            mEditPrice.getText().clear();
            mEditQuantity.getText().clear();
            mEditImage.getText().clear();
            //saveElectronics();

        }
    }

    private void saveElectronics() {
        String title = mEditTitle.getText().toString().trim();
        String manufacturer = mEditManufacturer.getText().toString().trim();
        //ImageView image = mEditImage.get
        String catSpinner = mSpinner.getSelectedItem().toString();
        String quantity = mEditQuantity.getText().toString().trim();
        String price = mEditPrice.getText().toString().trim();



        ElectronicGoods electronics = new ElectronicGoods(title, manufacturer, image, catSpinner, quantity, price);

        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        databaseElectronics.child("ElectronicGoods").child(title).setValue(electronics).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddElectronics.this, "Electronic Good saved", Toast.LENGTH_LONG).show();
                clearDetails();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddElectronics.this, "Error Electronic good not saved", Toast.LENGTH_LONG).show();

                    }
                });



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



