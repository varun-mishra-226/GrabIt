package com.example.grabit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerHome extends AppCompatActivity {

    TextView tvUserDetails;
    Button btnProfile, btnLogout, btnTodayOrder, btnOrderHistory;
    DatabaseReference mDatabase;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        tvUserDetails = (TextView) findViewById(R.id.tvUserDetails);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnOrderHistory = (Button) findViewById(R.id.btnOrderHistory);
        btnTodayOrder = (Button) findViewById(R.id.btnTodayOrder);
        btnProfile = (Button) findViewById(R.id.btnProfile);
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Customer");

        Intent intent = getIntent();
        String username = intent.getStringExtra("Username");

        
    }
}
