package com.example.alannalucas.assignment3.ElectronicsActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alannalucas.assignment3.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class PurchaseHistory extends AppCompatActivity {

    private RecyclerView mRecyclerHistory;
    private FirebaseAuth mAuth;
    private DatabaseReference HistoryRef;
    private BottomNavigationView mBottomNav;
    private FirebaseAuth.AuthStateListener mAuthListener;
    View mView;
    private static final String TAG = "ItemsList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);

        mAuth = FirebaseAuth.getInstance();
        /*FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();*/
        HistoryRef = FirebaseDatabase.getInstance().getReference().child("PurchaseHistory");


        mRecyclerHistory = findViewById(R.id.recyclerHistory);
        mRecyclerHistory.setHasFixedSize(true);
        mRecyclerHistory.setLayoutManager(new LinearLayoutManager(this));

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

        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectNavigation(item);
                return true;
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        startListening();

    }

    public void startListening() {
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("ShoppingCart").child(userID).limitToLast(50);

        FirebaseRecyclerOptions<CartData> options =
                new FirebaseRecyclerOptions.Builder<CartData>()
                        .setQuery(query, CartData.class)
                        .build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<CartData, PurchaseHistory.UserViewHolder>(options) {
            @Override
            public PurchaseHistory.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.catalogue_layout, parent, false);

                return new PurchaseHistory.UserViewHolder(view);


            }

            @Override
            protected void onBindViewHolder(PurchaseHistory.UserViewHolder holder, int position, CartData model) {
                // Bind the Chat object to the ChatHolder
                holder.setTitle(model.title);
                holder.setManufacturer(model.manufacturer);
                holder.setPrice(model.price);

                //final String userID = getRef(position).getKey();

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(PurchaseHistory.this, PurchaseHistory.class);
                        startActivity(intent);

                    }
                });

            }

        };
        mRecyclerHistory.setAdapter(adapter);
        adapter.startListening();
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



    public static class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }


        public void setTitle(String title) {
            TextView customerName = (TextView) mView.findViewById(R.id.itemSingleTitle);
            customerName.setText(title);
        }

        public void setPrice(String price) {
            TextView customerAddress = (TextView) mView.findViewById(R.id.itemSinglePrice);
            customerAddress.setText(price);
        }

        public void setManufacturer(String manufacturer) {
            TextView customerName = (TextView) mView.findViewById(R.id.itemSingleManufacturer);
            customerName.setText(manufacturer);
        }



    }

}
