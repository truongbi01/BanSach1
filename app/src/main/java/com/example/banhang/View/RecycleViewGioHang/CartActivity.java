package com.example.banhang.View.RecycleViewGioHang;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banhang.R;
import com.example.banhang.View.RecyclerViewProduct.*;
import com.example.banhang.View.RecyclerViewProductFavorite.ProductsFavoriteAdapter;
import com.example.banhang.database.CreateDatabase;
import com.example.banhang.View.RecycleViewGioHang.*;
import java.util.ArrayList;
import com.example.banhang.View.fragment.*;

public class CartActivity extends AppCompatActivity implements CartAdapter.TotalAmountListener {
    ArrayList<Products> lstProductsCart;
    CartAdapter cartAdapter;
    CreateDatabase databaseHelper;
    SQLiteDatabase database;
    RecyclerView rcvCart;
    CheckBox cbChonTatCa;
    TextView tvTongTien;
    Button btnMuaHang;
    ImageView imgBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cart);
        AnhXa();
        LoadDataProducts(CartActivity.this);
        databaseHelper = new CreateDatabase(CartActivity.this);
        database  = databaseHelper.getWritableDatabase();
        cartAdapter = new CartAdapter(lstProductsCart,databaseHelper, this);
        rcvCart.setAdapter(cartAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvCart.setLayoutManager(linearLayoutManager);

        cbChonTatCa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Chọn hoặc bỏ chọn tất cả các sản phẩm
                cartAdapter.selectAll(isChecked);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    void AnhXa(){
        rcvCart = findViewById(R.id.rcvGiohang);
        cbChonTatCa = findViewById(R.id.checkboxSelectAll);
        tvTongTien = findViewById(R.id.tvTongTien);
        btnMuaHang = findViewById(R.id.btnMuaHang);
        imgBack = findViewById(R.id.imgBack_Cart);
    }
    void LoadDataProducts(Context context){
        lstProductsCart = Utils.LoadDaTaProductsCart(context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTotalAmountChanged(double totalAmount) {
        tvTongTien.setText("Tổng Tiền: " + totalAmount + " VND");
    }
}
