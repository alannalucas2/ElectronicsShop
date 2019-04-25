package com.example.alannalucas.assignment3.ElectronicsActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alannalucas.assignment3.AdminMainActivity;
import com.example.alannalucas.assignment3.LoginActivities.AdminLoginActivity;
import com.example.alannalucas.assignment3.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart extends AppCompatActivity {


    Button purchase, edit, back;
    TextView total;
    //private List<ElectronicGoods> electronicGoodsList = new ArrayList<>();
    DatabaseReference cartDB;
    private RecyclerView recCart;
    private static final String TAG = "ItemsList";
    TextView mTitle;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        //cartDB = FirebaseDatabase.getInstance().getReference().child("ShoppingCart");

        mAuth = FirebaseAuth.getInstance();


        recCart = findViewById(R.id.recyclerCart);
        recCart.setHasFixedSize(true);
        recCart.setLayoutManager(new LinearLayoutManager(this));

        /*Intent intent = getIntent();
        final String aTitle = getIntent().getStringExtra("ValueKey");

        mTitle = (TextView) findViewById(R.id.itemSingleTitle);
        mTitle.setText(aTitle);*/

        purchase= (Button) findViewById(R.id.purchaseBtn);
        total = (TextView) findViewById(R.id.textTotal);
        back= (Button) findViewById(R.id.backBtn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShoppingCart.this, Catalogue.class);
                startActivity(intent);
            }
        });


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


        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Builder pattern.

                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                cartDB = FirebaseDatabase.getInstance().getReference("ShoppingCart").child(userID);
                cartDB.removeValue();

                DatabaseReference purchaseDB = FirebaseDatabase.getInstance().getReference("PurchaseHistory").child(userID);
                purchaseDB.setValue("£££");

                Toast.makeText(ShoppingCart.this, "Thank you for your purchase!", Toast.LENGTH_SHORT).show();


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
                .child("ShoppingCart").limitToLast(50);

        FirebaseRecyclerOptions<CartData> options =
                new FirebaseRecyclerOptions.Builder<CartData>()
                        .setQuery(query, CartData.class)
                        .build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<CartData, UserViewHolder>(options) {
            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.catalogue_layout, parent, false);

                return new UserViewHolder(view);


            }

            @Override
            protected void onBindViewHolder(UserViewHolder holder, int position, CartData model) {
                // Bind the Chat object to the ChatHolder
                holder.setTitle(model.title);
                holder.setManufacturer(model.manufacturer);
                holder.setPrice(model.price);

                //final String userID = getRef(position).getKey();

            }

        };
        recCart.setAdapter(adapter);
        adapter.startListening();
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }


        public void setTitle(String title) {
            TextView itemSingleTitle = (TextView) mView.findViewById(R.id.itemSingleTitle);
            itemSingleTitle.setText(title);
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
