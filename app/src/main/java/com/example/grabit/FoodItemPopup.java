package com.example.grabit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FoodItemPopup extends Activity {

    ImageView ivFoodItemImage;
    TextView tvFoodItemName, tvCalorie, tvCarbohydrate, tvFat, tvProtein;
    DatabaseReference mDatabaseCanteen, mDatabaseCustomer;
    FirebaseDatabase database;

    public class CartoonImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection myConnection = (HttpURLConnection) url.openConnection();
                myConnection.connect();
                InputStream in = myConnection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_popup);

        ivFoodItemImage = (ImageView) findViewById(R.id.ivFoodItemImage);
        tvFoodItemName = (TextView) findViewById(R.id.tvFoodItemName);
        tvCalorie = (TextView) findViewById(R.id.tvCalorie);
        tvCarbohydrate = (TextView) findViewById(R.id.tvCarbohydrate);
        tvFat = (TextView) findViewById(R.id.tvFat);
        tvProtein = (TextView) findViewById(R.id.tvProtein);
        database = FirebaseDatabase.getInstance();
        mDatabaseCustomer = database.getReference("Customer");

        Intent intent = getIntent();
        final String chosenCanteen = intent.getStringExtra("chosenCanteen");
        final String chosenItem = intent.getStringExtra("chosenItem");

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.5));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 100;

        getWindow().setAttributes(params);

        mDatabaseCanteen = database.getReference("/Canteen/" + chosenCanteen + "/Menu");

        mDatabaseCanteen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FoodItem foodItem = snapshot.child(chosenItem).getValue(FoodItem.class);
                int fat = foodItem.getFat();
                int protein = foodItem.getProtein();
                int carbohydrate = foodItem.getCarbohydrate();
                int calorie = foodItem.getCalorie();
                int price = foodItem.getPrice();
                String name = foodItem.getName();

                tvCalorie.setText("Calorie: " + calorie);
                tvCarbohydrate.setText("Carbohydrate: " + carbohydrate);
                tvFat.setText("Fat: " + fat);
                tvFoodItemName.setText(name);
                tvProtein.setText("Protein: " + protein);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}