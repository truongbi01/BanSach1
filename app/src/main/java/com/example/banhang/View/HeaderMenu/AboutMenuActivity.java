package com.example.banhang.View.HeaderMenu;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.banhang.R;
import com.example.banhang.database.CreateDatabase;
import com.example.banhang.View.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AboutMenuActivity extends AppCompatActivity {
    TextView tvYourName , tvThongBao, tvNgaysinh , tvCCCD, tvHoVaTen,tvEmail,tvSoDienThoai,tvDiaChi;
    EditText edtHoVaTen , edtEmail, edtSoDienThoai,edtDiaChi,edtNgaySinh,edtCMND;
    Button btnSave,btnChange;
    CreateDatabase databaseHelper;
    SQLiteDatabase database ;

    LinearLayout lnThongTinDangNhap, lnChinhSuaThongTinDangNhap,lnChinhSuaThongTinKhachHang,lnCapNhatThongTinKhachHang;
    boolean isEditing = false;// đang trong trạng thái để cập nhật thông tin khách hàng
    boolean editFirst = false;// nếu là việc cập nhật thông tin đầu tiên chỉ xuất hiện 1 button lưu
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_about);
        //Ánh Xa
        AnhXa();
        databaseHelper = new CreateDatabase(this);
        database = databaseHelper.getWritableDatabase();

        // Lấy thông tin đăng nhập từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("ShareData", MODE_PRIVATE);
        String ngaySinhP = prefs.getString("ngaySinh", "");
        String cmnd = prefs.getString("cmnd", "");
        SharedPreferences sharedPreferences = getSharedPreferences("tk_mk login", MODE_PRIVATE);
        String tenDangNhap = sharedPreferences.getString("hovaten", "");
        tvYourName.setText(tenDangNhap);
        tvNgaysinh.setText(ngaySinhP);
        tvCCCD.setText(cmnd);
        edtNgaySinh.setText(ngaySinhP);
        edtCMND.setText(cmnd);



        // Lấy dữ liệu từ DB
        String hoVaTendb = databaseHelper.GetCLHoVaTenKhachHang(cmnd);
        String emaildb = databaseHelper.GetCLEmailKhachHang(cmnd);
        String soDienThoaidb = databaseHelper.GetCLSDTKhachHang(cmnd);
        String diaChidb = databaseHelper.GetCLDiaChiKhachHang(cmnd);

        updateUI(hoVaTendb, emaildb, soDienThoaidb, diaChidb);
        //Tạo thông tin khách hàng nếu còn null
        if(hoVaTendb== null || emaildb == null||soDienThoaidb == null||diaChidb == null){
            Toast.makeText(getApplicationContext(),"Cập Nhật lần đầu",Toast.LENGTH_SHORT).show();
            tvThongBao.setText("Thông Tin Cần Cập Nhật*");
            editFirst = true;
           toggleEditMode(editFirst); // Hiển thị nút lưu
           toggleEditMode(isEditing); // đang được chỉnh sửa
            if (btnSave != null){

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Lấy dữ liệu từ layout
                        String hoVaTenL = edtHoVaTen.getText().toString();
                        String emailL = edtEmail.getText().toString();
                        String sodienThoaiL = edtSoDienThoai.getText().toString();
                        String diaChiL = edtDiaChi.getText().toString();
                        String ngaySinh = edtNgaySinh.getText().toString();
                        String CMND = edtCMND.getText().toString();

                        if(!isValidName(hoVaTenL)){
                            Toast.makeText(getApplicationContext(),"Các chữ cái đầu phải viết hoa để tôn trọng tên của bạn <3",Toast.LENGTH_SHORT).show();
                        } else if (!isValidEmail(emailL)) {
                            Toast.makeText(getApplicationContext(),"Email không đúng định dạng",Toast.LENGTH_SHORT).show();
                        } else if (!isValidPhone(sodienThoaiL)) {
                            Toast.makeText(getApplicationContext(),"Số điện thoại không hợp lệ",Toast.LENGTH_SHORT).show();
                        } else if (diaChiL.equals("")) {
                            Toast.makeText(getApplicationContext(),"Địa chỉ đang rỗng",Toast.LENGTH_SHORT).show();
                        } else if (ngaySinh.equals("")) {
                            Toast.makeText(getApplicationContext(),"Ngày sinh đang rỗng",Toast.LENGTH_SHORT).show();
                        } else if (!isValidCMND(CMND)) {
                            Toast.makeText(getApplicationContext(),"CCCD không hợp lệ",Toast.LENGTH_SHORT).show();

                        } else{
                            try {
                                CapNhatThongTinKhachHang(hoVaTenL,emailL,sodienThoaiL,diaChiL);
                                tvThongBao.setText("");
                                Intent intent = new Intent(AboutMenuActivity.this,HomeActivity.class);
                                startActivity(intent);
                            }
                            catch ( Exception e){
                                Toast toast = Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT);toast.show();

                            }
                        }

                    }
                });
            }else{
                Toast toast = Toast.makeText(getApplicationContext(),"Không tìm thấy nút save",Toast.LENGTH_SHORT);toast.show();

            }
        }//Ngược lại thực nếu đã tồn tại rồi thi khi chỉnh sủa xong sẽ là update
        else {
            Toast.makeText(getApplicationContext(),"Cập Nhật lần 2",Toast.LENGTH_SHORT).show();
            toggleEditMode(isEditing);
            toggleEditMode(editFirst); // Hiển thị nút lưu và chỉnh sửa
        }


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
        lnCapNhatThongTinKhachHang = findViewById(R.id.lnCapNhatThongTinKhachHang);
        lnChinhSuaThongTinKhachHang = findViewById(R.id.lnChinhSuaThongTinKhachHang);
        tvHoVaTen =findViewById(R.id.tvHoVaTen);
        tvEmail =findViewById(R.id.tvEmail);
        tvSoDienThoai = findViewById(R.id.tvSoDienThoai);
        tvDiaChi = findViewById(R.id.tvDiaChi);
    }
    private void CapNhatThongTinKhachHang(String hoVaTen, String email, String soDienThoai, String diaChi) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CreateDatabase.CL_TEN_KHACH_HANG, hoVaTen);
        values.put(CreateDatabase.CL_EMAIL, email);
        values.put(CreateDatabase.CL_SO_DIEN_THOAI, soDienThoai);
        values.put(CreateDatabase.CL_DIA_CHI, diaChi);

        int rowsAffected = db.update(
                CreateDatabase.TB_DANG_NHAP_KHACH_HANG,
                values,
                CreateDatabase.CL_CMND + "=?",
                new String[]{edtCMND.getText().toString()}
        );

        if (rowsAffected > 0) {
            Toast.makeText(getApplicationContext(), "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
        } else {
            // Xử lý khi không có bản ghi nào được cập nhật (không tồn tại CMND)
            Toast.makeText(this, "Không tìm thấy thông tin cần cập nhật", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }





    private void CapNhatNgaySinhVaCMND(String ngaySinh, String cmnd) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CreateDatabase.CL_NGAYSINH, ngaySinh);
        values.put(CreateDatabase.CL_CMND, cmnd);



        db.update(
                CreateDatabase.TB_DANG_NHAP_KHACH_HANG,
                values,
                CreateDatabase.CL_CMND + "=?",
                new String[]{cmnd}
        );

        Toast.makeText(getApplicationContext(), "Cập Nhật Ngày Sinh và CMND Thành Công", Toast.LENGTH_SHORT).show();

        db.close();
    }


    private boolean isThongTinKhachHang(String cmnd){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + CreateDatabase.TB_DANG_NHAP_KHACH_HANG +
                " WHERE " + CreateDatabase.CL_CMND + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{cmnd});
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
            lnCapNhatThongTinKhachHang.setVisibility(View.VISIBLE);
            lnChinhSuaThongTinKhachHang.setVisibility(View.GONE);
        }
        else{
            lnChinhSuaThongTinDangNhap.setVisibility(View.GONE);
            lnCapNhatThongTinKhachHang.setVisibility(View.GONE);

        }
    }

    //Valid Dữ liệu
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isValidName(String name) {
        String nameRegex = "^[A-Z][a-z]*( [A-Z][a-z]*)*$";
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
    public static boolean isValidPhone(String phone) {
        String phoneRegex = "^[0-9]{10}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
    public static boolean isValidCMND(String cmnd) {
        String cmndRegex = "^[0-9]{12}$";
        Pattern pattern = Pattern.compile(cmndRegex);
        Matcher matcher = pattern.matcher(cmnd);
        return matcher.matches();
    }
    @SuppressLint("SetTextI18n")
    private void updateUI(String hoVaTen, String email, String soDienThoai, String diaChi) {
        tvHoVaTen.setText("Họ Và Tên :"+hoVaTen);
        tvEmail.setText("Email: "+email);
        tvSoDienThoai.setText("Số Điện Thoại : " +soDienThoai);
        tvDiaChi.setText("Địa Chỉ : "+diaChi);
    }

}
