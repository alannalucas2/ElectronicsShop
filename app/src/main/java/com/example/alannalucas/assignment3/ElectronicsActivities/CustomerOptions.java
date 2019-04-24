package com.example.alannalucas.assignment3.ElectronicsActivities;

import android.content.Intent;
import android.media.Rating;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alannalucas.assignment3.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CustomerOptions extends AppCompatActivity {

    TextView mTitle, mCategory, mManufacturer, mPrice, mQuantity;
    String aImage, aTotal;
    Button mAddBtn, mReturnBtn;
    //RatingBar mRatingBar;
    private DatabaseReference cartDatabase;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "ItemsList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_options);

        Intent intent = getIntent();
        final String aTitle = getIntent().getStringExtra("ValueKey");
        final String aManufacturer = getIntent().getStringExtra("manufacturer");
        final String aCategory = getIntent().getStringExtra("ValueKey2");
        final String aPrice = getIntent().getStringExtra("ValueKey3");
        final String aQuantity = getIntent().getStringExtra("ValueKey4");

        mTitle = (TextView) findViewById(R.id.itemSingleTitle);
        mTitle.setText(aTitle);

        mManufacturer = (TextView) findViewById(R.id.itemSingleManufacturer);
        mManufacturer.setText(aManufacturer);

        mCategory = (TextView) findViewById(R.id.itemSingleCategory);
        mCategory.setText(aCategory);

        mPrice = (TextView) findViewById(R.id.itemSinglePrice);
        mPrice.setText(aPrice);

        mQuantity = (TextView) findViewById(R.id.itemSingleQuantity);
        mQuantity.setText(aQuantity);

        mAddBtn = (Button) findViewById(R.id.addBtn);
        mReturnBtn = (Button) findViewById(R.id.returnBtn);

        mReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerOptions.this, Catalogue.class));
            }
        });


        final RatingBar mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        Button rate = (Button) findViewById(R.id.rateBtn);
        final TextView rateTxt = (TextView) findViewById(R.id.ratingTxt);

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateTxt.setText("Your rating is: " + mRatingBar.getRating());
            }
        });


        mAuth = FirebaseAuth.getInstance();

        cartDatabase = FirebaseDatabase.getInstance().getReference().child("ShoppingCart");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        cartDatabase = mFirebaseDatabase.getReference();


        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                String userID = user.getUid();

                CartData shoppingCart = new CartData(aTitle, aCategory, aManufacturer, aQuantity, aPrice, aImage, aTotal);

                String cartId = cartDatabase.push().getKey();

                cartDatabase.child("ShoppingCart").child(userID).child(aTitle).setValue(shoppingCart).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CustomerOptions.this, "Shopping Cart updated", Toast.LENGTH_LONG).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CustomerOptions.this, "Error", Toast.LENGTH_LONG).show();

                            }
                        });

                /*String title = mTitle.getText().toString();

                Intent intent = new Intent(CustomerOptions.this, ShoppingCart.class);
                intent.putExtra("ValueKey", title);
                startActivity(intent);*/


            }
        });


    }
}
