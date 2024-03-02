package com.example.banhang.View.fragment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private TextView tvYourName, tvThongBao;
    private EditText edtHoVaTen, edtEmail, edtSoDienThoai, edtDiaChi, edtNgaySinh, edtCMND;
    private Button btnSave;
    private CreateDatabase databaseHelper;
    private SQLiteDatabase database;
    private LinearLayout lnThongTinDangNhap, lnCapNhatThongTinKhachHang;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        AnhXa(view);
        databaseHelper = new CreateDatabase(getActivity());

        SharedPreferences prefs = getActivity().getSharedPreferences("ShareData", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("tk_mk login", Context.MODE_PRIVATE);
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
                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        } finally {
                            if (database != null && database.isOpen()) {
                                database.close();
                            }
                        }
                    }
                    reloadFragment();
                }
            });
        }
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
            Toast.makeText(getActivity(), "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Không tìm thấy thông tin cần cập nhật", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput(String hoVaTen, String email, String soDienThoai, String diaChi) {
        if (!isValidName(hoVaTen)) {
            Toast.makeText(getActivity(), "Các chữ cái đầu phải viết hoa để tôn trọng tên của bạn <3", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValidEmail(email)) {
            Toast.makeText(getActivity(), "Email không đúng định dạng", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValidPhone(soDienThoai)) {
            Toast.makeText(getActivity(), "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (diaChi.equals("")) {
            Toast.makeText(getActivity(), "Địa chỉ đang rỗng", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateInput(String ngaysinh, String cmnd) {
        if (!isValidDateOfBirth(ngaysinh)) {
            Toast.makeText(getActivity(), "NgaySin", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValidCMND(cmnd)) {
            Toast.makeText(getActivity(), "CMND không đúng định dạng", Toast.LENGTH_SHORT).show();
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
        databaseHelper = new CreateDatabase(getActivity());
        lnCapNhatThongTinKhachHang = view.findViewById(R.id.lnCapNhatThongTinKhachHang);
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidDateOfBirth(String dateOfBirth) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
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
    private void updateUI(String hoVaTen, String email, String soDienThoai, String diaChi,String ngaysinh, String cmnd) {
        edtHoVaTen.setText(hoVaTen);
        edtEmail.setText(email);
        edtSoDienThoai.setText(soDienThoai);
        edtDiaChi.setText(diaChi);
        edtNgaySinh.setText(ngaysinh);
        edtCMND.setText(cmnd);
    }

}

