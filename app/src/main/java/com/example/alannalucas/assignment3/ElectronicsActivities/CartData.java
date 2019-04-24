package com.example.alannalucas.assignment3.ElectronicsActivities;

import android.widget.TextView;

public class CartData {

    public String title;
    public String manufacturer;
    public String image;
    public String category;
    public String quantity;
    public String price;
    public String total;

    public CartData(){

    }

    public CartData(String title, String manufacturer, String image, String category, String quantity, String price, String total) {
        this.title = title;
        this.manufacturer = manufacturer;
        this.image = image;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
