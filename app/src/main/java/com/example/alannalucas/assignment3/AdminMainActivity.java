package com.example.alannalucas.assignment3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.alannalucas.assignment3.ElectronicsActivities.AddElectronics;
import com.example.alannalucas.assignment3.ElectronicsActivities.Catalogue;
import com.example.alannalucas.assignment3.ElectronicsActivities.CustomerProfiles;
import com.example.alannalucas.assignment3.LoginActivities.ChooseActivity;
import com.google.firebase.auth.FirebaseAuth;


public class AdminMainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private BottomNavigationView mBottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);



        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);


        return true;
    }

    public void viewCustomer(View view) {

        Intent intent3 = new Intent(this, CustomerProfiles.class);
        this.startActivity(intent3);

    }

    public void addElectonics(View view) {
        Intent intent = new Intent(AdminMainActivity.this, AddElectronics.class);
        startActivity(intent);
        finish();
    }

    public void logoutUser(View view){
        mAuth.signOut();
        Intent intent = new Intent(AdminMainActivity.this, ChooseActivity.class);
        startActivity(intent);
        finish();
        return;

    }



}






