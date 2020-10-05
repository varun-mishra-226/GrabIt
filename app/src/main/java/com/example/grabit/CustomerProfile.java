package com.example.grabit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerProfile extends AppCompatActivity {

    Button btnHome, btnLogOut, btnChangePassword;
    EditText etPrevPassword, etNewPassword;
    DatabaseReference mDatabaseCustomer;
    FirebaseDatabase database;
    TextView tvName, tvRegistration, tvPhone, tvWallet, tvAge, tvHeight, tvWeight,
            tvGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        btnHome = (Button) findViewById(R.id.btnHome);
        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etPrevPassword = (EditText) findViewById(R.id.etPrevPassword);

        tvName = (TextView) findViewById(R.id.tvName);
        tvAge = (TextView) findViewById(R.id.tvAge);
        tvWallet = (TextView) findViewById(R.id.tvWallet);
        tvWeight = (TextView) findViewById(R.id.tvWeight);
        tvHeight = (TextView) findViewById(R.id.tvHeight);
        tvGender = (TextView) findViewById(R.id.tvGender);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvRegistration = (TextView) findViewById(R.id.tvRegistration);

        database = FirebaseDatabase.getInstance();
        mDatabaseCustomer = database.getReference("Customer");

        final Intent intent = getIntent();
        final String username = intent.getStringExtra("Username");

        mDatabaseCustomer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Customer customer = snapshot.child(username).getValue(Customer.class);

                tvName.setText("Name: " + customer.getName());
                tvAge.setText("Age: " + customer.getAge());
                tvGender.setText("Gender: " + customer.getGender());
                tvHeight.setText("Height: " + customer.getHeight() + " cms");
                tvWeight.setText("Weight: " + customer.getWeight() + " kgs");
                tvPhone.setText("Phone: " + customer.getPhone());
                tvRegistration.setText("Reg. No.: " + customer.getRegNo());
                tvWallet.setText("Wallet: Rs " + customer.getWallet());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CustomerProfile.this, CustomerHome.class);
                intent1.putExtra("Username", username);
                startActivity(intent1);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CustomerProfile.this, CutomerLogin.class);
                startActivity(intent1);
                finish();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etPrevPassword.getText().toString().trim().isEmpty()){
                    Log.i("Prev Password", etPrevPassword.getText().toString().trim());
                    mDatabaseCustomer.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Customer customer = snapshot.child(username).getValue(Customer.class);
                            String password = customer.getPassword();
                            Log.i("Database Password", password);
                            if (etPrevPassword.getText().toString().trim().equals(password)){
                                if (!etNewPassword.getText().toString().trim().isEmpty()){
                                    Log.i("New Password", etNewPassword.getText().toString().trim());
                                    mDatabaseCustomer.child(username).child("password").
                                            setValue(etNewPassword.getText().toString().trim());
                                    Toast.makeText(CustomerProfile.this, "Password changed successfully!!",
                                            Toast.LENGTH_SHORT).show();
                                    etNewPassword.setText("");
                                }
                                else{
                                    Toast.makeText(CustomerProfile.this, "Please enter the previous password!!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(CustomerProfile.this, "Entered previous password doesn't match!!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    Toast.makeText(CustomerProfile.this, "Please enter the previous password!!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
