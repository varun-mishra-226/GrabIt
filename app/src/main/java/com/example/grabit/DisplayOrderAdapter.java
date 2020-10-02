package com.example.grabit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DisplayOrderAdapter extends ArrayAdapter<OrderedItemsDetail> {

    Context context;
    int resource;
    List<OrderedItemsDetail> orderedItemsDetailList;

    public DisplayOrderAdapter(Context context, int resource, List<OrderedItemsDetail> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.orderedItemsDetailList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.order_item_details, null);

        TextView tvOrderTime = view.findViewById(R.id.tvOrderTime);
        TextView tvOrderQuantity = view.findViewById(R.id.tvOrderQuantity);
        TextView tvOrderItem = view.findViewById(R.id.tvOrderItem);

        OrderedItemsDetail orderedItemsDetail = orderedItemsDetailList.get(position);

        tvOrderItem.setText(orderedItemsDetail.getOrderItem());
        tvOrderQuantity.setText(String.valueOf(orderedItemsDetail.getOrderQuantity()));
        tvOrderTime.setText(orderedItemsDetail.getOrderDate());

        return view;
    }
}
