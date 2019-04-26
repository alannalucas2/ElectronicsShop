package com.example.alannalucas.assignment3.ElectronicsActivities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alannalucas.assignment3.CustomerMainActivity;
import com.example.alannalucas.assignment3.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class Catalogue extends AppCompatActivity {



    private RecyclerView mRecyclerCatalogue;
    Uri uriProfileImage;
    String profileImageUrl;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference electronicsDatabase;
    private BottomNavigationView mBottomNav;
    private FirebaseAuth.AuthStateListener mAuthListener;
    View mView;
    private static final String TAG = "ItemsList";



    public ArrayList<String> itemsList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        mAuth = FirebaseAuth.getInstance();
        /*FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();*/

        electronicsDatabase = FirebaseDatabase.getInstance().getReference().child("ElectronicGoods");


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        electronicsDatabase = mFirebaseDatabase.getReference();

        mRecyclerCatalogue = (RecyclerView) findViewById(R.id.recyclerCatalogue);
        mRecyclerCatalogue.setHasFixedSize(true);
        mRecyclerCatalogue.setLayoutManager(new LinearLayoutManager(this));


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

    //recycler view uses Adapter design pattern

    public void startListening() {
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("ElectronicGoods")
                .limitToLast(50);

        FirebaseRecyclerOptions<ElectronicGoods> options =
                new FirebaseRecyclerOptions.Builder<ElectronicGoods>()
                        .setQuery(query, ElectronicGoods.class)
                        .build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<ElectronicGoods, UserViewHolder>(options) {
            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.catalogue_layout, parent, false);

                return new UserViewHolder(view);


            }

            //view holder uses Singleton design pattern to improve preformance when
            //scrolling through the list

            @Override
            protected void onBindViewHolder(UserViewHolder holder, int position, ElectronicGoods model) {
                // Bind the Chat object to the ChatHolder
                holder.setTitle(model.title);
                holder.setManufacturer(model.manufacturer);
                holder.setPrice(model.price);

                final String title = getRef(position).getKey();
                final String manufacturer = getRef(position).getKey();
                final String category = getRef(position).getKey();
                final String price = getRef(position).getKey();
                final String quantity = getRef(position).getKey();


                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intent = new Intent(Catalogue.this, CustomerOptions.class);
                        intent.putExtra("ValueKey", title);
                        intent.putExtra("manufacturer", manufacturer);
                        intent.putExtra("ValueKey2", category);
                        intent.putExtra("ValueKey3", price);
                        intent.putExtra("ValueKey4", quantity);
                        startActivity(intent);

                    }
                });

            }

        };
        mRecyclerCatalogue.setAdapter(adapter);
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

    /*public void recyclerClick(View view) {
            int itemPosition = mRecyclerCatalogue.getChildLayoutPosition(view);
            String item = mList.get(itemPosition);
    }*/




    public static class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }


        public void setTitle(String name) {
            TextView userNameView = (TextView) mView.findViewById(R.id.itemSingleTitle);
            userNameView.setText(name);
        }

        public void setManufacturer(String address) {
            TextView userAddressView = (TextView) mView.findViewById(R.id.itemSingleManufacturer);
            userAddressView.setText(address);
        }

        public void setPrice(String address) {
            TextView userAddressView = (TextView) mView.findViewById(R.id.itemSinglePrice);
            userAddressView.setText(address);
        }

    }

}



