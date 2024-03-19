package com.example.banhang.View.RecycleViewDonHang;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banhang.R;
import com.example.banhang.View.RecyclerViewProductFavorite.ProductsFavoriteAdapter;
import com.example.banhang.database.CreateDatabase;

import java.util.ArrayList;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.DonHangViewHolder> {
    ArrayList<DonHang> listDonHang ;
    Context context;
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
        //Gan Du lieu
    }
    @Override
    public int getItemCount() {
        return listDonHang.size();
    }
    static class DonHangViewHolder extends RecyclerView.ViewHolder{
        ImageView imgAnhSanPham ;
        TextView tvTenSanPham,tvSoLuong,tvNgayDat,tvTongSoTien;
        Button btnTrangThaiDonHang;



        public DonHangViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAnhSanPham =itemView.findViewById(R.id.imgAnhSanPham_DonHang);
            tvTenSanPham = itemView.findViewById(R.id.tvTenSanPham_Donhang);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong_DonHang);
            tvNgayDat = itemView.findViewById(R.id.tvNgayDat_DonHang);
            tvTongSoTien = itemView.findViewById(R.id.tvTongTien_DonHang);
            btnTrangThaiDonHang = itemView.findViewById(R.id.btnTrangThai_DonHang);
        }
    }
}
