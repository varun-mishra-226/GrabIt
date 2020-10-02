package com.example.grabit;

public class OrderedItems {
    public String orderItem;
    public int orderQuantity;

    public OrderedItems() {
    }

    public OrderedItems(String orderItem, int orderQuantity) {
        this.orderItem = orderItem;
        this.orderQuantity = orderQuantity;
    }

    public String getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(String orderItem) {
        this.orderItem = orderItem;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }
}
