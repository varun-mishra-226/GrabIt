package com.example.grabit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CustomerTodayOrder extends AppCompatActivity {

    TextView tvUserDetails;
    Button btnProfile, btnLogout, btnGoBack;
    DatabaseReference mDatabaseCustomer, mDatabaseCanteen, mDatabaseOrder;
    FirebaseDatabase database;
    List<OrderedItemsDetail> orderedItemsDetailArrayList = new ArrayList<>();
    ListView lvOrders;
    Calendar calender;
    DisplayOrderAdapter displayOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_today_order);

        tvUserDetails = (TextView) findViewById(R.id.tvUserDetails);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnGoBack = (Button) findViewById(R.id.btnGoBack);
        btnProfile = (Button) findViewById(R.id.btnProfile);
        database = FirebaseDatabase.getInstance();
        lvOrders = (ListView) findViewById(R.id.lvOrders);
        mDatabaseCustomer = database.getReference("Customer");
        mDatabaseCanteen = database.getReference("Canteen");

        Intent intent = getIntent();
        final String username = intent.getStringExtra("Username");

        mDatabaseOrder = database.getReference("/Order/" + username);

        mDatabaseCustomer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Customer customer = snapshot.child(username).getValue(Customer.class);
                String name = customer.getName();
                String reg = customer.getRegNo();
                int wallet = customer.getWallet();
                tvUserDetails.setText(name + "\n" + reg + "\nWallet: " + wallet);
                tvUserDetails.setMovementMethod(new ScrollingMovementMethod());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mDatabaseOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderedItemsDetailArrayList.clear();
                List<String> keys = new ArrayList<>();
                List<String> keys1 = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren()){
                    keys.add(keyNode.getKey());

                    calender = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

                    String CurrentDate = df.format(calender.getTime());
                    //Log.i("TimeStamp", CurrentDate);

                    calender.setTimeInMillis(Long.parseLong(keyNode.getKey()));
                    //Log.i("TimeStamp", String.valueOf(calender.getTime()));
                    String formattedDate = df.format(calender.getTime());
                    //Log.i("TimeStamp", formattedDate);

                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

                    if (CurrentDate.equals(formattedDate)){
                        for (DataSnapshot keyNode1 : keyNode.getChildren()){
                            keys1.add(keyNode1.getKey());

                            Log.i("OrderedItems", keyNode1.getKey());
                            OrderedItems orderedItems = keyNode1.getValue(OrderedItems.class);
                            OrderedItemsDetail orderedItemsDetail = new OrderedItemsDetail();
                            orderedItemsDetail.setOrderItem(orderedItems.getOrderItem());
                            orderedItemsDetail.setOrderQuantity((orderedItems.getOrderQuantity()));
                            orderedItemsDetail.setOrderDate(timeFormat.format(calender.getTime()));

                            orderedItemsDetailArrayList.add(orderedItemsDetail);
                        }
                    }
                }

                for (int i=0; i<orderedItemsDetailArrayList.size(); i++){
                    Log.i("OrderedItemDetails",
                            orderedItemsDetailArrayList.get(i).getOrderItem() + " " +
                            orderedItemsDetailArrayList.get(i).getOrderQuantity() + " " +
                            orderedItemsDetailArrayList.get(i).getOrderDate());
                }

                displayOrderAdapter = new DisplayOrderAdapter(CustomerTodayOrder.this,
                        R.layout.order_item_details, orderedItemsDetailArrayList);
                lvOrders.setAdapter(displayOrderAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CustomerTodayOrder.this, CustomerProfile.class);
                intent1.putExtra("Username", username);
                startActivity(intent1);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CustomerTodayOrder.this, CutomerLogin.class);
                startActivity(intent1);
                finish();
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CustomerTodayOrder.this, CustomerHome.class);
                intent1.putExtra("Username", username);
                startActivity(intent1);
            }
        });
    }
}
