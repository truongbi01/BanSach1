package com.example.banhang.View.RecyclerViewProduct;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.example.banhang.View.RecyclerViewCategory.*;
import com.example.banhang.database.*;

public class Utils {

    public static Bitmap convertToBitmapFromAssets(Context context, String nameImage) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open("images/" + nameImage);
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<ProductsCategory> loadCategoriesFromDatabase(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        ArrayList<ProductsCategory> categoryList = new ArrayList<>();
        SQLiteDatabase db = createDatabase.getReadableDatabase();

        String query = "SELECT " + CreateDatabase.CL_THE_LOAI_SAN_PHAM_ID + ", " + CreateDatabase.CL_TEN_LOAI_SAN_PHAM +
                " FROM " + CreateDatabase.TB_LOAI_SAN_PHAM;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String categoryId = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_THE_LOAI_SAN_PHAM_ID));
                @SuppressLint("Range")String categoryName = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_TEN_LOAI_SAN_PHAM));

                ProductsCategory category = new ProductsCategory(categoryId, categoryName);
                categoryList.add(category);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return categoryList;
    }

    public static ArrayList<ProductsCategory> LoadDataCategory(Context context) {
        ArrayList<ProductsCategory> lstUser = new ArrayList<>();
        ArrayList<ProductsCategory> categoryList = Utils.loadCategoriesFromDatabase(context);

        for (ProductsCategory category : categoryList) {
            lstUser.add(new ProductsCategory(category.getID(), category.getName()));
        }

        return lstUser;
    }

    public static ArrayList<Products> LoadProductsFromDatabase(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        ArrayList<Products> listProducts = new ArrayList<>();
        SQLiteDatabase db = createDatabase.getReadableDatabase();

        String query = "SELECT * FROM " + CreateDatabase.TB_SAN_PHAM;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String tenSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_TEN_SAN_PHAM));
                @SuppressLint("Range") String giaSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_GIA_BAN));
                @SuppressLint("Range") String moTaSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_MO_TA));
                @SuppressLint("Range") String anhSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_ANH_SAN_PHAM));
                @SuppressLint("Range")  String idTheLoaiSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_LOAI_SAN_PHAM_ID));

                Products newProducts1 = new Products(context, tenSanPham, giaSanPham, moTaSanPham, anhSanPham, idTheLoaiSanPham);
                listProducts.add(newProducts1);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listProducts;
    }

    public static ArrayList<Products> LoadDaTaProducts(Context context) {
        ArrayList<Products> lstProducts = new ArrayList<>();
        ArrayList<Products> productsArrayList = Utils.LoadProductsFromDatabase(context);

        for (Products products : productsArrayList) {
            if (products != null) {
                lstProducts.add(new Products(context, products.getName(), products.getPrice(), products.getDes(), products.getImage(), products.getCategoryId()));
            }
        }

        return lstProducts;
    }
    public static ArrayList<Products> LoadProductsFavoriteFromDatabase(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        ArrayList<Products> listProducts = new ArrayList<>();
        SQLiteDatabase db = createDatabase.getReadableDatabase();

        String query = "SELECT " + CreateDatabase.CL_TEN_SAN_PHAM + ", " + CreateDatabase.CL_GIA_BAN + ", " + CreateDatabase.CL_ANH_SAN_PHAM + ", " + CreateDatabase.CL_MO_TA + ", " + CreateDatabase.CL_THE_LOAI_SAN_PHAM_ID +
                " From " + CreateDatabase.TB_YEU_THICH +
                " inner  join " + CreateDatabase.TB_SAN_PHAM +
                " on " + CreateDatabase.TB_YEU_THICH + "." + CreateDatabase.CL_SAN_PHAM_YEU_THICH_ID +
                " = " + CreateDatabase.TB_SAN_PHAM + "." + CreateDatabase.CL_SAN_PHAM_ID;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String tenSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_TEN_SAN_PHAM));
                @SuppressLint("Range") String giaSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_GIA_BAN));
                @SuppressLint("Range") String moTaSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_MO_TA));
                @SuppressLint("Range") String anhSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_ANH_SAN_PHAM));
                @SuppressLint("Range")  String idTheLoaiSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_LOAI_SAN_PHAM_ID));

                Products newProducts1 = new Products(context, tenSanPham, giaSanPham, moTaSanPham, anhSanPham, idTheLoaiSanPham);
                listProducts.add(newProducts1);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listProducts;
    }
    public static void ThongBao(Context context, String thongBao){
        Toast.makeText(context,thongBao,Toast.LENGTH_SHORT).show();
    }

}
