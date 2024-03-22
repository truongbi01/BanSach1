package com.example.banhang.View.RecycleViewDonHang;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;

import com.example.banhang.R;
import com.example.banhang.View.RecyclerViewProductFavorite.ProductsFavoriteAdapter;
import com.example.banhang.database.CreateDatabase;
import com.example.banhang.View.RecyclerViewProduct.*;
import com.example.banhang.View.ChiTietHoaDonAcitivity;

import java.util.ArrayList;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.DonHangViewHolder> {
    ArrayList<DonHang> listDonHang ;
    Context context;
    public DonHangAdapter(ArrayList<DonHang> listDonHang){
        this.listDonHang = listDonHang;
    }
    @NonNull
    @Override
    public DonHangViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        // Tạo View
        View view = layoutInflater.inflate(R.layout.layout_item_donhang,parent,false);
        DonHangAdapter.DonHangViewHolder viewHolder = new  DonHangAdapter.DonHangViewHolder(view);
        return viewHolder ;
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangViewHolder holder, int position) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        //Lay tung products cua dữ liêu
        DonHang donHang = listDonHang.get(position);
        //Gan Du lieu vao view
      holder.tvNgayDat.setText(donHang.getNgayDatHang());
      holder.tvTongSoTien.setText(String.valueOf(donHang.getTongTien() + "VNĐ"));


      if( donHang.getTrangThai() == 1){
          holder.btnTrangThaiDonHang.setText("Đang Giao");
          holder.btnTrangThaiDonHang.setBackgroundResource(R.drawable.customer_button_trangthai_true);
      }
      holder.btnXemChiTiet.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              SharedPreferences sharedPreferences = context.getSharedPreferences("ChiTietDonHang",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("ngayDatHang",donHang.getNgayDatHang());
                editor.putString("tongTien",String.valueOf(donHang.getTongTien()));
                editor.apply();
              // Sử dụng Context từ itemView để start activity
              Intent i = new Intent(v.getContext(), ChiTietHoaDonAcitivity.class);
              v.getContext().startActivity(i);
          }
      });

    }
    @Override
    public int getItemCount() {
        return listDonHang.size();
    }
    static class DonHangViewHolder extends RecyclerView.ViewHolder{
        TextView tvNgayDat,tvTongSoTien;
        Button btnTrangThaiDonHang,btnXemChiTiet;



        public DonHangViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNgayDat = itemView.findViewById(R.id.tvNgayDat_DonHang);
            tvTongSoTien = itemView.findViewById(R.id.tvTongTien_DonHang);
            btnTrangThaiDonHang = itemView.findViewById(R.id.btnTrangThai_DonHang);
            btnXemChiTiet = itemView.findViewById(R.id.btn_XemChiTiet_DonHang);
        }
    }

}
