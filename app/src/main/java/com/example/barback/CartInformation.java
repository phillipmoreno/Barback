package com.example.barback;

public class CartInformation {
    public String name;
    public String image;
    public String quantity;
    public String unitPrice;
    public String total;

    public CartInformation(){}

    public CartInformation(String name, String image, String quantity, String unitPrice, String total){
        this.name = name;
        this.image = image;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.total = total;
    }
    public String getName(){
        return name;
    }

    public String getImage(){
        return image;
    }

    public String getQuantity(){
        return quantity;
    }

    public String getUnitPrice(){
        return unitPrice;
    }

    public String getTotal(){
        return total;
    }


}
