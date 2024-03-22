package com.example.banhang.View.RecycleViewDonHang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.banhang.R;
import com.example.banhang.database.*;
import com.example.banhang.View.RecyclerViewProduct.*;

import java.util.ArrayList;

public class DonHangChiTietAdapter extends RecyclerView.Adapter<DonHangChiTietAdapter.DonHangChiTietViewHolder> {
    ArrayList<Products> listDonHang ;
    CreateDatabase createDatabase;
    Context context;
    public DonHangChiTietAdapter(Context context,ArrayList<Products> listDonHang ){
        this.context = context;
        this.listDonHang = listDonHang;

    }
    @NonNull
    @Override
    public DonHangChiTietViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        // Tạo View
        View view = layoutInflater.inflate(R.layout.layout_item_don_hang_chi_tiet,parent,false);
        DonHangChiTietAdapter.DonHangChiTietViewHolder donHangChiTietViewHolder = new  DonHangChiTietAdapter.DonHangChiTietViewHolder(view);
        return donHangChiTietViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangChiTietViewHolder holder, int position) {
    CreateDatabase createDatabase1 = new CreateDatabase(context);
        Products item = listDonHang.get(position);

     //gán dữ liệu vào view
        holder.tvTenSanPham.setText(item.getName());
        holder.tvMoTa.setText(item.getDes());
        holder.tvSoLuong.setText(String.valueOf(item.getSoLuong()));
        holder.tvGiaTien.setText(item.getPrice() + "VNĐ");
        holder.imgAnhSanPham.setImageBitmap(Utils.convertToBitmapFromAssets(context,item.getImage()));

    }

    @Override
    public int getItemCount() {
        return listDonHang.size();
    }

    static class DonHangChiTietViewHolder extends RecyclerView.ViewHolder{
        TextView tvTenSanPham , tvSoLuong, tvMoTa,tvGiaTien;
        ImageView imgAnhSanPham;
        public DonHangChiTietViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenSanPham = itemView.findViewById(R.id.tvTenSanPham_ChiTiet_DonHang);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong_ChiTiet_DonHang);
            tvMoTa = itemView.findViewById(R.id.tvMoTa_ChiTiet_DonHang);
            tvGiaTien = itemView.findViewById(R.id.tvGiaTien_ChiTiet_DonHang);
            imgAnhSanPham = itemView.findViewById(R.id.imgAnhSanPham_ChiTiet_DonHang);
        }
    }
}
