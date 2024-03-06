package com.example.banhang.View.RecyclerViewProduct;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banhang.R;
import com.example.banhang.View.RecyclerViewCategory.CategoryAdapter;
import com.example.banhang.database.CreateDatabase;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    ArrayList<Products> listProducts ;
    CategoryAdapter.OnItemClickListener mListener;
    CreateDatabase databaseHelper;
    Context context;
    public ProductAdapter(){};
    public ProductAdapter(ArrayList<Products> listProducts , CreateDatabase  database){
        this.listProducts = listProducts;
        this.databaseHelper = database;
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //Nạp layout cho view biểu diễn phần từ User
        View userView = inflater.inflate(R.layout.layout_item_product,parent,false);
        //
        ProductAdapter.ProductViewHolder viewHolder = new   ProductAdapter.ProductViewHolder(userView);
        return viewHolder;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        //Lấy từng item của dữ liệu
        Products item = listProducts.get(position);

        //gán vào item của view
        holder.tvName.setText(item.getName());
        holder.tvGia.setText(item.getPrice() + "đ");
        holder.imgAnhSanPham.setImageBitmap(Utils.convertToBitmapFromAssets(context,item.getImage()));
        // Set trạng thái của checkbox
        holder.cbYeuThich.setChecked(createDatabase.isFavorite(item.getName(),createDatabase.GetIdSanPham(item.getName())));
        //Tạo sự kiện cho nút tim
        holder.cbYeuThich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String tenSanPham = item.getName();
                String idSanPham = createDatabase.GetIdSanPham(tenSanPham);
                if(isChecked){
                    ThemSanPhamYeuThich(tenSanPham,idSanPham,context);
                }
                else {
                    XoaSanPhamYeuThich(tenSanPham,idSanPham,context);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder{
        ImageView imgAnhSanPham;
        TextView tvName,tvGia;
        CheckBox cbYeuThich;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTenProducts);
            tvGia = itemView.findViewById(R.id.tvGiaSanPham);
            cbYeuThich = itemView.findViewById(R.id.cbYeuThich);
            imgAnhSanPham = itemView.findViewById(R.id.imgAnhSanPham);
        }

    }
    //insert value thông tin sản phẩm yêu thích
    public void ThemSanPhamYeuThich(String tenSanPham,String idSanPham,Context context){
        CreateDatabase createDatabase = new CreateDatabase(context);

        SQLiteDatabase sqLiteDatabase = createDatabase.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CreateDatabase.CL_TEN_KHACH_HANG_YEU_THICH,tenSanPham);
        cv.put(CreateDatabase.CL_SAN_PHAM_YEU_THICH_ID,idSanPham);

        sqLiteDatabase.insert(CreateDatabase.TB_YEU_THICH,null,cv);
        Toast.makeText(context, "Sản phẩm đã được thêm vao danh sách yêu thích", Toast.LENGTH_SHORT).show();

        sqLiteDatabase.close();

    }
    public void XoaSanPhamYeuThich(String tenSanPham, String idSanPham, Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);

        SQLiteDatabase sqLiteDatabase = createDatabase.getWritableDatabase();

        // Xác định điều kiện xóa - dựa trên tên sản phẩm và ID sản phẩm
        String selection = CreateDatabase.CL_TEN_KHACH_HANG_YEU_THICH + "=? AND " +
                CreateDatabase.CL_SAN_PHAM_YEU_THICH_ID + "=?";
        String[] selectionArgs = {tenSanPham, idSanPham};

        // Thực hiện xóa
        int deletedRows = sqLiteDatabase.delete(CreateDatabase.TB_YEU_THICH, selection, selectionArgs);

        if (deletedRows > 0) {
            Toast.makeText(context, "Sản phẩm đã được xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Không thể xóa sản phẩm khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
        }
        sqLiteDatabase.close();
    }

}
