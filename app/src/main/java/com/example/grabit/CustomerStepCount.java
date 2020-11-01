package com.example.grabit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class CustomerStepCount extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    TextView tvUserDetails, tvCurrentSteps, tvStepsLeft;
    Sensor sensor;
    double magnitudePrevious = 0;
    Integer stepCount = 0;
    Button btnProfile, btnHome, btnLogout;
    DatabaseReference mDatabaseCustomer;
    FirebaseDatabase database;
    String username;
    int targetSteps=1, progress;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_step_count);

        tvUserDetails = (TextView) findViewById(R.id.tvUserDetails);
        tvStepsLeft = (TextView) findViewById(R.id.tvStepsLeft);
        tvCurrentSteps = (TextView) findViewById(R.id.tvCurrentSteps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        btnHome = (Button) findViewById(R.id.btnHome);
        btnProfile = (Button) findViewById(R.id.btnProfile);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        database = FirebaseDatabase.getInstance();
        mDatabaseCustomer = database.getReference("Customer");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Intent intent = getIntent();
        username = intent.getStringExtra("Username");

        schedAlarm(this);

        mDatabaseCustomer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Customer customer = snapshot.child(username).getValue(Customer.class);
                String name = customer.getName();
                String reg = customer.getRegNo();
                int wallet = customer.getWallet();
                tvUserDetails.setText(name + "\n" + reg + "\nWallet: " + wallet);
                tvUserDetails.setMovementMethod(new ScrollingMovementMethod());
                targetSteps = (int) (customer.getCalorieTarget()/(0.04));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CustomerStepCount.this, CustomerHome.class);
                intent1.putExtra("Username", username);
                startActivity(intent1);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CustomerStepCount.this, CustomerProfile.class);
                intent1.putExtra("Username", username);
                startActivity(intent1);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CustomerStepCount.this, CutomerLogin.class);
                startActivity(intent1);
                finish();
            }
        });

        Log.i("Step Counter Running", "Step Counter Activity");

        final SensorEventListener sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event!=null){
                    float xAcc = event.values[0];
                    float yAcc = event.values[1];
                    float zAcc = event.values[2];

                    double mag = Math.sqrt(xAcc*xAcc + yAcc*yAcc + zAcc*zAcc);
                    double magnitudeDelta = mag - magnitudePrevious;
                    magnitudePrevious = mag;

                    if (magnitudeDelta > 8 ){
                        stepCount++;
                    }
                    tvCurrentSteps.setText(stepCount.toString() + "\n" + "STEPS");
                    int res = (targetSteps-stepCount);
                    tvStepsLeft.setText( res + " STEPS TO GO");
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void updateValue(int sC){
        mDatabaseCustomer.child(username).child("currentSteps").setValue(sC);
    }

    private void schedAlarm(Context context) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0,
                new Intent(context, YourBroadcastReceiver.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000*60*60*24, pi);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        stepCount = sharedPreferences.getInt("stepCount", 0);
        Log.i("CurrentStepsOnResume", String.valueOf(stepCount));
        updateValue(stepCount);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount", stepCount);
        editor.apply();
        Log.i("CurrentStepsOnPause", String.valueOf(stepCount));
        updateValue(stepCount);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount", stepCount);
        editor.apply();
        Log.i("CurrentStepsOnStop", String.valueOf(stepCount));
        updateValue(stepCount);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}