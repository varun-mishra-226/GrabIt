package com.example.grabit;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StepCountService extends Service {

    SensorManager sensorManager;
    Sensor sensor;
    double magnitudePrevious = 0;
    Integer stepCount = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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

                    if (magnitudeDelta > 6){
                        stepCount++;
                    }
                    //tvStepCount.setText(stepCount.toString());
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
