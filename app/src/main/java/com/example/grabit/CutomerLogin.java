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

public class CutomerLogin extends AppCompatActivity {

    EditText etPassword, etUsername;
    Button btnSubmit;
    DatabaseReference mDatabase;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutomer_login);

        etPassword = (EditText) findViewById(R.id.etPassword);
        etUsername = (EditText) findViewById(R.id.etUsername);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Customer");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUsername.getText().toString().isEmpty() ||
                        etPassword.getText().toString().trim().isEmpty()){
                    Toast.makeText(CutomerLogin.this, "Please enter all the fields!!",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.i("Submit Select", "Submit button pressed");
                    signIn(etUsername.getText().toString(),
                            etPassword.getText().toString().trim());
                }
            }
        });
    }

    private void signIn(final String username, final String password) {
        Log.i("Function Entered", "Entered the validation function");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("Value Event Listener", "Entered value event listener");
                if (snapshot.child(username).exists()){
                    Log.i("Email Search", "Entered Email Found");
                    Customer customer = snapshot.child(username).getValue(Customer.class);
                    if (customer.getPassword().equals(password)){
                        Intent intent = new Intent(CutomerLogin.this, CustomerWelcomeActivity.class);
                        intent.putExtra("Username", username);
                        startActivity(intent);

                        if (snapshot.child(username).child("calorieTarget").exists()){
                            Intent intent1 = new Intent(CutomerLogin.this, CustomerHome.class);
                            intent1.putExtra("Username", username);
                            startActivity(intent1);
                        }
                        else{
                            Intent intent1 = new Intent(CutomerLogin.this, CustomerWelcomeActivity.class);
                            intent1.putExtra("Username", username);
                            startActivity(intent1);
                        }
                    }
                    else {
                        Toast.makeText(CutomerLogin.this, "Wrong Password!!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(CutomerLogin.this, "Username does not exist!!",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
