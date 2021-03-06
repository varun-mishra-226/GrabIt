package com.example.grabit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

    Button btnHome, btnLogOut, btnChangePassword, btnUpdateDetails;
    EditText etPrevPassword, etNewPassword;
    DatabaseReference mDatabaseCustomer;
    FirebaseDatabase database;
    TextView tvName, tvRegistration, tvPhone, tvWallet, tvAge, tvHeight, tvWeight,
            tvGender, tvCurrentProgress, tvCurrentProgress1, tvBurnTargetRatio, tvIntakeTargetRatio;
    ProgressBar progressBar, progressBar1;
    int target;
    float currentSteps, progress, progress1, currentIntake, calorieIntake;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        btnHome = (Button) findViewById(R.id.btnHome);
        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);
        btnUpdateDetails = (Button) findViewById(R.id.btnUpdateDetails);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etPrevPassword = (EditText) findViewById(R.id.etPrevPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        sharedPreferences = getPreferences(MODE_PRIVATE);

        tvName = (TextView) findViewById(R.id.tvName);
        tvAge = (TextView) findViewById(R.id.tvAge);
        tvWallet = (TextView) findViewById(R.id.tvWallet);
        tvWeight = (TextView) findViewById(R.id.tvWeight);
        tvHeight = (TextView) findViewById(R.id.tvHeight);
        tvGender = (TextView) findViewById(R.id.tvGender);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvRegistration = (TextView) findViewById(R.id.tvRegistration);
        tvCurrentProgress = (TextView) findViewById(R.id.tvCurrentProgress);
        tvCurrentProgress1 = (TextView) findViewById(R.id.tvCurrentProgress1);
        tvBurnTargetRatio = (TextView) findViewById(R.id.tvBurnTargetRatio);
        tvIntakeTargetRatio = (TextView) findViewById(R.id.tvIntakeTargetRatio);

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

                currentSteps = customer.getCurrentSteps();
//                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
//                currentSteps = sharedPreferences.getInt("stepCount", 100);
                Log.i("CurrentStepsProfile", String.valueOf(currentSteps));
                currentIntake = customer.getCurrentCalorieTaken();
//                currentIntake = sharedPreferences.getInt("dailyCalorieIntake", 0);
                Log.i("CurrentIntakeProfile", String.valueOf(currentIntake));
                calorieIntake = customer.getCalorieIntake();
                target = customer.getCalorieTarget();

                progress = (float) ((currentSteps*0.04)/target)*100;
                progress1 = currentIntake/calorieIntake*100;
                Log.i("CurrentProgress", String.valueOf(progress));
                Log.i("CurrentProgress1", String.valueOf(progress1));
                if (progress>100)
                    progress = 100;
                if (progress1>100)
                    progress1 = 100;

                progressBar.setProgress((int) progress);
                progressBar1.setProgress((int) progress1);
                tvCurrentProgress.setText(String.valueOf((int) progress) + "%");
                tvCurrentProgress1.setText(String.valueOf((int) progress1) + "%");

                int cS = (int) (currentSteps*0.04), t = target, cI = (int) currentIntake, caI = (int) calorieIntake;

                tvBurnTargetRatio.setText(cS + "/" + t + " cal");
                tvIntakeTargetRatio.setText(cI + "/" + caI + " cal");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        progress = (float) ((currentSteps*0.04)/target)*100;
//        progress1 = currentIntake/calorieIntake*100;
//        Log.i("CurrentProgress", String.valueOf(progress));
//
//        progressBar.setProgress((int) progress);
//        progressBar1.setProgress((int) progress1);
//        tvCurrentProgress.setText(String.valueOf((int) progress) + "%");
//        tvCurrentProgress1.setText(String.valueOf((int) progress1) + "%");

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

        btnUpdateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CustomerProfile.this, CustomerAlterDetails.class);
                intent1.putExtra("Username", username);
                startActivity(intent1);
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
