package com.example.grabit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FoodItemListAdapter extends BaseAdapter implements Filterable {
    Context context;
    List<FoodItem> menuList, filterMenuList;
    int menuListSize;

    public FoodItemListAdapter(Context context, List<FoodItem> menuList){
        super();

        this.context = context;
        this.menuList = menuList;
        this.menuListSize = menuList.size();
    }

    public class FoodItemHolder{
        TextView name;
        TextView price;
        TextView calorie;
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
                    filterResults.values = results;
                }
                else{
                    if (filterMenuList!=null && filterMenuList.size()>0){
                        for (final FoodItem f: filterMenuList){
                            results.add(f);
                        }
                    }
                    filterResults.values = results;
                }
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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        FoodItemHolder holder;
        if (convertView==null){
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.menu_food_item, parent, false);
            holder = new FoodItemHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tvFoodName);
            holder.calorie = (TextView) convertView.findViewById(R.id.tvCalorie);
            holder.price = (TextView) convertView.findViewById(R.id.tvPrice);
            convertView.setTag(holder);
        }
        else{
            holder = (FoodItemHolder) convertView.getTag();
        }

        holder.name.setText(menuList.get(position).getName());
        holder.price.setText(String.valueOf(menuList.get(position).getPrice()));
        holder.calorie.setText(String.valueOf(menuList.get(position).getCalorie()));

        /*LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.menu_food_item, null);

        TextView tvCalorie = view.findViewById(R.id.tvCalorie);
        TextView tvPrice = view.findViewById(R.id.tvPrice);
        TextView tvFoodName = view.findViewById(R.id.tvFoodName);

        FoodItem foodItem = menuList.get(position);

        tvFoodName.setText(foodItem.getName());
        tvPrice.setText(String.valueOf(foodItem.getPrice()));
        tvCalorie.setText(String.valueOf(foodItem.getCalorie()));*/

        return convertView;
    }
}
