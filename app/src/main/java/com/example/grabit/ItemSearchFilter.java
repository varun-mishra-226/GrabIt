package com.example.grabit;

import android.widget.Filter;

import java.util.ArrayList;

public class ItemSearchFilter extends Filter {

    ArrayList<FoodItem> filteredList;
    FoodItemListAdapter adapter;

    public ItemSearchFilter(ArrayList<FoodItem> filteredList, FoodItemListAdapter adapter) {
        this.filteredList = filteredList;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        if (constraint != null && constraint.length()>0){
            constraint = constraint.toString().toUpperCase();
            ArrayList<FoodItem> filteredData = new ArrayList<>();
            for (int i=-0; i<filteredList.size(); i++){
                if (filteredList.get(i).getName().toUpperCase().contains(constraint)){
                    filteredData.add(filteredList.get(i));
                }
            }
            filterResults.count = filteredData.size();
            filterResults.values = filteredData;
        }
        else{
            filterResults.count = filteredList.size();
            filterResults.values = filteredList;
        }
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.menuList = (ArrayList<FoodItem>)results.values;
        adapter.notifyDataSetChanged();
    }
}
