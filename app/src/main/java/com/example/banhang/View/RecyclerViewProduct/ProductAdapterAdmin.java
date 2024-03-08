package com.example.banhang.View.RecyclerViewProduct;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banhang.R;
import com.example.banhang.View.RecyclerViewCategory.CategoryAdapter;
import com.example.banhang.View.RecyclerViewCategory.ProductsCategory;
import com.example.banhang.database.CreateDatabase;

import java.util.ArrayList;

public class ProductAdapterAdmin extends RecyclerView.Adapter<ProductAdapterAdmin.ProductViewHolderAdmin> {
    ArrayList<Products> listProducts ;
    CategoryAdapter.OnItemClickListener mListener;
    CreateDatabase databaseHelper;
    Context context;
    public ProductAdapterAdmin(){};

    public ProductAdapterAdmin(ArrayList<Products> listProducts , CreateDatabase  database){
        this.listProducts = listProducts;
        this.databaseHelper = database;
    }
    @NonNull
    @Override
    public ProductViewHolderAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //Nạp layout cho view biểu diễn phần từ User
        View userView = inflater.inflate(R.layout.layout_item_product_admin,parent,false);
        //
        ProductAdapterAdmin.ProductViewHolderAdmin viewHolder = new   ProductAdapterAdmin.ProductViewHolderAdmin(userView);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolderAdmin holder, int position) {
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
                ProductAdapter productAdapter = new ProductAdapter();

                String tenSanPham = item.getName();
                String idSanPham = createDatabase.GetIdSanPham(tenSanPham);
                if(isChecked){
                    productAdapter.ThemSanPhamYeuThich(tenSanPham,idSanPham,context);
                }
                else {
                    productAdapter.XoaSanPhamYeuThich(tenSanPham,idSanPham,context);
                }
            }
        });
        holder.btnChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProductAdapter productAdapter = new ProductAdapter();
                String idSanPham = createDatabase.GetIdSanPham(item.getName());

                productAdapter.XoaSanPhamYeuThich(item.getName(),idSanPham,context);
                showEditDialog(item,context);
            }
        });
        holder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XoaSanPham(item.getName(),context);


                LoadData(context);
                setData(listProducts);
            }
        });
    }



    @SuppressLint("SetTextI18n")
    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    static class ProductViewHolderAdmin extends RecyclerView.ViewHolder{
        ImageView imgAnhSanPham;
        TextView tvName,tvGia;
        CheckBox cbYeuThich;
        Button btnChinhSua , btnXoa;
        public ProductViewHolderAdmin(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTenProducts);
            tvGia = itemView.findViewById(R.id.tvGiaSanPham);
            cbYeuThich = itemView.findViewById(R.id.cbYeuThich);
            imgAnhSanPham = itemView.findViewById(R.id.imgAnhSanPham);
            btnChinhSua = itemView.findViewById(R.id.btnChinhSua_SanPham);
            btnXoa = itemView.findViewById(R.id.btnXoa_SanPham);

        }

    }
    //insert value thông tin sản phẩm yêu thích

    public void ChinhSuaSanPham(String tenSanPham,String tenSanPhamMoi, String giaMoi, String theloaimoi, String anhSanPhammoi,Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);

        SQLiteDatabase sqLiteDatabase = createDatabase.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CreateDatabase.CL_TEN_SAN_PHAM,tenSanPhamMoi);
        values.put(CreateDatabase.CL_GIA_BAN, giaMoi);
        values.put(CreateDatabase.CL_ANH_SAN_PHAM,anhSanPhammoi);
        values.put(CreateDatabase.CL_THE_LOAI_SAN_PHAM_ID,theloaimoi);
        String selection = CreateDatabase.CL_TEN_SAN_PHAM + "=?";
        String[] selectionArgs = {tenSanPham};

        int rowsAffected = sqLiteDatabase.update(CreateDatabase.TB_SAN_PHAM, values, selection, selectionArgs);

        if (rowsAffected > 0) {
            Toast.makeText(context, "Thông tin sản phẩm đã được cập nhật", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Không thể cập nhật thông tin sản phẩm", Toast.LENGTH_SHORT).show();
        }

        sqLiteDatabase.close();
    }
    //Kieem tra San Pham Da ton tai chua
    private boolean isProductExists(String tenSanPham) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CreateDatabase.TB_SAN_PHAM + " WHERE " +
                CreateDatabase.CL_TEN_SAN_PHAM + "=?", new String[]{tenSanPham});

        boolean exists = cursor.getCount() > 0;

        cursor.close();

        return exists;
    }
    public void XoaSanPham(String tenSanPham, Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);

        SQLiteDatabase sqLiteDatabase = createDatabase.getWritableDatabase();

        String selection = CreateDatabase.CL_TEN_SAN_PHAM + "=?";
        String[] selectionArgs = {tenSanPham};

        int rowsDeleted = sqLiteDatabase.delete(CreateDatabase.TB_SAN_PHAM, selection, selectionArgs);

        if (rowsDeleted > 0) {
            Toast.makeText(context, "Sản phẩm đã được xóa ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Không thể xóa sản phẩm ", Toast.LENGTH_SHORT).show();
        }

        sqLiteDatabase.close();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void LoadData(Context context) {
        listProducts = Utils.LoadDaTaProducts(context);

    }
    public void setData(ArrayList<Products> newList) {
        this.listProducts = newList;
        notifyDataSetChanged();
    }
    public void showEditDialog(Products products , Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_edit_products, null); // Tạo layout cho dialog

        // Ánh xạ các thành phần trong dialog
        EditText edtTenSanPhamMoi = view.findViewById(R.id.edtTenSanPhamMoi);
        EditText edtSrcAnhSanPhamMoi = view.findViewById(R.id.edtSrcSanPhamMoi);
        EditText edtGiaTienMoi = view.findViewById(R.id.edtGiaTienMoi);
        EditText edtTheLoaiMoi = view.findViewById(R.id.edtTheLoaiMoi);
        // Thêm các thành phần khác nếu cần

        builder.setView(view);
        builder.setTitle("Chỉnh sửa thông tin sản phẩm");

        builder.setPositiveButton("Lưu", (dialog, which) -> {

            // Xử lý khi người dùng nhấn nút Lưu
            String tenSanPhamMoi = edtTenSanPhamMoi.getText().toString();
            String srcAnhMoi = edtSrcAnhSanPhamMoi.getText().toString();
            String giaMoi = edtGiaTienMoi.getText().toString();
            String theLoaiMoi = edtTheLoaiMoi.getText().toString();
            if(tenSanPhamMoi.equals("")){
                ThongBao(context,"Tên Sản Phẩm Đang Trống12");
            } else if (srcAnhMoi.equals("")) {
                ThongBao(context,"Src Ảnh Đang Trống");
            }else if(giaMoi.equals("")){
                ThongBao(context,"Gia tiền đang trống");
            }
            else if (theLoaiMoi.equals("") || !products.isCategoryValid(theLoaiMoi)) {
              Toast.makeText(context,"Loại sản phẩm không hợp lệ!",Toast.LENGTH_SHORT).show();
            }
            else {
                // Lưu thông tin mới vào cơ sở dữ liệu hoặc thực hiện các bước cần thiết
                ChinhSuaSanPham(products.getName(),tenSanPhamMoi,giaMoi, theLoaiMoi, srcAnhMoi, context);
            }

        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }
    void ThongBao(Context context, String noidung){
        Toast.makeText(context,noidung,Toast.LENGTH_SHORT).show();
    }
}
