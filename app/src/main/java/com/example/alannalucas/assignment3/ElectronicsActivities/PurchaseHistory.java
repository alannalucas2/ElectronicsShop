package com.example.alannalucas.assignment3.ElectronicsActivities;

import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.alannalucas.assignment3.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class PurchaseHistory extends AppCompatActivity {

    private RecyclerView mRecyclerCustomers;
    private FirebaseAuth mAuth;
    private DatabaseReference CustomerRef;
    private BottomNavigationView mBottomNav;
    private FirebaseAuth.AuthStateListener mAuthListener;
    View mView;
    private static final String TAG = "ItemsList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);
    }
}
