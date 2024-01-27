package com.example.banhang.View.HeaderMenu;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.banhang.R;
import com.example.banhang.database.CreateDatabase;


public class AboutMenuActivity extends AppCompatActivity {
    TextView tvYourName , tvThongBao, tvNgaysinh , tvCCCD;
    EditText edtHoVaTen , edtEmail, edtSoDienThoai,edtDiaChi,edtNgaySinh,edtCMND;
    Button btnSave,btnChange;
    CreateDatabase databaseHelper;
    SQLiteDatabase database ;

    LinearLayout lnThongTinDangNhap, lnChinhSuaThongTinDangNhap;
    boolean isEditing = false;// đang trong trạng thái để cập nhật thông tin khách hàng
    boolean editFirst = false;// nếu là việc cập nhật thông tin đầu tiên chỉ xuất hiện 1 button lưu
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_about);
        //Ánh Xa
        AnhXa();
        databaseHelper = new CreateDatabase(this);
        database = databaseHelper.getWritableDatabase();
        String thongBao = tvThongBao.getText().toString();
        if(thongBao.equals("Thông tin cần cập nhật *")){
            editFirst = true;
            toggleEditMode(editFirst);
        }
        toggleEditMode(isEditing);



        SharedPreferences prefs = getSharedPreferences("ShareData", MODE_PRIVATE);
        String ngaySinh = prefs.getString("ngaySinh", "");
        String cmnd = prefs.getString("cmnd", "");

        tvNgaysinh.setText(ngaySinh);
        tvCCCD.setText(cmnd);
        edtNgaySinh.setText(ngaySinh);
        edtCMND.setText(cmnd);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoVaTen = edtHoVaTen.getText().toString();
                String email = edtEmail.getText().toString();
                String soDienThoai = edtSoDienThoai.getText().toString();
                String diaChi = edtDiaChi.getText().toString();
                String thongBao = tvThongBao.getText().toString();
            }
        });
    }



    //Ánh Xạ
    void AnhXa(){
        tvYourName = findViewById(R.id.tvYourName);
        tvThongBao = findViewById(R.id.tvThongBao);
        edtHoVaTen = findViewById(R.id.edtHoVaTen);
        edtEmail   = findViewById(R.id.edtEmail);
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtNgaySinh = findViewById(R.id.edtNgaySinh);
        edtCMND = findViewById(R.id.edtCMND);
        btnSave = findViewById(R.id.btnSave);
        tvNgaysinh = findViewById(R.id.tvNgaySinh);
        tvCCCD = findViewById(R.id.tvCCCD);
        lnThongTinDangNhap = findViewById(R.id.lnThongTinDangNhap);
        lnChinhSuaThongTinDangNhap = findViewById(R.id.lnChinhSuaThongTinDangNhap);
        btnChange = findViewById(R.id.btnChange);
    }
    private void CapNhatThongTinKhachHang(String hoVaTen , String email, String soDienThoai , String diaChi){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        if (!isThongTinKhachHang(hoVaTen,email)) {
            ContentValues values = new ContentValues();
            values.put(CreateDatabase.CL_TEN_KHACH_HANG, hoVaTen);
            values.put(CreateDatabase.CL_email, email);
            values.put(CreateDatabase.CL_SO_DIEN_THOAI, soDienThoai);
            values.put(CreateDatabase.CL_DIA_CHI, diaChi);

            // Cập Nhật dữ liệu vào bảng TB_KHACH_HANG
            db.update(
                    CreateDatabase.TB_KHACH_HANG,
                    values,
                    CreateDatabase.CL_TEN_KHACH_HANG + "=? AND " + CreateDatabase.CL_email + "=?",
                    new String[]{hoVaTen, email}
            );

            Toast.makeText(getApplicationContext(), "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();

        }else {
            // Xử lý khi thông tin đã tồn tại
            Toast.makeText(this, "Thông Tin Đã Tồn Tại", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isThongTinKhachHang(String hoVaTenKhachHang, String email){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + CreateDatabase.TB_KHACH_HANG +
                " WHERE " + CreateDatabase.CL_TEN_KHACH_HANG + "=? OR " + CreateDatabase.CL_email + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{hoVaTenKhachHang, email});
        boolean result = cursor.getCount() > 0;
        cursor.close();

        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Đóng đối tượng SQLiteDatabase khi Activity bị hủy

    }
    private void toggleEditMode(boolean isEdit){
        isEditing = isEdit;
        editFirst = isEdit;
        //nếu là lần chỉnh sửa đầu tiên có chữ thông tin cần câp nhật
        if(editFirst){
            btnChange.setVisibility(View.GONE);
        }
        else {
            btnChange.setVisibility(View.VISIBLE);
        }
        if(isEditing){
            lnThongTinDangNhap.setVisibility(View.GONE);
        }
        else{
            lnChinhSuaThongTinDangNhap.setVisibility(View.GONE);
        }
    }
}
