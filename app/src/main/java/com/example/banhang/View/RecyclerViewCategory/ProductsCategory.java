package com.example.banhang.View.RecyclerViewCategory;

public class ProductsCategory {
    String id;
    String name;
    public ProductsCategory(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public String getID(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
}
