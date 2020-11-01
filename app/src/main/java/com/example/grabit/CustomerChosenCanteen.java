package com.example.grabit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CustomerChosenCanteen extends AppCompatActivity
        implements SearchView.OnQueryTextListener{

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000, REQ_FOOD_ITEM = 500;
    TextView tvUserDetails;
    Button btnProfile, btnLogout, btnGoBack, btnContinue;
    SearchView svMenu;
    DatabaseReference mDatabaseCustomer, mDatabaseCanteen, mDatabaseOrder;
    FirebaseDatabase database;
    List<FoodItem> menu = new ArrayList<>();
    List<FoodItem> orders = new ArrayList<>();
    ListView lvMenu;
    Hashtable<String, Integer> hm = new Hashtable<String, Integer>();
    Hashtable<String, Integer> itemPrice = new Hashtable<String, Integer>();
    Hashtable<String, Integer> calorieContent = new Hashtable<String, Integer>();
    ImageButton btnMic;
    FoodItemListAdapter foodItemListAdapter;
    Calendar calendar;
    int totalBill=0, totalIntake=0, currentIntake=0, targetIntake;
    int wallet;
    String query;
    SharedPreferences sharedPreferences;

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
        svMenu = (SearchView) findViewById(R.id.svMenu);
        sharedPreferences = getPreferences(MODE_PRIVATE);

        final Intent intent = getIntent();
        final String username = intent.getStringExtra("Username");
        final String chosenCanteen = intent.getStringExtra("chosenCanteen");

        mDatabaseCanteen = database.getReference("/Canteen/" + chosenCanteen + "/Menu");

        // To add header details
        mDatabaseCustomer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Customer customer = snapshot.child(username).getValue(Customer.class);
                String name = customer.getName();
                String reg = customer.getRegNo();
                wallet = customer.getWallet();
                currentIntake = customer.getCurrentCalorieTaken();
                targetIntake = customer.getCalorieIntake();
                tvUserDetails.setText(name + "\n" + reg + "\nWallet: " + wallet);
                tvUserDetails.setMovementMethod(new ScrollingMovementMethod());
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
                    calorieContent.put(foodItem.getName(), foodItem.getCalorie());
                    menu.add(foodItem);
                }

                foodItemListAdapter = new FoodItemListAdapter(CustomerChosenCanteen.this, menu, targetIntake, currentIntake);
                lvMenu.setAdapter(foodItemListAdapter);
                lvMenu.setTextFilterEnabled(true);

                lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.i("ClickPosition", String.valueOf(position));
                        Intent intent1 = new Intent(CustomerChosenCanteen.this, FoodItemPopup.class);
                        intent1.putExtra("chosenCanteen", chosenCanteen);
                        intent1.putExtra("chosenItem", menu.get(position).getName());
                        intent1.putExtra("possibleIntake", targetIntake-currentIntake);
                        intent1.putExtra("calorieItem", menu.get(position).getCalorie());
                        startActivity(intent1);
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
                new AlertDialog.Builder(CustomerChosenCanteen.this)
                        .setTitle("Confirm Order")
                        .setMessage("Do you want to proceed with the order...?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                placeOrder();

                                for (Map.Entry<String, Integer> entry : hm.entrySet()){
                                    if (entry.getValue()>0){
                                        totalBill += entry.getValue()*itemPrice.get(entry.getKey());
                                        totalIntake += entry.getValue()*calorieContent.get(entry.getKey());
                                    }
                                }

                                calendar = Calendar.getInstance();
                                Log.i("Time:", String.valueOf(calendar.getTimeInMillis()));

                                if (wallet>=totalBill){
                                    mDatabaseCustomer.child(username).child("currentCalorieTaken").setValue(totalIntake+currentIntake);
                                    int i=1;
                                    for (Map.Entry<String, Integer> entry : hm.entrySet()){
                                        if (entry.getValue()>0){
                                            mDatabaseOrder.child(username).child(String.valueOf(calendar.getTimeInMillis())).child("Item"+i).child("orderItem").setValue(entry.getKey());
                                            mDatabaseOrder.child(username).child(String.valueOf(calendar.getTimeInMillis())).child("Item"+i).child("orderQuantity").setValue(entry.getValue());
                                            i++;
                                        }
                                    }
                                    Toast.makeText(CustomerChosenCanteen.this, "Order successfully processed!!", Toast.LENGTH_SHORT).show();
                                    hm.clear();
                                    mDatabaseCustomer.child(username).child("wallet").setValue(wallet-totalBill);
                                }else{
                                    Toast.makeText(CustomerChosenCanteen.this, "Order cannot be processed!! (Not enough CREDITS!!)", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
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

        btnMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query = "";
                speak();
            }
        });
    }

    private void placeOrder() {
        orders.clear();
        for(int i=0; i<foodItemListAdapter.menuList.size(); i++) {
            if(foodItemListAdapter.menuList.get(i).quantity>0){
                hm.put(foodItemListAdapter.menuList.get(i).name, foodItemListAdapter.menuList.get(i).quantity);
                Log.i("ItemOrdered", foodItemListAdapter.menuList.get(i).name + " " + foodItemListAdapter.menuList.get(i).quantity);
            }
        }
    }

    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Search for item...");
        Log.i("VoiceRecognition", "Initial Msg + query: " + query);

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
            Log.i("VoiceRecognition", "Inside Msg + query: " + query);
            Log.i("VoiceRecognition", "Q: " + query);
        }
        catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Log.i("VoiceRecognition", "Final Msg  + query: " + query);
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
        switch (requestCode){
            case REQ_FOOD_ITEM:{
                if (resultCode==1){
                    hm.put(data.getStringExtra("chosenItem"), Integer.parseInt(data.getStringExtra("Quantity")));
                    String cartItem = "Item Name\n";
                    String cartItemQuantity = "Quantity\n";
                    String cartItemPrice = "Price\n";
                    for (Map.Entry<String, Integer> entry : hm.entrySet()){
                        if (entry.getValue()>0){
                            cartItem += entry.getKey() + "\n";
                            cartItemPrice += entry.getValue()*itemPrice.get(entry.getKey()) + "\n";
                            cartItemQuantity += entry.getValue() + "\n";
                            totalBill += entry.getValue()*itemPrice.get(entry.getKey());
                        }
                    }
                }
            }
            case REQUEST_CODE_SPEECH_INPUT:{
                if (resultCode==RESULT_OK && null!=data){
                    ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.i("VoiceRecognition", "Inside Activity Result  + query: " + query);
                    query = res.get(0);
                    Log.i("VoiceRecognition", "Inside " + query);
                    svMenu.setQuery(query, false);
                }
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        FoodItemListAdapter adapter = (FoodItemListAdapter) lvMenu.getAdapter();

        if (TextUtils.isEmpty(newText)) {
            //lvMenu.clearTextFilter();
            adapter.getFilter().filter(null);
        } else {
            //lvMenu.setFilterText(newText);
            adapter.getFilter().filter(newText);
        }
        return true;
    }
}
