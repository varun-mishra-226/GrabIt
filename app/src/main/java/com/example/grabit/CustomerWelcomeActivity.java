package com.example.grabit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerWelcomeActivity extends AppCompatActivity {

    EditText etName, etMobile, etAge, etHeight, etWeight, etCalorieTarget, etCalorieTarget1;
    String name, phone, gender="";
    int age, height, weight, calorieTarget, calorieIntake;
    Button btnMale, btnSubmit, btnFemale;
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    boolean click = false;
    RadioGroup rgGender;
    RadioButton rbGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_welcome);

        etName = (EditText) findViewById(R.id.etName);
        etMobile = (EditText) findViewById(R.id.etPhone);
        etAge = (EditText) findViewById(R.id.etAge);
        etHeight = (EditText) findViewById(R.id.etHeight);
        etWeight = (EditText) findViewById(R.id.etWeight);
        etCalorieTarget = (EditText) findViewById(R.id.etCalorieTarget);
        etCalorieTarget1 = (EditText) findViewById(R.id.etCalorieTarget1);
//        btnMale = (Button) findViewById(R.id.btnMale);
//        btnFemale = (Button) findViewById(R.id.btnFemale);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        rgGender = (RadioGroup) findViewById(R.id.rgGender);


        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Customer");

        final Intent intent = getIntent();
        final String username = intent.getStringExtra("Username");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(username).child("calorieTarget").exists()){
                    Intent intent1 = new Intent(CustomerWelcomeActivity.this, CustomerHome.class);
                    intent1.putExtra("Username", username);
                    startActivity(intent1);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        btnMale.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!click) {
//                    click = true;
//                    gender = "Male";
//                    btnMale.setBackgroundResource(R.drawable.button_highlight_layout);
//                }
//            }
//        });
//
//        btnFemale.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!click) {
//                    click = true;
//                    gender = "Female";
//                    btnFemale.setBackgroundResource(R.drawable.button_highlight_layout);
//                }
//            }
//        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().trim().isEmpty() || etMobile.getText().toString().trim().isEmpty() ||
                        gender.isEmpty() || etHeight.getText().toString().isEmpty() ||
                        etAge.getText().toString().isEmpty() || etWeight.getText().toString().isEmpty() ||
                        etCalorieTarget.getText().toString().isEmpty() || etCalorieTarget1.getText().toString().isEmpty()){
                    Toast.makeText(CustomerWelcomeActivity.this, "Please enter all fields to proceed!!",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    name = etName.getText().toString().trim();
                    phone = etMobile.getText().toString().trim();
                    height = Integer.parseInt(etHeight.getText().toString());
                    weight = Integer.parseInt(etWeight.getText().toString());
                    age = Integer.parseInt(etAge.getText().toString());
                    calorieTarget = Integer.parseInt(etCalorieTarget.getText().toString());
                    calorieIntake = Integer.parseInt(etCalorieTarget1.getText().toString());

                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(username).child("calorieTarget").exists()){
                                Intent intent1 = new Intent(CustomerWelcomeActivity.this, CustomerHome.class);
                                intent1.putExtra("Username", username);
                                startActivity(intent1);
                            }
                            else{
                                mDatabase.child(username).child("name").setValue(name);
                                mDatabase.child(username).child("phone").setValue(phone);
                                mDatabase.child(username).child("height").setValue(height);
                                mDatabase.child(username).child("weight").setValue(weight);
                                mDatabase.child(username).child("age").setValue(age);
                                mDatabase.child(username).child("gender").setValue(gender);
                                mDatabase.child(username).child("calorieTarget").setValue(calorieTarget);
                                mDatabase.child(username).child("calorieIntake").setValue(calorieIntake);

                                Intent intent1 = new Intent(CustomerWelcomeActivity.this, CustomerHome.class);
                                intent1.putExtra("Username", username);
                                startActivity(intent1);
                            }
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CustomerWelcomeActivity.this, CutomerLogin.class);
        startActivity(intent);
    }

    public void genderSelect(View view) {
        int radioId = rgGender.getCheckedRadioButtonId();
        rbGender = findViewById(radioId);
        gender = (String) rbGender.getText();
    }
}