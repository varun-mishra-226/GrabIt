package com.example.grabit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
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
    TextView tvFoodItemName, tvCalorie, tvCarbohydrate, tvFat, tvProtein, tvSuggestion;
    DatabaseReference mDatabaseCanteen, mDatabaseCustomer;
    FirebaseDatabase database;
    int calorie;

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
        tvSuggestion = (TextView) findViewById(R.id.tvCalorieLimit);
        database = FirebaseDatabase.getInstance();
        mDatabaseCustomer = database.getReference("Customer");

        Intent intent = getIntent();
        final String chosenCanteen = intent.getStringExtra("chosenCanteen");
        final String chosenItem = intent.getStringExtra("chosenItem");
        final int possibleIntake = intent.getIntExtra("possibleIntake", 2000);
        final int calorieItem = intent.getIntExtra("calorieItem", 0);

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
                calorie = foodItem.getCalorie();
                String name = foodItem.getName();

                tvCalorie.setText("Calorie: " + calorie + " cals");
                tvCarbohydrate.setText("Carbohydrate: " + carbohydrate + " grams");
                tvFat.setText("Fat: " + fat + " grams");
                tvFoodItemName.setText(name);
                tvProtein.setText("Protein: " + protein + " grams");

                String imgName = name;
                imgName = imgName.replaceAll("\\s", "_");
                imgName = imgName.toLowerCase();

                Log.i("ImageName", imgName);

                String uri = "@drawable/" + imgName;
                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                Drawable res = getResources().getDrawable(imageResource);
                ivFoodItemImage.setImageDrawable(res);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (possibleIntake<calorieItem) {
            tvSuggestion.setVisibility(View.VISIBLE);
            tvSuggestion.setText("Exceeding Calorie Target!!");
        }
    }
}