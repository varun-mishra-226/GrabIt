package com.example.grabit;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FoodItemListAdapter extends BaseAdapter implements Filterable {
    Context context;
    List<FoodItem> menuList, filterMenuList;
    int menuListSize, targetIntake, currentIntake;

    public FoodItemListAdapter(Context context, List<FoodItem> menuList, int targetIntake, int currentIntake){
        super();

        this.context = context;
        this.menuList = menuList;
        this.menuListSize = menuList.size();
        this.targetIntake = targetIntake;
        this.currentIntake = currentIntake;
    }

    public class FoodItemHolder{
        TextView name;
        TextView price;
        TextView calorie;
        EditText holderQuantity;
        ImageButton increase;
        ImageButton decrease;
        ImageView category;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults filterResults = new FilterResults();
                final ArrayList<FoodItem> results = new ArrayList<FoodItem>();
                if (filterMenuList==null){
                    filterMenuList = menuList;
                }
                if (constraint!=null){
                    if (filterMenuList!=null && filterMenuList.size()>0){
                        for (final FoodItem f: filterMenuList){
                            if (f.getName().toLowerCase().contains(constraint.toString().toLowerCase()))
                                results.add(f);
                        }
                    }
                }
                else{
                    if (filterMenuList!=null && filterMenuList.size()>0){
                        for (final FoodItem f: filterMenuList){
                            results.add(f);
                        }
                    }
                }
                filterResults.values = results;
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                menuList = (ArrayList<FoodItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return menuList.size();
    }

    @Override
    public Object getItem(int position) {
        return menuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final FoodItemHolder holder;

        if (convertView==null){
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.menu_food_item, parent, false);
            holder = new FoodItemHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tvFoodName);
            holder.price = (TextView) convertView.findViewById(R.id.tvPrice);
            holder.calorie = (TextView) convertView.findViewById(R.id.tvCalorie);
            holder.holderQuantity = (EditText) convertView.findViewById(R.id.etQuantity);
            holder.increase = (ImageButton) convertView.findViewById(R.id.ivIncrease);
            holder.decrease = (ImageButton) convertView.findViewById(R.id.ivDecrease);
            holder.category = (ImageView) convertView.findViewById(R.id.ivFoodCategory);
            convertView.setTag(holder);
        }
        else{
            holder = (FoodItemHolder) convertView.getTag();
        }

        holder.name.setText(menuList.get(position).getName());
        holder.price.setText(String.valueOf(menuList.get(position).getPrice()));
        holder.calorie.setText("Calories: " + String.valueOf(menuList.get(position).getCalorie()));
        holder.holderQuantity.setText(menuList.get(position).quantity + "");

        if (menuList.get(position).getCategory().equals("Beverages")){
            holder.category.setImageResource(R.drawable.beverage_new);
        }
        else if (menuList.get(position).getCategory().equals("Snacks")){
            holder.category.setImageResource(R.drawable.snacks_new);
        }
        else if (menuList.get(position).getCategory().equals("Meals")){
            holder.category.setImageResource(R.drawable.meals_new);
        }
        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuantity(position, holder.holderQuantity, 1);
            }
        });
        holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuantity(position, holder.holderQuantity, -1);
            }
        });

        return convertView;
    }

    private void updateQuantity(int position, EditText edTextQuantity, int value) {
        FoodItem products = (FoodItem) getItem(position);
        if(value > 0){
            products.quantity = products.quantity + 1;
        }
        else{
            if(products.quantity > 0){
                products.quantity = products.quantity - 1;
            }
        }
        edTextQuantity.setText(products.quantity + "");
    }
}
