package com.example.banhang.View.fragment;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.example.banhang.View.RecycleViewGioHang.*;
import com.example.banhang.R;
import com.example.banhang.View.RecyclerViewCategory.CategoryAdapter;
import com.example.banhang.View.RecyclerViewCategory.ProductsCategory;
import com.example.banhang.View.*;
import com.example.banhang.database.CreateDatabase;
import com.example.banhang.View.RecyclerViewProduct.*;
import java.util.ArrayList;

public class HomeFragment extends Fragment implements ProductAdapter.UserCallBack {
    CreateDatabase databaseHelper;
    RecyclerView rvListProduct;
    ArrayList<Products> listProducts;
    ProductAdapter productAdapter;
    ProductAdapterAdmin productAdapterAdmin;
    ImageView imgCart;
    TextView tvCartItemCount;
    SwipeRefreshLayout swipeRefreshLayout;
    Spinner spinnerCategory;
    ArrayList<ProductsCategory> categoryList;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ViewFlipper viewFlipper;
    ArrayList<Products> lstProductCart = new ArrayList<>();
    private int currentImage = 0;
    private static final int FLIP_INTERVAL = 3000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        AnhXa(view);
        tvCartItemCount = view.findViewById(R.id.tvCartItemCount);
        updateCartItemCount();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadFragment();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        flipHandler.sendEmptyMessageDelayed(0, FLIP_INTERVAL);
        databaseHelper = new CreateDatabase(getActivity());

        SharedPreferences cartPreferences = getContext().getSharedPreferences("DuLieu", MODE_PRIVATE);
        String cartItemCount = cartPreferences.getString("soLuong", null);
        lstProductCart = Utils.LoadDaTaProductsCart(getContext());

        int totalQuantity = 0;
        for (Products product : lstProductCart) {
            if (product != null) {
                totalQuantity += 1;
            }
        }

        if (totalQuantity > 0) {
            tvCartItemCount.setVisibility(View.VISIBLE);
            tvCartItemCount.setText(String.valueOf(totalQuantity));
        } else {
            tvCartItemCount.setVisibility(View.GONE);
        }

        String thongtinluu = "tk_mk login";
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(thongtinluu, MODE_PRIVATE);
        String tenDangNhap = sharedPreferences.getString("Username", "");

        String role = databaseHelper.GetCLVaitro(tenDangNhap);
        if (role.equals("admin")) {
            Toast.makeText(getContext(), "Product Admin", Toast.LENGTH_SHORT).show();
            LoadDataProducts(getActivity());
            productAdapterAdmin = new ProductAdapterAdmin(listProducts, databaseHelper);
            rvListProduct.setAdapter(productAdapterAdmin);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            rvListProduct.setLayoutManager(gridLayoutManager);
        } else {
            LoadDataProducts(getActivity());
            productAdapter = new ProductAdapter(listProducts, databaseHelper, this);
            rvListProduct.setAdapter(productAdapter);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            rvListProduct.setLayoutManager(gridLayoutManager);
        }

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CartActivity.class);
                startActivity(i);
            }
        });

        loadCategories();
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProductsCategory selectedCategory = (ProductsCategory) parent.getSelectedItem();
                loadProductsByCategory(selectedCategory.getID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        return view;
    }
    private void updateCartItemCount() {
        // Giả sử bạn có một phương thức để lấy số lượng sản phẩm trong giỏ hàng
        int cartItemCount = getCartItemCount();
        tvCartItemCount.setText(String.valueOf(cartItemCount));
    }

    private int getCartItemCount() {
        // Thay thế bằng logic thực tế để lấy số lượng sản phẩm trong giỏ hàng
        return 5; // Ví dụ: trả về 5 sản phẩm
    }

    public void reloadFragment() {
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.mainFragment);
        if (currentFragment instanceof HomeFragment) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.detach(currentFragment);
            fragmentTransaction.attach(currentFragment);
            fragmentTransaction.commit();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    void AnhXa(View view) {
        rvListProduct = view.findViewById(R.id.rvListProduct);
        viewFlipper = view.findViewById(R.id.viewlipper);
        imgCart = view.findViewById(R.id.imgCart);
        tvCartItemCount = view.findViewById(R.id.tvCartItemCount);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
    }

    void LoadDataProducts(Context context) {
        listProducts = Utils.LoadDaTaProducts(context);
    }

    void loadCategories() {
        categoryList = Utils.loadCategoriesFromDatabase(getContext());
        // Thêm mục "Tất cả" vào danh sách thể loại
        categoryList.add(0, new ProductsCategory("all", "Tất cả"));

        ArrayAdapter<ProductsCategory> adapter = new ArrayAdapter<ProductsCategory>(getContext(), android.R.layout.simple_spinner_item, categoryList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(categoryList.get(position).getName());
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(categoryList.get(position).getName());
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    void loadProductsByCategory(String categoryId) {
        if (categoryId.equals("all")) {
            // Hiển thị tất cả sản phẩm
            listProducts = Utils.LoadDaTaProducts(getContext());
        } else {
            // Hiển thị sản phẩm theo thể loại
            listProducts = Utils.LoadProductsByCategory(getContext(), categoryId);
        }

        if (productAdapter != null) {
            productAdapter.setData(listProducts);
        } else if (productAdapterAdmin != null) {
            productAdapterAdmin.setData(listProducts);
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler flipHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (currentImage < viewFlipper.getChildCount() - 1) {
                    currentImage++;
                } else {
                    currentImage = 0;
                }
                viewFlipper.setDisplayedChild(currentImage);
                sendEmptyMessageDelayed(0, FLIP_INTERVAL);
            }
        }
    };

    @Override
    public void onItemClick(String tenSanPham, String GiaTien, String moTa, String srcAnh) {
        Intent i = new Intent(getActivity(), DetailActivity.class);
        i.putExtra("tenSanPham", tenSanPham);
        i.putExtra("giaTien", GiaTien);
        i.putExtra("mota", moTa);
        i.putExtra("srcAnh", srcAnh);
        startActivity(i);
    }
}