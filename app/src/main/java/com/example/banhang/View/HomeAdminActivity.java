package com.example.banhang.View;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.banhang.Login.KhungDangNhapActivity;
import com.example.banhang.R;
import com.example.banhang.View.fragment.*;
import com.example.banhang.database.CreateDatabase;
import com.google.android.material.navigation.NavigationView;

public class HomeAdminActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_admin);
        AnhXa();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        //
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Main");
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) { // Đảm bảo fragment chỉ được thêm khi Activity được tạo lần đầu tiên
            Fragment fmNew = new HomeFragment();
            loadFragment(fmNew);
            actionBar.setTitle("Home");

        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fmNew;
                int menuID = item.getItemId();
                if (menuID == R.id.miHome) {
                    getSupportActionBar().setTitle(item.getTitle());
                    fmNew = new HomeFragment();
                    loadFragment(fmNew);
                    return true;

                } else if (menuID == R.id.miFavorite) {
                  getSupportActionBar().setTitle(item.getTitle());
                  fmNew = new FavoriteFragment();
                  loadFragment(fmNew);
                    return true;
                } else if (menuID == R.id.miRecommend) {
                    Toast.makeText(HomeAdminActivity.this, "Recommend", Toast.LENGTH_SHORT).show();
                    return true;

                }else if (menuID == R.id.mnAbout) {
                    getSupportActionBar().setTitle(item.getTitle());
                    fmNew = new AboutFragment();
                    loadFragment(fmNew);
                    return true;

                }else if (menuID == R.id.mnLogout) {
                    Intent intent = new Intent(HomeAdminActivity.this, KhungDangNhapActivity.class);
                    startActivity(intent);
                    return true;

                }else if (menuID == R.id.mnAddProduct) {
                    getSupportActionBar().setTitle(item.getTitle());
                    fmNew = new ProductFragment();
                    loadFragment(fmNew);
                    return true;

                }else if (menuID == R.id.mnThemLoaiSanPham) {
                    getSupportActionBar().setTitle(item.getTitle());
                    fmNew = new ProductCategoryFragment();
                    loadFragment(fmNew);
                    return true;

                }else if (menuID == R.id.mnDLTAccount) {
                    showDeleteAccountDialog();
                    return true;

                }
                else {
                    return true;

                }
            }
        });
    }
    void AnhXa() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
    }
    // Hàm hiển thị Dialog xác nhận xóa tài khoản
    private void showDeleteAccountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác Nhận Xóa Tài Khoản");
        builder.setMessage("Bạn có chắc muốn xóa tài khoản?");

        // Nút xác nhận xóa tài khoản
        builder.setPositiveButton("Xác Nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Gọi hàm xóa tài khoản và chuyển về màn hình đăng nhập
                deleteAccount();
            }
        });

        // Nút hủy bỏ
        builder.setNegativeButton("Hủy Bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Hiển thị Dialog
        builder.show();
    }
    // Hàm xóa tài khoản
    private void deleteAccount() {
        // Mở kết nối với cơ sở dữ liệu
        CreateDatabase databaseHelper = new CreateDatabase(HomeAdminActivity.this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Lấy CMND của người dùng hiện tại
        SharedPreferences prefs = getSharedPreferences("ShareData", Context.MODE_PRIVATE);
        String cmnd = prefs.getString("cmnd", "");

        try {
            // Bắt đầu một giao dịch
            db.beginTransaction();

            // Xóa từ bảng TB_DANG_NHAP_KHACH_HANG
            db.delete(
                    CreateDatabase.TB_DANG_NHAP_KHACH_HANG,
                    CreateDatabase.CL_CMND + "=?",
                    new String[]{cmnd}
            );



            // Gọi setTransactionSuccessful() để đánh dấu rằng giao dịch thành công
            db.setTransactionSuccessful();

            // Nếu bạn đến được đến đây mà không có ngoại lệ nào, hãy đóng giao dịch
        } catch (Exception e) {
            // Xử lý ngoại lệ (nếu có)
            e.printStackTrace();
        } finally {
            // Kết thúc giao dịch, đảm bảo rằng giao dịch sẽ được đóng ngay cả khi có ngoại lệ xảy ra
            db.endTransaction();

            // Đóng kết nối với cơ sở dữ liệu
            db.close();
        }

        // Chuyển về màn hình đăng nhập
        Intent intent = new Intent(HomeAdminActivity.this, KhungDangNhapActivity.class);
        startActivity(intent);
        finish(); // Đóng màn hình HomeActivity sau khi chuyển đến màn hình đăng nhập
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //ham load Fragment
    void loadFragment(Fragment fmNew){
        FragmentTransaction fmTran = getSupportFragmentManager().beginTransaction();
        fmTran.replace(R.id.mainFragment, fmNew);
        fmTran.addToBackStack(null);
        fmTran.commit();

    }

}
