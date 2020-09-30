package com.example.grabit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CustomerChosenCanteen extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    TextView tvUserDetails, tvCart;
    Button btnProfile, btnLogout, btnGoBack, btnContinue;
    DatabaseReference mDatabaseCustomer, mDatabaseCanteen;
    FirebaseDatabase database;
    List<FoodItem> menu = new ArrayList<>();
    ListView lvMenu;
    Hashtable<String, Integer> hm = new Hashtable<String, Integer>();
    ImageButton btnMic;
    FoodItemListAdapter foodItemListAdapter;
    SearchView searchView;

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
        btnMic = (ImageButton) findViewById(R.id.btnMic);
        searchView = (SearchView) findViewById(R.id.svMenu);
        tvCart = (TextView) findViewById(R.id.tvCart);

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

                foodItemListAdapter = new FoodItemListAdapter(CustomerChosenCanteen.this, R.layout.menu_food_item, menu);
                lvMenu.setAdapter(foodItemListAdapter);
                lvMenu.setTextFilterEnabled(true);

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        foodItemListAdapter.getFilter().filter(newText);
                        return false;
                    }
                });

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
                speak();
            }
        });
    }

    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Search for the item in the list");

        try{
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        }
        catch (Exception e){
            Toast.makeText(CustomerChosenCanteen.this, ""+e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
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
}
