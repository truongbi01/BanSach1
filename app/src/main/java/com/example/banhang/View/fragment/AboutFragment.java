package com.example.banhang.View.fragment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.banhang.R;
import com.example.banhang.database.CreateDatabase;
import com.example.banhang.View.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AboutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private TextView tvYourName, tvThongBao,tvXacThucSoDienThoai;
    private EditText edtHoVaTen, edtEmail, edtSoDienThoai, edtDiaChi, edtNgaySinh, edtCMND;
    private Button btnSave;
    private CreateDatabase databaseHelper;
    private SQLiteDatabase database;


    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        context = getContext(); // 17405
        AnhXa(view);
        databaseHelper = new CreateDatabase(getContext());
        SharedPreferences sharedPreferences1 = getContext().getSharedPreferences("ThongTinNguoiDung",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putString("tenKhachHang",edtHoVaTen.getText().toString());
        editor1.apply();
        SharedPreferences prefs = getContext().getSharedPreferences("ShareData", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("tk_mk login", Context.MODE_PRIVATE);
        String tenDangNhap = sharedPreferences.getString("hovaten", "");
        String ngaySinhP = prefs.getString("ngaySinh", "");
        String cmnd = prefs.getString("cmnd", "");

        tvYourName.setText(tenDangNhap);
        edtNgaySinh.setText(ngaySinhP);
        edtCMND.setText(cmnd);
        String hoVaTendb = databaseHelper.GetCLHoVaTenKhachHang(cmnd);
        String emaildb = databaseHelper.GetCLEmailKhachHang(cmnd);
        String soDienThoaidb = databaseHelper.GetCLSDTKhachHang(cmnd);
        String diaChidb = databaseHelper.GetCLDiaChiKhachHang(cmnd);
        String ngaySinhdb = databaseHelper.GetCLNgaySinh(cmnd);
        String cmndDb = databaseHelper.GetCLCMND(cmnd);
        // String trangthai = null;

        if (hoVaTendb == null || emaildb == null || soDienThoaidb == null || diaChidb == null) {
            tvThongBao.setText("Thông Tin Cần Cập Nhật*");
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String hoVaTenL = edtHoVaTen.getText().toString();
                        String emailL = edtEmail.getText().toString();
                        String sodienThoaiL = edtSoDienThoai.getText().toString();
                        String diaChiL = edtDiaChi.getText().toString();
                        String ngaysinhU = edtNgaySinh.getText().toString();
                        String cmndU = edtCMND.getText().toString();

                        if (validateInput(hoVaTenL, emailL, sodienThoaiL, diaChiL) && validateInput(ngaySinhP, cmnd)) {
                            try {
                                database = databaseHelper.getWritableDatabase();
                                CapNhatThongTinKhachHang(hoVaTenL, emailL, sodienThoaiL, diaChiL,ngaysinhU,cmndU);
                                tvThongBao.setText("");


                            } catch (Exception e) {
                                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            } finally {
                                if (database != null && database.isOpen()) {
                                    database.close();

                                }
                            }
                        }
                        reloadFragment();
                    }
                });

        } else {
            // Xu Ly khi bam vao chinh sua
            updateUI(hoVaTendb,emaildb,soDienThoaidb,diaChidb,ngaySinhdb,cmndDb);
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String hoVaTenL = edtHoVaTen.getText().toString();
                    String emailL = edtEmail.getText().toString();
                    String sodienThoaiL = edtSoDienThoai.getText().toString();
                    String diaChiL = edtDiaChi.getText().toString();
                    String ngaysinhU = edtNgaySinh.getText().toString();
                    String cmndU = edtCMND.getText().toString();

                    if (validateInput(hoVaTenL, emailL, sodienThoaiL, diaChiL) && validateInput(ngaySinhP, cmnd)) {
                        try {
                            database = databaseHelper.getWritableDatabase();
                            CapNhatThongTinKhachHang(hoVaTenL, emailL, sodienThoaiL, diaChiL,ngaysinhU,cmndU);
                            tvThongBao.setText("");

                        } catch (Exception e) {
                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        } finally {
                            if (database != null && database.isOpen()) {
                                database.close();
                            }
                        }
                    }
                    reloadFragment();
                }
            });
            edtSoDienThoai.setTextColor(Color.RED);

        }
        tvXacThucSoDienThoai.setOnClickListener(v -> {
            editor1.putString("soDienThoai", soDienThoaidb);
            editor1.apply();
            Intent i = new Intent(context, VerifyActivity.class);
            startActivity(i);
            Toast.makeText(context,"Hệ thống đang cập nhật chức năng này",Toast.LENGTH_SHORT).show();
        });
        return view;
    }

    private void reloadFragment() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(this);
        fragmentTransaction.attach(this);
        fragmentTransaction.commit();
    }
    void loadFragment(Fragment fmNew){
        FragmentTransaction fmTran = getActivity().getSupportFragmentManager().beginTransaction();
        fmTran.replace(R.id.mainFragment, fmNew);
        fmTran.addToBackStack(null);
        fmTran.commit();

    }



    private void CapNhatThongTinKhachHang(String hoVaTen, String email, String soDienThoai, String diaChi,String ngaySinh, String cmnd) {
        ContentValues values = new ContentValues();
        values.put(CreateDatabase.CL_TEN_KHACH_HANG, hoVaTen);
        values.put(CreateDatabase.CL_EMAIL, email);
        values.put(CreateDatabase.CL_SO_DIEN_THOAI, soDienThoai);
        values.put(CreateDatabase.CL_DIA_CHI, diaChi);
        values.put(CreateDatabase.CL_NGAYSINH, ngaySinh);
        values.put(CreateDatabase.CL_CMND, cmnd);

        int rowsAffected =    database.update(
                CreateDatabase.TB_DANG_NHAP_KHACH_HANG,
                values,
                CreateDatabase.CL_CMND + "=?",
                new String[]{cmnd}
        );

        if (rowsAffected > 0) {
            Toast.makeText(getContext(), "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getContext(), "Không tìm thấy thông tin cần cập nhật", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput(String hoVaTen, String email, String soDienThoai, String diaChi) {
        if (!isValidName(hoVaTen)) {
            edtHoVaTen.setError("Tên không được chứa ký tự đặc biệt hoặc số");
            return false;
        } else if (!isValidEmail(email)) {
            edtEmail.setError("Email không đúng định dạng");
            return false;
        } else if (!isValidPhone(soDienThoai)) {
            edtSoDienThoai.setError("Số điện thoại không hợp lệ");
            return false;
        } else if (diaChi.equals("")) {
            edtDiaChi.setError("Địa chỉ đang rỗng");
            return false;
        }
        return true;
    }

    private boolean validateInput(String ngaysinh, String cmnd) {
        if (!isValidDateOfBirth(ngaysinh)) {
            Toast.makeText(getContext(), "Ngay sinh không đúng định dạng dd/mm/yyyy", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValidCMND(cmnd)) {
            Toast.makeText(getContext(), "CMND không đúng định dạng", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void AnhXa(View view) {
        tvYourName = view.findViewById(R.id.tvYourName);
        tvThongBao = view.findViewById(R.id.tvThongBao);
        edtHoVaTen = view.findViewById(R.id.edtHoVaTen);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtSoDienThoai = view.findViewById(R.id.edtSoDienThoai);
        edtDiaChi = view.findViewById(R.id.edtDiaChi);
        edtNgaySinh = view.findViewById(R.id.edtNgaySinh);
        edtCMND = view.findViewById(R.id.edtCMND);
        btnSave = view.findViewById(R.id.btnSave);
        databaseHelper = new CreateDatabase(getContext());
        tvXacThucSoDienThoai = view.findViewById(R.id.tvXacThucSoDienThoai);
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        Pattern pattern = Pattern.compile(emailRegex);

        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public static boolean isValidDateOfBirth(String dateOfBirth) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);

        try {
            Date parsedDate = sdf.parse(dateOfBirth);

            if (!dateOfBirth.equals(sdf.format(parsedDate))) {
                return false;
            }

            Date currentDate = new Date();
            if (parsedDate.after(currentDate)) {
                return false;
            }

        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    public static boolean isValidName(String name) {
        // Kiểm tra xem tên có chứa chữ số không
        if (name.matches(".*\\d.*")) {
            return false;
        }

        // Kiểm tra xem tên có chứa ký tự đặc biệt không
        if (name.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};\\\\|,.<>\\/?].*")) {
            return false;
        }

        return true;
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
    private void updateUI(String hoVaTen, String email, String soDienThoai, String diaChi,String ngaysinh, String cmnd) {
        edtHoVaTen.setText(hoVaTen);
        edtEmail.setText(email);
        edtSoDienThoai.setText(soDienThoai);
        edtDiaChi.setText(diaChi);
        edtNgaySinh.setText(ngaysinh);
        edtCMND.setText(cmnd);
    }

}

