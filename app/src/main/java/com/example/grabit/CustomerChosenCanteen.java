package com.example.grabit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerChosenCanteen extends AppCompatActivity {

    TextView tvUserDetails;
    Button btnProfile, btnLogout, btnGoBack, btnContinue;
    DatabaseReference mDatabaseCustomer, mDatabaseCanteen;
    FirebaseDatabase database;
    List<FoodItem> menu = new ArrayList<>();
    ListView lvMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_chosen_canteen);

        tvUserDetails = (TextView) findViewById(R.id.tvUserDetails);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnGoBack = (Button) findViewById(R.id.btnGoBack);
        btnContinue = (Button) findViewById(R.id.btnContinue);
        btnProfile = (Button) findViewById(R.id.btnProfile);
        database = FirebaseDatabase.getInstance();
        mDatabaseCustomer = database.getReference("Customer");
        lvMenu = (ListView) findViewById(R.id.lvMenu);

        final Intent intent = getIntent();
        final String username = intent.getStringExtra("Username");
        final String chosenCanteen = intent.getStringExtra("chosenCanteen");

        mDatabaseCanteen = database.getReference("/Canteen/" + chosenCanteen + "/Menu");

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
                menu.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    FoodItem foodItem = keyNode.getValue(FoodItem.class);
                    menu.add(foodItem);
                }

                for (int i=0; i<menu.size(); i++){
                    Log.i("Menu Item Name", menu.get(i).getName());
                    Log.i("Menu Calorie Name", String.valueOf(menu.get(i).getCalorie()));
                }

                FoodItemListAdapter foodItemListAdapter = new FoodItemListAdapter(CustomerChosenCanteen.this, R.layout.menu_food_item, menu);
                lvMenu.setAdapter(foodItemListAdapter);

                lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent1 = new Intent(CustomerChosenCanteen.this, CustomerChosenFoodItem.class);
                        intent1.putExtra("username", username);
                        intent1.putExtra("chosenCanteen", chosenCanteen);
                        intent1.putExtra("chosenItem", menu.get(position).getName());
                        startActivityForResult(intent1, );
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
                Intent intent1 = new Intent(CustomerChosenCanteen.this, CustomerProfile.class);
                intent1.putExtra("Username", username);
                startActivity(intent1);
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0; i<menu.size(); i++){

                }
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CustomerChosenCanteen.this, CustomerHome.class);
                intent1.putExtra("Username", username);
                startActivity(intent1);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CustomerChosenCanteen.this, CutomerLogin.class);
                startActivity(intent1);
                finish();
            }
        });
    }
}
