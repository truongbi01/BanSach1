package com.example.banhang.View.RecycleViewGioHang;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.banhang.R;
import com.example.banhang.View.RecyclerViewProduct.*;
import java.util.ArrayList;

import com.example.banhang.View.RecyclerViewProductFavorite.ProductsFavoriteAdapter;
import  com.example.banhang.database.*;
import  com.example.banhang.View.fragment.*;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    ArrayList<Products> lstProductCart;
    Context context;
    CreateDatabase createDatabase;
    private int soLuong ;
    private TotalAmountListener totalAmountListener;
    private ArrayList<CartViewHolder> holders = new ArrayList<>();
    int soLuongHienTai = 0;
    private HomeFragment homeFragment;

    public CartAdapter (ArrayList<Products> listProductsCart, CreateDatabase createDatabase, TotalAmountListener totalAmountListener){
        this.lstProductCart = listProductsCart;
        this.createDatabase = createDatabase;
        this.totalAmountListener = totalAmountListener;
    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        // Tạo View
        View view = layoutInflater.inflate(R.layout.layout_item_cart,parent,false);
        CartAdapter.CartViewHolder viewHolder = new CartAdapter.CartViewHolder(view);
        return viewHolder ;
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holders.add(holder);
        //Lấy tung products của dữ liệu
        Products item = lstProductCart.get(position);

        //Gán dữ liệu cho item
        holder.tvName.setText(item.getName());
        holder.tvGia.setText(item.getPrice()+"VND");
        holder.imgAnhSanPham.setImageBitmap(Utils.convertToBitmapFromAssets(context,item.getImage()));
        //Lấy số lượng từ detail
        SharedPreferences sharedPreferences = context.getSharedPreferences("DuLieu",Context.MODE_PRIVATE);


        //Tạo sự kiện
        holder.btnTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(holder.edtSoLuong.getText().toString());
                if (currentQuantity > 1) { // Giảm số lượng nếu lớn hơn 1
                    holder.edtSoLuong.setText(String.valueOf(currentQuantity - 1));
                        calculateTotalAmount();
                    if (holder.cbGioHang.isChecked()) { // Kiểm tra xem checkbox được chọn
                        calculateAndNotifyTotalAmount();
                    }
                } else {
                    Toast.makeText(context, "Sản phẩm ít nhất là 1", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.btnCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(holder.edtSoLuong.getText().toString());
                // Tăng số lượng
                holder.edtSoLuong.setText(String.valueOf(currentQuantity + 1));
                if (holder.cbGioHang.isChecked()) { // Kiểm tra xem checkbox được chọn
                    calculateAndNotifyTotalAmount();
                }
            }
        });

        for (Products product : lstProductCart) {
            // Kiểm tra sản phẩm đã được chọn trong giỏ hàng
            if (product  != null ) {
                // Tăng số lượng sản phẩm trong giỏ hàng
                soLuongHienTai += 1; // Hoặc sử dụng một số thuộc tính khác để lấy số lượng, phụ thuộc vào cách bạn đã cài đặt nó trong lớp Products
            }
        }
        holder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    HomeFragment homeFragment = new HomeFragment();
                    homeFragment.reloadFragment();
                }finally {
                    XoaSanPham(createDatabase.GetIdSanPham(item.getName()), context);
                }



            }
        });

        holder.cbGioHang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setChecked(isChecked);
                totalAmountListener.onTotalAmountChanged(calculateTotalAmount());
            }
        });
    }


    @Override
    public int getItemCount() {
        return lstProductCart.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        EditText edtSoLuong;
        ImageView imgAnhSanPham;
        TextView tvName, tvGia;
        CheckBox cbGioHang;
        Button btnTru, btnCong,btnXoa;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTenSanPham_GioHang);
            tvGia = itemView.findViewById(R.id.tvGiaTien_GioHang);
            cbGioHang = itemView.findViewById(R.id.cbGioHang);
            imgAnhSanPham = itemView.findViewById(R.id.imgAnhSanPham_Cart);
            btnTru = itemView.findViewById(R.id.btnTru_GioHang);
            btnCong = itemView.findViewById(R.id.btnCong_GioHang);
            edtSoLuong = itemView.findViewById(R.id.edtSoLuong_GioHang);
            btnXoa = itemView.findViewById(R.id.btnXoa_Cart);
        }
    }
    public void setHomeFragment(HomeFragment fragment) {
        this.homeFragment = fragment;
    }
        private void XoaSanPham(String id,Context context) {
        SQLiteDatabase database = createDatabase.getWritableDatabase(); // Sử dụng createDatabase đã truyền vào constructor
        // Xác định bảng và điều kiện cho phép xóa dữ liệu
        String tableName = CreateDatabase.TB_GIO_HANG; // Thay "YourTableName" bằng tên bảng của bạn
        String condition = CreateDatabase.CL_GIO_HANG_SAN_PHAM_ID + "= ?"; // Điều kiện xóa, ở đây là xóa Sản phẩm theo tên
        String[] args = {String.valueOf(id)}; // Giá trị tham số cho điều kiện xóa

        // Thực thi lệnh xóa trong cơ sở dữ liệu
        int rowsAffected = database.delete(tableName, condition, args);

        if (rowsAffected > 0) {
            // Nếu có ít nhất một hàng được xóa, thông báo xóa thành công
            Toast.makeText(context,"Xoá Sản Phẩm thành công",Toast.LENGTH_SHORT).show();
            // (cập nhật giao diện người dùng hoặc thông báo cho người dùng nếu cần thiết)
            LoadData(context);
            setData(lstProductCart);
        } else {
            // Nếu không có hàng nào được xóa, thông báo xóa không thành công hoặc không có sản phẩm tương ứng trong cơ sở dữ liệu
            Toast.makeText(context,"Xoá Sản Phẩm Không thành công",Toast.LENGTH_SHORT).show();

        }
    }
    public void LoadData(Context context) {
        lstProductCart = Utils.LoadDaTaProductsCart(context);

    }
    public void setData(ArrayList<Products> newList) {
        this.lstProductCart = newList;
        notifyDataSetChanged();
    }
    private double calculateTotalAmount() {
        double totalAmount = 0.0;

        for (Products product : lstProductCart) {
            if (product.isChecked()) {
                double price = Double.parseDouble(product.getPrice().replace("VND", "").trim());
                int quantity = 0;

                // Tìm số lượng tương ứng trong ViewHolder
                for (CartViewHolder holder : holders) {
                    if (holder != null && holder.cbGioHang.isChecked() && holder.tvName.getText().toString().equals(product.getName())) { // Kiểm tra checkbox đã được chọn và sản phẩm tương ứng
                        String quantityString = holder.edtSoLuong.getText().toString();
                        if (!quantityString.isEmpty()) {
                            quantity = Integer.parseInt(quantityString);
                            break; // Thoát khỏi vòng lặp sau khi tìm thấy số lượng
                        }
                    }
                }

                totalAmount += price * quantity; // Tính tổng số tiền cho sản phẩm hiện tại
            }
        }

        return totalAmount;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void selectAll(boolean isSelected) {
        for (Products product : lstProductCart) {
            product.setChecked(isSelected);

        }

        // Cập nhật tổng số tiền sau khi chọn/bỏ chọn tất cả sản phẩm
        totalAmountListener.onTotalAmountChanged(calculateTotalAmount());

        // Cập nhật trạng thái của tất cả các CheckBox trong holders
        for (CartViewHolder holder : holders) {
            holder.cbGioHang.setChecked(isSelected);
        }
        notifyDataSetChanged();
    }

    public interface TotalAmountListener {
        void onTotalAmountChanged(double totalAmount);
    }
    private void calculateAndNotifyTotalAmount() {
        double totalAmount = calculateTotalAmount();
        totalAmountListener.onTotalAmountChanged(totalAmount);
    }

}


