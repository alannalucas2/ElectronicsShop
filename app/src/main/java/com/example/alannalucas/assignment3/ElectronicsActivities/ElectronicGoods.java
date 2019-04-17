package com.example.alannalucas.assignment3.ElectronicsActivities;

public class ElectronicGoods {

    private String title;
    private String manufacturer;
    private String image;
    private String category;
    private String quantity;
    private String price;

    public ElectronicGoods(){

    }


    public ElectronicGoods(String title, String manufacturer, String image, String category, String quantity, String price) {
        this.title = title;
        this.manufacturer = manufacturer;
        this.image = image;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
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

    public String getImageUrl() {
        return image;
    }

    public void setImageUrl(String imageUrl) {
        this.image = imageUrl;
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
}
