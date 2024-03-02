package com.example.banhang.View.RecyclerViewProduct;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.example.banhang.View.RecyclerViewCategory.*;
import com.example.banhang.database.*;
public class Utils {

    public static Bitmap convertToBitmapFromAssets(Context context, String nameImage)  {
        AssetManager assetManager = context.getAssets();
        try{
            InputStream inputStream = assetManager.open("image/"+ nameImage);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return  bitmap;

        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<ProductsCategory> loadCategoriesFromDatabase(Context context) {
        // Thực hiện truy vấn cơ sở dữ liệu ở đây và trả về danh sách thể loại
        CreateDatabase createDatabase = new CreateDatabase(context);

        // Ví dụ sử dụng CreateDatabase từ mã nguồn của bạn

        ArrayList<ProductsCategory> categoryList = new ArrayList<>();
        SQLiteDatabase db = createDatabase.getReadableDatabase();

        // Thực hiện truy vấn và đọc dữ liệu từ cơ sở dữ liệu
        String query = "SELECT " + CreateDatabase.CL_THE_LOAI_SAN_PHAM_ID + ", " + CreateDatabase.CL_TEN_LOAI_SAN_PHAM +
                " FROM " + CreateDatabase.TB_LOAI_SAN_PHAM;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String categoryId = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_THE_LOAI_SAN_PHAM_ID));
                @SuppressLint("Range") String categoryName = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_TEN_LOAI_SAN_PHAM));

                // Tạo đối tượng Category từ dữ liệu cơ sở dữ liệu và thêm vào danh sách
                ProductsCategory category = new ProductsCategory(categoryId, categoryName);
                categoryList.add(category);
            } while (cursor.moveToNext());
        }

        // Đóng cursor và database
        cursor.close();
        db.close();

        return categoryList;
    }

    public static ArrayList<ProductsCategory> LoadDataCategory(Context context) {
        ArrayList<ProductsCategory> lstUser = new ArrayList<>();
        // Sử dụng dữ liệu từ cơ sở dữ liệu thay vì dữ liệu mẫu cứng
        ArrayList<ProductsCategory> categoryList = Utils.loadCategoriesFromDatabase(context);

        for (ProductsCategory category : categoryList) {
            lstUser.add(new ProductsCategory(category.getID(), category.getName()));
        }

        return lstUser;
    }
}
