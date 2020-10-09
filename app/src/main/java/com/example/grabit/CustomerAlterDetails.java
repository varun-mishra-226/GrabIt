package com.example.grabit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerAlterDetails extends AppCompatActivity {

    EditText etName, etMobile, etAge, etHeight, etWeight, etCalorieTarget;
    String name, phone, gender="";
    int age, height, weight, calorieTarget;
    Button btnMale, btnSubmit, btnFemale;
    DatabaseReference mDatabase;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_alter_details);

        etName = (EditText) findViewById(R.id.etName);
        etMobile = (EditText) findViewById(R.id.etPhone);
        etAge = (EditText) findViewById(R.id.etAge);
        etHeight = (EditText) findViewById(R.id.etHeight);
        etWeight = (EditText) findViewById(R.id.etWeight);
        etCalorieTarget = (EditText) findViewById(R.id.etCalorieTarget);
        btnMale = (Button) findViewById(R.id.btnMale);
        btnFemale = (Button) findViewById(R.id.btnFemale);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Customer");

        final Intent intent = getIntent();
        final String username = intent.getStringExtra("Username");

        btnMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "Male";
            }
        });

        btnFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "Female";
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etName.getText().toString().trim().isEmpty())
                    mDatabase.child(username).child("name").setValue(etName.getText().toString().trim());
                if (!etMobile.getText().toString().trim().isEmpty())
                    mDatabase.child(username).child("phone").setValue(etMobile.getText().toString().trim());
                if (!etHeight.getText().toString().isEmpty())
                    mDatabase.child(username).child("height").setValue(Integer.parseInt(etHeight.getText().toString()));
                if (!etWeight.getText().toString().isEmpty())
                    mDatabase.child(username).child("weight").setValue(Integer.parseInt(etWeight.getText().toString()));
                if (!etAge.getText().toString().isEmpty())
                    mDatabase.child(username).child("age").setValue(Integer.parseInt(etAge.getText().toString()));
                if (!gender.isEmpty())
                    mDatabase.child(username).child("gender").setValue(gender);
                if (!etCalorieTarget.getText().toString().isEmpty())
                    mDatabase.child(username).child("calorieTarget").setValue(Integer.parseInt(etCalorieTarget.getText().toString()));

                Intent intent1 = new Intent(CustomerAlterDetails.this, CustomerProfile.class);
                intent1.putExtra("Username", username);
                startActivity(intent1);
            }
        });
    }
}