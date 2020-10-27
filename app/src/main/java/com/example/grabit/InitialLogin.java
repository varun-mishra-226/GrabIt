package com.example.grabit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.grabit.CustomerHome;
import com.example.grabit.R;

public class InitialLogin extends AppCompatActivity {

    TextView tvInitialLogin;
    Button btnChef, btnCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_login);

        btnChef = (Button) findViewById(R.id.btnChef);
        btnCustomer = (Button) findViewById(R.id.btnCustomer);

        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InitialLogin.this, CutomerLogin.class);
                startActivity(intent);
            }
        });

//        btnChef.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(InitialLogin.this, EmployeeHome.class);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onBackPressed() {
    }
}
