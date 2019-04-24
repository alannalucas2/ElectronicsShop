package com.example.alannalucas.assignment3.ElectronicsActivities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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


    private Button btnSave;
    private EditText mEditTitle, mEditPrice, mEditImage, mEditQuantity, mEditManufacturer, mEditCategory;

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
        mEditCategory = (EditText) findViewById(R.id.editCategory);
        mEditManufacturer = (EditText) findViewById(R.id.editManufacturer);
        mEditQuantity = (EditText) findViewById(R.id.editQuantity);
        mEditPrice = (EditText) findViewById(R.id.editPrice);
        btnSave = (Button) findViewById(R.id.btnSave);
        imageView = (ImageView) findViewById(R.id.imageView);

        imageReference = FirebaseStorage.getInstance().getReference();

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

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


    /*private void StoringImageToFirebaseStorage() {
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime;

        StorageReference filePath = imageReference.child("Post Images").child(uriProfileImage.getLastPathSegment() + postRandomName + ".jpg");

        filePath.putFile(uriProfileImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();
                    Toast.makeText(AddElectronics.this, "image uploaded successfully to Storage...", Toast.LENGTH_SHORT).show();

                    SavingPostInformationToDatabase();

                } else {
                    String message = task.getException().getMessage();
                    Toast.makeText(AddElectronics.this, "Error occured: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                saveElectronics();
            }
        });
    }/*




    /*private void SavePost(){
        manufacturer = mEditManufacturer.getText().toString();
        price = mEditPrice.getText().toString();
        category = mEditCategory.getText().toString();
        quantity = mEditQuantity.getText().toString();
        title = mEditTitle.getText().toString();

        if(uriProfileImage == null){
            Toast.makeText(AddElectronics.this, "Please select image...", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(manufacturer))
        {
            Toast.makeText(AddElectronics.this, "please fill all fields", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(quantity))
        {
            Toast.makeText(AddElectronics.this, "please fill all fields", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(title))
        {
            Toast.makeText(AddElectronics.this, "please fill all fields", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(price))
        {
            Toast.makeText(AddElectronics.this, "please fill all fields", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(category))
        {
            Toast.makeText(AddElectronics.this, "please fill all fields", Toast.LENGTH_SHORT).show();
        }else{
            StoringImageToFirebaseStorage();
        }

    }*/


    /*private void SavingPostInformationToDatabase() {

        HashMap imageMap = new HashMap();
            imageMap.put("title", title);
            imageMap.put("manufacturer", manufacturer);
            imageMap.put("category", category);
            imageMap.put("price", price);
            imageMap.put("quantity", quantity);
            imageMap.put("image", image);
        imageRef.child(postRandomName).updateChildren(imageMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Toast.makeText(AddElectronics.this, "successful upload", Toast.LENGTH_SHORT);

                }else{
                    Toast.makeText(AddElectronics.this, "error uploading", Toast.LENGTH_SHORT);
                }
            }
        });

*/




    /*private void loadUserInformation() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this).load(user.getPhotoUrl().toString())
                        .into(imageView);

            }

            if (user.getDisplayName() != null) {
                editName.setText(user.getDisplayName());
            }
        }


    }*/

    /*public void uploadImageToFirebaseStorage() {

        final StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");


        if (uriProfileImage != null) {
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //progressBar.setVisibility(View.GONE);
                            profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    profileImageUrl = uri.toString();
                                    Toast.makeText(getApplicationContext(), "Image Upload Successful", Toast.LENGTH_SHORT).show();
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }*/







    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), CHOOSE_IMAGE);

    }


    public void onClick(View view) {
        if (view==btnSave){
            mEditTitle.getText().clear();
            mEditCategory.getText().clear();
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
        String category = mEditCategory.getText().toString().trim();
        String quantity = mEditQuantity.getText().toString().trim();
        String price = mEditPrice.getText().toString().trim();



        ElectronicGoods electronics = new ElectronicGoods(title, manufacturer, image, category, quantity, price);

        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        databaseElectronics.child("ElectronicGoods").child(title).setValue(electronics).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                Intent intent = new Intent(this, Catalogue.class);
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


    /*public void saveBtn(View view) {
        manufacturer = mEditManufacturer.getText().toString();
        price = mEditPrice.getText().toString();
        category = mEditCategory.getText().toString();
        quantity = mEditQuantity.getText().toString();
        title = mEditTitle.getText().toString();

        if(uriProfileImage == null){
            Toast.makeText(AddElectronics.this, "Please select image...", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(manufacturer))
        {
            Toast.makeText(AddElectronics.this, "please fill all fields", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(quantity))
        {
            Toast.makeText(AddElectronics.this, "please fill all fields", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(title))
        {
            Toast.makeText(AddElectronics.this, "please fill all fields", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(price))
        {
            Toast.makeText(AddElectronics.this, "please fill all fields", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(category))
        {
            Toast.makeText(AddElectronics.this, "please fill all fields", Toast.LENGTH_SHORT).show();
        }else{
            //StoringImageToFirebaseStorage();
        }*/


    }



