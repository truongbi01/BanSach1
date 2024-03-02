package com.example.banhang.View.fragment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.banhang.R;
import com.example.banhang.database.CreateDatabase;
import com.example.banhang.View.RecyclerViewCategory.*;
import com.example.banhang.View.RecyclerViewProduct.*;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductCategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductCategoryFragment newInstance(String param1, String param2) {
        ProductCategoryFragment fragment = new ProductCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
     EditText edtTenTheLoai;
    Button btnThem,btnDeleted;
    private CreateDatabase databaseHelper;
    RecyclerView rvListC;
    ArrayList<ProductsCategory> lstCategory;
    CategoryAdapter categoryAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_category, container, false);
        AnhXa(view);
        databaseHelper = new CreateDatabase(getActivity());
        LoadData(getActivity());
        categoryAdapter = new CategoryAdapter(lstCategory,databaseHelper);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvListC.setAdapter(categoryAdapter);
        rvListC.setLayoutManager(linearLayoutManager);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String TenTheLoai = edtTenTheLoai.getText().toString();
                if (TextUtils.isEmpty(TenTheLoai)) {
                    Toast.makeText(getActivity(), "Tên Thể Loại Đang Trống", Toast.LENGTH_SHORT).show();
                } else {
                    // Kiểm tra xem thể loại đã tồn tại chưa
                    if (!kiemTraTheLoaiTonTai(TenTheLoai)) {
                        themTheLoaiVaoDatabase(TenTheLoai);
                        Toast.makeText(getActivity(), "Thêm Thể Loại Thành Công", Toast.LENGTH_SHORT).show();
                        edtTenTheLoai.setText("");

                        // Tải lại dữ liệu và cập nhật RecyclerView
                        LoadData(getActivity());
                        categoryAdapter.setData(lstCategory);

                    } else {
                        Toast.makeText(getActivity(), "Thể Loại Đã Tồn Tại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                // Gọi phương thức xóa trong Adapter khi bấm nút xóa
                categoryAdapter.removeCategory(position);
                Toast.makeText(getActivity(), "Xóa Thể Loại Thành Công", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

     void AnhXa(View view){
        edtTenTheLoai = view.findViewById(R.id.edtTenTheLoai);
        btnThem = view.findViewById(R.id.btnThem);
         rvListC = view.findViewById(R.id.rvList);
        btnDeleted = view.findViewById(R.id.btnDeleted);
    }
    // Phương thức kiểm tra thể loại đã tồn tại chưa
    private boolean kiemTraTheLoaiTonTai(String tenTheLoai) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String query = "SELECT * FROM " + CreateDatabase.TB_LOAI_SAN_PHAM +
                " WHERE " + CreateDatabase.CL_TEN_LOAI_SAN_PHAM + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{tenTheLoai});

        boolean tonTai = cursor.getCount() > 0;

        // Đóng cursor và database
        cursor.close();
        db.close();

        return tonTai;
    }
    // Phương thức thêm thể loại vào database
    private void themTheLoaiVaoDatabase(String tenTheLoai) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CreateDatabase.CL_TEN_LOAI_SAN_PHAM, tenTheLoai);

        db.insert(CreateDatabase.TB_LOAI_SAN_PHAM, null, values);

        // Đóng kết nối database
        db.close();
    }

    void LoadData(Context context){
        lstCategory = Utils.LoadDataCategory(context);
    }
}