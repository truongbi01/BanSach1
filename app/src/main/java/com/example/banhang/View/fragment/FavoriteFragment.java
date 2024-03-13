package com.example.banhang.View.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.banhang.R;
import com.example.banhang.View.DetailActivity;
import com.example.banhang.View.RecyclerViewProduct.Products;
import com.example.banhang.View.RecyclerViewProduct.Utils;
import com.example.banhang.View.RecyclerViewProductFavorite.ProductsFavoriteAdapter;
import com.example.banhang.database.CreateDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GalleryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
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
    ArrayList<Products> listProducts;
    RecyclerView recyclerView;
    ProductsFavoriteAdapter productsFavoriteAdapter;
    CreateDatabase databaseHelper;
    Button btnXoaTatCaSanPhamYeuThich;
    SQLiteDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        AnhXa(view);
        LoadDataProducts(getActivity());
        databaseHelper = new CreateDatabase(getContext());
        database  = databaseHelper.getWritableDatabase();
        productsFavoriteAdapter = new ProductsFavoriteAdapter(listProducts,databaseHelper);
        recyclerView.setAdapter(productsFavoriteAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        btnXoaTatCaSanPhamYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    DeletedAllProductsFavorite();
            }
        });


        return  view;
    }
    void AnhXa(View view){
        recyclerView = view.findViewById(R.id.rcvYeuThich);
        btnXoaTatCaSanPhamYeuThich = view.findViewById(R.id.btnXoaTatCaSanPhamyeuThich);
    }
    void LoadDataProducts(Context context){
        listProducts = Utils.LoadProductsFavoriteFromDatabase(context);
    }
    void DeletedAllProductsFavorite(){
        database.execSQL("DELETE FROM favorite_products");
    }
}