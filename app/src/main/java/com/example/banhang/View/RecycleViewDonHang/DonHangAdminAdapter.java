package com.example.banhang.View.RecycleViewDonHang;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banhang.R;
import com.example.banhang.database.CreateDatabase;

import java.util.ArrayList;

public class DonHangAdminAdapter extends RecyclerView.Adapter<DonHangAdminAdapter.DonHangViewHolder> {
    ArrayList<DonHang> listDonHang ;
    Context context;
    public DonHangAdminAdapter(ArrayList<DonHang> listDonHang){
        this.listDonHang = listDonHang;
    }
    @NonNull
    @Override
    public DonHangViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        // Tạo View
        View view = layoutInflater.inflate(R.layout.layout_item_donhang_admin,parent,false);
        DonHangAdminAdapter.DonHangViewHolder viewHolder = new  DonHangAdminAdapter.DonHangViewHolder(view);
        return viewHolder ;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull DonHangViewHolder holder, int position) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        //Lay tung products cua dữ liêu
        DonHang donHang = listDonHang.get(position);
        //Gan Du lieu vao view
      holder.tvNgayDat.setText(donHang.getNgayDatHang());
      holder.tvTongSoTien.setText(String.valueOf(donHang.getTongTien() + "VNĐ"));
        SharedPreferences sharedPreferences = context.getSharedPreferences("tk_mk login",Context.MODE_PRIVATE);
        String tenDangNhap =  sharedPreferences.getString("Username",null);
        String role = createDatabase.GetCLVaitro(tenDangNhap);
       String user = createDatabase.getTenDangNhapFromDonHang(context,donHang.getNgayDatHang(),donHang.getTongTien());
       holder.tvTenDangNhap.setText(user);
        if(role.equals("admin") && donHang.getTrangThai() == 0){
            holder.btnTrangThaiDonHang.setText("Xác Nhận Đơn");
            holder.btnTrangThaiDonHang.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    updateDonHangTrangThai(donHang.getMaDonHang(),1,context);
                    holder.btnTrangThaiDonHang.setText("Đã Xác Nhận");
                    holder.btnTrangThaiDonHang.setBackgroundResource(R.drawable.customer_button_trangthai_true_admin);
                    holder.btnTrangThaiDonHang.setTextColor(R.color.darklv10);

                }
            });
        } else if (role.equals("admin") && donHang.getTrangThai() == 1) {
            holder.btnTrangThaiDonHang.setText("Đã Xác Nhận");
            holder.btnTrangThaiDonHang.setBackgroundResource(R.drawable.customer_button_trangthai_true_admin);
            holder.btnTrangThaiDonHang.setTextColor(R.color.darklv10);
        }


    }
    @Override
    public int getItemCount() {
        return listDonHang.size();
    }
    static class DonHangViewHolder extends RecyclerView.ViewHolder{
        TextView tvNgayDat,tvTongSoTien,tvTenDangNhap;
        Button btnTrangThaiDonHang,btnXemChiTiet;



        public DonHangViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNgayDat = itemView.findViewById(R.id.tvNgayDat_DonHang_Admin);
            tvTongSoTien = itemView.findViewById(R.id.tvTongTien_DonHang_Admin);
            btnTrangThaiDonHang = itemView.findViewById(R.id.btnTrangThai_DonHang_Admin);
            btnXemChiTiet = itemView.findViewById(R.id.btn_XemChiTiet_DonHang_Admin);
            tvTenDangNhap = itemView.findViewById(R.id.tv_TenDangNhap_DonHang);
        }
    }
    public void updateDonHangTrangThai(String maDonHang, int trangThai,Context context) {
        CreateDatabase database = new CreateDatabase(context);
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CreateDatabase.CL_TRANG_THAI, trangThai);

        db.update(CreateDatabase.TB_DON_HANG, values, CreateDatabase.CL_DON_HANG_ID + "=?", new String[]{maDonHang});

        db.close();
    }
}
