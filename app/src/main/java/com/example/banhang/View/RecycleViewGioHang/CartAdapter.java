package com.example.banhang.View.RecycleViewGioHang;

import android.annotation.SuppressLint;
import android.content.Context;
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
                holder.edtSoLuong.setText("");
                holder.cbGioHang.setChecked(false);


                    XoaSanPham(createDatabase.GetIdSanPham(item.getName()), context);




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
        String tableName = CreateDatabase.TB_GIO_HANG;
        String condition = CreateDatabase.CL_GIO_HANG_SAN_PHAM_ID + "= ?"; // Điều kiện xóa, ở đây là xóa Sản phẩm theo id
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
    double calculateTotalAmount() {
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
    public double calculateAndNotifyTotalAmount() {
        double totalAmount = calculateTotalAmount();
        totalAmountListener.onTotalAmountChanged(totalAmount);
        return totalAmount;
    }

    public ArrayList<Products> getSelectedProducts() {
        ArrayList<Products> selectedProducts = new ArrayList<>();
        for (Products product : lstProductCart) {
            if (product.isChecked()) {
                selectedProducts.add(product);
            }
        }
        return selectedProducts;
    }
    void deleteSelectedProductsFromCart(String id,ArrayList<Products> selectedProducts, SQLiteDatabase database) {
        String tableName = CreateDatabase.TB_GIO_HANG;
        int deletedCount = 0;
        for (Products product : selectedProducts) {
            if(product.isChecked()){
                String condition = CreateDatabase.CL_GIO_HANG_SAN_PHAM_ID + " = ?";
                String[] args = {String.valueOf(id)}; // Sử dụng id của từng sản phẩm để xóa
                int rowsAffected = database.delete(tableName, condition, args);
                if (rowsAffected > 0) {
                    deletedCount++;
                }
            }

        }

        if (deletedCount > 0) {
            // Nếu có ít nhất một sản phẩm được xóa, thông báo xóa thành công
            Toast.makeText(context, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
        } else {
            // Nếu không có sản phẩm nào được xóa, thông báo xóa không thành công
            Toast.makeText(context, "Không có sản phẩm nào được xóa", Toast.LENGTH_SHORT).show();
        }

        // Cập nhật lại dữ liệu giỏ hàng sau khi xóa sản phẩm
        LoadData(context);
        setData(lstProductCart);

    }
}


