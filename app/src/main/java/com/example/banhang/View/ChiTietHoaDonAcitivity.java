package com.example.banhang.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banhang.R;
import com.example.banhang.View.RecycleViewDonHang.*;
import com.example.banhang.View.RecyclerViewProduct.*;
import com.example.banhang.database.*;


import java.util.ArrayList;

public class ChiTietHoaDonAcitivity extends AppCompatActivity {
    TextView tvThongTinKhachHang , tvThongTinSoDienThoai,tvThongTinDiaChi , tvTongTien;
    RecyclerView rcvDonHang_ChiTiet;
    DonHangChiTietAdapter donHangChiTietAdapter;
    ArrayList<Products> listDonHang ;
    CreateDatabase createDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chitiethoadon_activity);
        AnhXa();
        createDatabase = new CreateDatabase(getBaseContext());
        SharedPreferences shareThongTinNguoiDung = this.getSharedPreferences("tk_mk login",MODE_PRIVATE);
        String tenDangNhap = shareThongTinNguoiDung.getString("Username",null);
        String soDienThoai = createDatabase.GetCLValueFromTenDangNhap(tenDangNhap,CreateDatabase.CL_SO_DIEN_THOAI);
        String tenKhachHang = createDatabase.GetCLValueFromTenDangNhap(tenDangNhap,CreateDatabase.CL_TEN_KHACH_HANG);
        String diachi = createDatabase.GetCLValueFromTenDangNhap(tenDangNhap,CreateDatabase.CL_DIA_CHI);
        SharedPreferences sharedPreferences = this.getSharedPreferences("ChiTietDonHang", MODE_PRIVATE);
        String ngayDatHang =  sharedPreferences.getString("ngayDatHang",null);
        String tongTien = sharedPreferences.getString("tongTien",null);
        // Load danh sách sản phẩm từ cơ sở dữ liệu
        listDonHang = Utils.LoadDataOrderDetails(this, ngayDatHang, tongTien);
        donHangChiTietAdapter = new DonHangChiTietAdapter(this,listDonHang);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvDonHang_ChiTiet.setAdapter(donHangChiTietAdapter);
        rcvDonHang_ChiTiet.setLayoutManager(linearLayoutManager);
        tvTongTien.setText(tongTien);
        tvThongTinKhachHang.setText(tenKhachHang);
        tvThongTinSoDienThoai.setText(soDienThoai);
        tvThongTinDiaChi.setText(diachi);

    }
    void AnhXa(){
        tvThongTinKhachHang = findViewById(R.id.tvThongTinTenKhachHang_DonHang_ChiTiet);
        tvThongTinSoDienThoai = findViewById(R.id.tvThongTinSoDienThoai_DonHang_ChiTiet);
        tvThongTinDiaChi = findViewById(R.id.tvThongTinDiaChi_DonHang_ChiTiet);
        tvTongTien = findViewById(R.id.tvTongTien_DonHang_ChiTiet);
        rcvDonHang_ChiTiet = findViewById(R.id.rcvDonHang_ChiTiet);
    }

}
