package com.example.barback;

// Class contains necessary member variables for retrieving Drink Information from the Database.
public class Model {

    public String name;
    public String image;
    public String abv;
    public String cal;
    public String taste;
    public String price;

    // Constructor for Model class
    public Model() {
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getAbv() {
        return abv;
    }

    public String getCal() {
        return cal;
    }

    public String getTaste() {
        return taste;
    }

    public String getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAbv(String abv) {
        this.abv = abv;
    }

    public void setCal(String cal) {
        this.cal = cal;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
