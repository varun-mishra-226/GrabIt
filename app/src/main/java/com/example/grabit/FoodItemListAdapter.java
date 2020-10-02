package com.example.grabit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FoodItemListAdapter extends ArrayAdapter<FoodItem> implements Filterable {
    Context context;
    int resource;
    List<FoodItem> menuList;

    ItemSearchFilter filter;
    List<FoodItem> filteredList;

    public FoodItemListAdapter(Context context, int resource, List<FoodItem> menuList){
        super(context, resource, menuList);

        this.context = context;
        this.resource = resource;
        this.menuList = menuList;
        this.filteredList = menuList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.menu_food_item, null);

        TextView tvCalorie = view.findViewById(R.id.tvCalorie);
        TextView tvPrice = view.findViewById(R.id.tvPrice);
        TextView tvFoodName = view.findViewById(R.id.tvFoodName);

        FoodItem foodItem = menuList.get(position);

        tvFoodName.setText(foodItem.getName());
        tvPrice.setText(String.valueOf(foodItem.getPrice()));
        tvCalorie.setText(String.valueOf(foodItem.getCalorie()));

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (filter==null){
            filter = new ItemSearchFilter((ArrayList<FoodItem>) filteredList, this);
        }
        return filter;
    }
}
