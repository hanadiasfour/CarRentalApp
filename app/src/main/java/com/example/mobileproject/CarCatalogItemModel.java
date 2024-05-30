package com.example.mobileproject;

public class CarCatalogItemModel {//hhhhhhh
    //variables
    private int image, price;
    private String name;

//constructor
    public CarCatalogItemModel(int image, int price, String name) {
        this.image = image;
        this.price = price;
        this.name = name;
    }


    //getters
    public int getImage() {
        return image;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}
