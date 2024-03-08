package com.example.banhang.View;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.banhang.R;
import com.example.banhang.View.RecyclerViewProduct.*;

public class DetailActivity extends AppCompatActivity {
    TextView tvGiaTien, tvNoiDungMoTa,tvTenSanPham;
    EditText edtSoLuong;
    Button btnMuaSanPham,btnPlus,btnBack;
    ImageView imgAnhSanPham;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_detail);
        //Ánh xạ view
        AnhXa();

        //Lấy dự liệu được intent qua từ ProductsAdapter
        String tenSanPham = getIntent().getStringExtra("tenSanPham");
        String anhSanPham = getIntent().getStringExtra("srcAnh");
        String moTa = getIntent().getStringExtra("mota");
        String giaTien = getIntent().getStringExtra("giaTien");
        int[] soLuong = {0};


        //Gan Dữ liêu cho detail
        imgAnhSanPham.setImageBitmap(Utils.convertToBitmapFromAssets(getApplication(),anhSanPham));
        tvTenSanPham.setText(tenSanPham);
        tvNoiDungMoTa.setText(moTa);
        tvGiaTien.setText(giaTien);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soLuong[0] <= 0 ){
                    soLuong[0] = 1;
                }else{
                    soLuong[0]++;

                }
                // Set the updated value to the EditText
                edtSoLuong.setText(String.valueOf(soLuong[0]));
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Khi Mua San Pham Can phai kiem tra thong tin khach hang da cap nhat thong tin day du chua
        //Neu Chua : chuyen sang trang cap nhat thong tin khach hang
        //Neu Roi : hien thi man hinh dialog Xac Nhan Mua
        // => luu du lieu vao database donHang va ...
        btnMuaSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    void AnhXa(){
        tvGiaTien = findViewById(R.id.tvGiaTien_Detail);
        tvNoiDungMoTa = findViewById(R.id.tvNoiDung_Detail);
        edtSoLuong = findViewById(R.id.edtSoLuong_Detail);
        btnMuaSanPham  = findViewById(R.id.btnMuaSanPham_Detail);
        tvTenSanPham = findViewById(R.id.tvTenSanPham_Detail);
        imgAnhSanPham = findViewById(R.id.imgAnhSanPham_Detail);
        btnPlus = findViewById(R.id.btnPlus);
        btnBack = findViewById(R.id.btnBack);
    }
}
