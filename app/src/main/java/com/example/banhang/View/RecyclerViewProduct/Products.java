package com.example.banhang.View.RecyclerViewProduct;

public class Products {
    String id;
    String name;
    String price;
    String description;

    String image;
    public Products(String id, String name, String price,String description,String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
    }
    public  String getId(){
        return id;
    }
    public String getImage(){
        return image;
    }
    public String getName(){
        return name;
    }
}
