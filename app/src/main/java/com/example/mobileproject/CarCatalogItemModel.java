package com.example.mobileproject;

public class CarCatalogItemModel {
    //variables
    private int price;
    private String name,image,car_id;

//constructor
    public CarCatalogItemModel(String car_id,String image, int price, String name) {
        this.car_id = car_id;
        this.image = image;
        this.price = price;
        this.name = name;
    }


    //getters
    public String getImage() {
        return image;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
     public String getCar_id(){return car_id;}
}
