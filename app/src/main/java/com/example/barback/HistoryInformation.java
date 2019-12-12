package com.example.barback;

public class HistoryInformation {
    public String orderDate;
    public String name;
    public String image;
    public String quantity;
    public String total;

    public HistoryInformation(){}

    public HistoryInformation(String orderDate, String name, String image, String quantity, String total){
        this.orderDate = orderDate;
        this.name = name;
        this.image = image;
        this.quantity = quantity;
        this.total = total;
    }

    public String getOrderDate(){
        return orderDate;
    }

    public String getName(){
        return name;
    }

    public String getImage(){
        return image;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getTotal(){
        return total;
    }
}
