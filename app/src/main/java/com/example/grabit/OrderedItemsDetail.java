package com.example.grabit;

public class OrderedItemsDetail {
    public String orderItem;
    public int orderQuantity;
    public String orderDate;

    public OrderedItemsDetail() {
    }

    public OrderedItemsDetail(String orderItem, int orderQuantity, String orderDate) {
        this.orderItem = orderItem;
        this.orderQuantity = orderQuantity;
        this.orderDate = orderDate;
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

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
