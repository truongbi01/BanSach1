package com.example.banhang.View.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.banhang.R;
import com.example.banhang.View.RecyclerViewCategory.CategoryAdapter;
import com.example.banhang.View.RecyclerViewCategory.ProductsCategory;
import com.example.banhang.View.RecyclerViewProduct.*;
import com.example.banhang.database.*;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
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
    ImageView imgAnhSanpham;
    Button btnThemSanPham;
    TextView tvTenSanPham, tvGiaTien , tvMoTa,tvNhapAnh,tvLoaiSanPham;
    EditText edtTenSanPham ,edtGiaTien , edtMoTa,edtNhapSrcAnh,edtLoaiSanPham;
    RecyclerView rvListCategories;

     CategoryAdapter categoryAdapter;
     ArrayList<ProductsCategory> lstCategories;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_product, container, false);
        AnhXa(view);
        CreateDatabase databaseHelper = new CreateDatabase(getContext());
        // Tạo và thêm Fragment danh mục sản phẩm vào ProductFragment


        // Khởi tạo RecyclerView và CategoryAdapter
        lstCategories = Utils.LoadDataCategory(getContext());
        categoryAdapter = new CategoryAdapter(lstCategories, databaseHelper);
        rvListCategories.setAdapter(categoryAdapter);
        rvListCategories.setLayoutManager(new LinearLayoutManager(getContext()));

        btnThemSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srcAnh = edtNhapSrcAnh.getText().toString();
                String tenSanPhamr = edtTenSanPham.getText().toString();
                String giaTien = edtGiaTien.getText().toString();
                String moTa = edtMoTa.getText().toString();
                String loaiSanPham = edtLoaiSanPham.getText().toString();
                //kiểm tra có null không
                if(srcAnh.equals("")){
                    ThongBao(getContext(),"Src Ảnh Đang Trống");
                } else if (tenSanPhamr.equals("")) {
                    ThongBao(getContext(),"Tên sản phẩm Đang Trống");
                }else if(giaTien.equals("")){
                    ThongBao(getContext(),"Gia tiền đang trống");
                }
                else if(moTa.equals("")){
                    ThongBao(getContext(),"Mô tả đang trống");
                }
                else{

                    //Khởi tạo đối tưởng Products
                        Products newsProducts = new Products(getContext(),tenSanPhamr,giaTien,moTa,srcAnh,loaiSanPham);

                    // Kiểm tra loại sản phẩm
                    if (loaiSanPham.equals("") || !newsProducts.isCategoryValid(loaiSanPham)) {
                        ThongBao(getContext(), "Loại sản phẩm không hợp lệ!");
                    } else {
                        newsProducts.ThemSanPham(newsProducts, getContext());
                        edtNhapSrcAnh.setText("");
                        edtGiaTien.setText("");
                        edtMoTa.setText("");
                        edtLoaiSanPham.setText("");
                        edtTenSanPham.setText("");
                    }


                }
            }
        });
        return view;
    }
    void AnhXa(View view){
        imgAnhSanpham = view.findViewById(R.id.imgAnhSanPham);
        btnThemSanPham = view.findViewById(R.id.btnThemSanPham);
        tvTenSanPham = view.findViewById(R.id.tvTenSanPham);
        tvGiaTien = view.findViewById(R.id.tvGiaTien);
        tvMoTa = view.findViewById(R.id.tvMoTa);
        edtTenSanPham = view.findViewById(R.id.edtTenSanPham);
        edtGiaTien = view.findViewById(R.id.edtGiaTien);
        edtMoTa = view.findViewById(R.id.edtMoTa);
        tvNhapAnh = view.findViewById(R.id.tvNhapAnh);
        edtNhapSrcAnh = view.findViewById(R.id.edtNhapSrcAnh);
        tvLoaiSanPham = view.findViewById(R.id.tvLoaiSanPham);
        edtLoaiSanPham = view.findViewById(R.id.edtLoaiSanPham);
        rvListCategories = view.findViewById(R.id.rvListCategories);

    }
    void ThongBao(Context context, String noidung){
        Toast.makeText(context,noidung,Toast.LENGTH_SHORT).show();
    }




}

