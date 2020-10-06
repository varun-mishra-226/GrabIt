package com.example.grabit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CustomerChosenCanteen extends AppCompatActivity
        implements SearchView.OnQueryTextListener{

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    TextView tvUserDetails, tvCart;
    Button btnProfile, btnLogout, btnGoBack, btnContinue;
    SearchView svMenu;
    DatabaseReference mDatabaseCustomer, mDatabaseCanteen, mDatabaseOrder;
    FirebaseDatabase database;
    List<FoodItem> menu = new ArrayList<>();
    ListView lvMenu;
    Hashtable<String, Integer> hm = new Hashtable<String, Integer>();
    Hashtable<String, Integer> itemPrice = new Hashtable<String, Integer>();
    ImageButton btnMic;
    FoodItemListAdapter foodItemListAdapter;
    Calendar calendar;
    int totalBill=0;
    int wallet;

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
        mDatabaseOrder = database.getReference("Order");
        lvMenu = (ListView) findViewById(R.id.lvMenu);
        btnMic = (ImageButton) findViewById(R.id.btnMic);
        tvCart = (TextView) findViewById(R.id.tvCart);
        svMenu = (SearchView) findViewById(R.id.svMenu);

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
                wallet = customer.getWallet();
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
                    itemPrice.put(foodItem.getName(), foodItem.getPrice());
                    menu.add(foodItem);
                }

                for (int i=0; i<menu.size(); i++){
                    Log.i("Menu Item Name", menu.get(i).getName());
                    Log.i("Menu Calorie Name", String.valueOf(menu.get(i).getCalorie()));
                }

                foodItemListAdapter = new FoodItemListAdapter(CustomerChosenCanteen.this, menu);
                lvMenu.setAdapter(foodItemListAdapter);
                lvMenu.setTextFilterEnabled(true);

                lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent1 = new Intent(CustomerChosenCanteen.this, CustomerChosenFoodItem.class);
                        intent1.putExtra("Username", username);
                        intent1.putExtra("chosenCanteen", chosenCanteen);
                        intent1.putExtra("chosenItem", menu.get(position).getName());
                        startActivityForResult(intent1, 1);
                    }
                });

                lvMenu.setTextFilterEnabled(true);
                setupSearchView();
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
                for (Map.Entry<String, Integer> entry : hm.entrySet())
                    Log.i("HashTable" , entry.getKey() + entry.getValue());

                calendar = Calendar.getInstance();
                Log.i("Time:", String.valueOf(calendar.getTimeInMillis()));

                int i=1;
                for (Map.Entry<String, Integer> entry : hm.entrySet()){
                    if (entry.getValue()>0){
                        mDatabaseOrder.child(username).child(String.valueOf(calendar.getTimeInMillis())).child("Item"+i).child("orderItem").setValue(entry.getKey());
                        mDatabaseOrder.child(username).child(String.valueOf(calendar.getTimeInMillis())).child("Item"+i).child("orderQuantity").setValue(entry.getValue());
                        i++;
                    }
                }

                if (wallet>totalBill){
                    mDatabaseCustomer.child(username).child("wallet").setValue(wallet-totalBill);
                    hm.clear();
                    tvCart.setText("");
                }

                Toast.makeText(CustomerChosenCanteen.this, "Order successfully processed!!", Toast.LENGTH_LONG).show();
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

    private void setupSearchView() {
        svMenu.setIconifiedByDefault(false);
        svMenu.setOnQueryTextListener(CustomerChosenCanteen.this);
        svMenu.setSubmitButtonEnabled(true);
        svMenu.setQueryHint("Search here...");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==1){
            hm.put(data.getStringExtra("chosenItem"), Integer.parseInt(data.getStringExtra("Quantity")));
            String cartItem = "";
            for (Map.Entry<String, Integer> entry : hm.entrySet()){
                if (entry.getValue()>0){
                    cartItem += entry.getKey() + " " + entry.getValue() + "\n";
                    totalBill += entry.getValue()*itemPrice.get(entry.getKey());
                }
            }
            tvCart.setText(cartItem);
        }
        else if (resultCode==REQUEST_CODE_SPEECH_INPUT){
            if (resultCode==RESULT_OK && null!=data){
                ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                Log.i("Voice Recognition", res.get(0));
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            lvMenu.clearTextFilter();
        } else {
            lvMenu.setFilterText(newText);
        }
        return true;
    }
}
