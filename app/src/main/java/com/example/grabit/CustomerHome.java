package com.example.grabit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CustomerHome extends AppCompatActivity {

    TextView tvUserDetails;
    Button btnProfile, btnLogout, btnTodayOrder, btnFitnessTracker;
    DatabaseReference mDatabaseCustomer, mDatabaseCanteen;
    FirebaseDatabase database;
    List<String> canteen = new ArrayList<>();
    ListView lvCanteen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        schedAlarm(this);

        tvUserDetails = (TextView) findViewById(R.id.tvUserDetails);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnFitnessTracker = (Button) findViewById(R.id.btnFitnessTracker);
        btnTodayOrder = (Button) findViewById(R.id.btnTodayOrder);
        btnProfile = (Button) findViewById(R.id.btnProfile);
        database = FirebaseDatabase.getInstance();
        lvCanteen = (ListView) findViewById(R.id.lvCanteen);
        mDatabaseCustomer = database.getReference("Customer");
        mDatabaseCanteen = database.getReference("Canteen");

        Intent intent = getIntent();
        final String username = intent.getStringExtra("Username");

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

        mDatabaseCanteen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyNode : snapshot.getChildren()){
                    canteen.add(keyNode.getKey());
                }

                for (int i=0; i<canteen.size(); i++)
                    Log.i("CANTEEN LIST", canteen.get(i));

                ArrayAdapter<String> myAdapter = new ArrayAdapter<>(CustomerHome.this, android.R.layout.simple_list_item_1, canteen);
                lvCanteen.setAdapter(myAdapter);
                lvCanteen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.i("CANTEEN LIST", canteen.get(position));
                        Intent intent1 = new Intent(CustomerHome.this, CustomerChosenCanteen.class);
                        intent1.putExtra("chosenCanteen", canteen.get(position));
                        intent1.putExtra("Username", username);
                        startActivity(intent1);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CustomerHome.this, CustomerProfile.class);
                intent1.putExtra("Username", username);
                startActivity(intent1);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CustomerHome.this, CutomerLogin.class);
                startActivity(intent1);
                finish();
            }
        });

        btnTodayOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CustomerHome.this, CustomerTodayOrder.class);
                intent1.putExtra("Username", username);
                startActivity(intent1);
            }
        });

        btnFitnessTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CustomerHome.this, CustomerStepCount.class);
                intent1.putExtra("Username", username);
                startActivity(intent1);
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    private void schedAlarm(Context context) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 18);
        cal.set(Calendar.MINUTE, 49);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0,
                new Intent(context, YourBroadcastReceiver.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000*60*60*24, pi);
    }
}