package com.example.grabit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerChosenFoodItem extends AppCompatActivity {

    TextView tvUserDetails, tvFoodDetails;
    Button btnProfile, btnLogout, btnContinue;
    EditText etQuantity;
    DatabaseReference mDatabaseCanteen, mDatabaseCustomer;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_chosen_food_item);

        tvUserDetails = (TextView) findViewById(R.id.tvUserDetails);
        tvFoodDetails = (TextView) findViewById(R.id.tvFoodDetails);
        etQuantity = (EditText) findViewById(R.id.etQuantity);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnContinue = (Button) findViewById(R.id.btnContinue);
        btnProfile = (Button) findViewById(R.id.btnProfile);
        database = FirebaseDatabase.getInstance();
        mDatabaseCustomer = database.getReference("Customer");

        Intent intent = getIntent();
        final String username = intent.getStringExtra("Username");
        final String chosenCanteen = intent.getStringExtra("chosenCanteen");
        final String chosenItem = intent.getStringExtra("chosenItem");

        mDatabaseCustomer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Customer customer = snapshot.child(username).getValue(Customer.class);
                String name = customer.getName();
                String reg = customer.getRegNo();
                int wallet = customer.getWallet();
                tvUserDetails.setText(name + "\n" + reg + "\nWallet: " + wallet);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mDatabaseCanteen = database.getReference("/Canteen/" + chosenCanteen + "/Menu" + chosenItem);
    }
}