package com.example.banhang.View.RecyclerViewProduct;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.banhang.database.*;

public class Products {
    CreateDatabase databaseHelper;
    String id;
    String name;
    String price;
    String description;



    int soLuong;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    String categoryId;
    private boolean isChecked;
    String image;
    public Products(){};
    public Products( Context context,String name, String price,String image) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.databaseHelper = new CreateDatabase(context);
    }
    public Products( Context context,String name, String price,String moTa,String image,int soLuong) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = moTa;
        this.soLuong = soLuong;
        this.databaseHelper = new CreateDatabase(context);
    }
    public Products( Context context,String name, String price,String description,String image,String categoryId) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.databaseHelper = new CreateDatabase(context);
        this.categoryId = categoryId;
    }
    public Products( Context context,String id,String name, String price,String description,String image,String categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.databaseHelper = new CreateDatabase(context);
        this.categoryId = categoryId;
    }
    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
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
    public String getPrice(){
        return price;
    }
    public String getDes(){
        return description;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public  void ThemSanPham(Products products,Context context) {

        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        try {
            // Kiểm tra xem sản phẩm đã tồn tại hay chưa
            if (!isProductExists(products.getName())) {
                // Nếu sản phẩm chưa tồn tại, thì thêm vào cơ sở dữ liệu
                ContentValues cv = new ContentValues();
                cv.put(CreateDatabase.CL_TEN_SAN_PHAM, products.getName());
                cv.put(CreateDatabase.CL_GIA_BAN, products.getPrice());
                cv.put(CreateDatabase.CL_MO_TA, products.getDes());
                cv.put(CreateDatabase.CL_ANH_SAN_PHAM, products.getImage());
                cv.put(CreateDatabase.CL_LOAI_SAN_PHAM_ID, products.getCategoryId());

                sqLiteDatabase.insert(CreateDatabase.TB_SAN_PHAM, null, cv);
                Toast.makeText(context, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
            } else {
                // Nếu sản phẩm đã tồn tại, hiển thị thông báo hoặc xử lý tùy chọn khác
                Toast.makeText(context, "Sản phẩm đã tồn tại!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            sqLiteDatabase.close();
        }
    }

    // Hàm kiểm tra xem sản phẩm đã tồn tại hay chưa
    private boolean isProductExists(String tenSanPham) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CreateDatabase.TB_SAN_PHAM + " WHERE " +
                CreateDatabase.CL_TEN_SAN_PHAM + "=?", new String[]{tenSanPham});

        boolean exists = cursor.getCount() > 0;

        cursor.close();

        return exists;
    }
    // Hàm kiểm tra loại sản phẩm
    public boolean isCategoryValid(String categoryId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CreateDatabase.TB_LOAI_SAN_PHAM + " WHERE " +
                CreateDatabase.CL_THE_LOAI_SAN_PHAM_ID + "=?", new String[]{categoryId});

        boolean isValid = cursor.getCount() > 0;

        cursor.close();

        return isValid;
    }
}
