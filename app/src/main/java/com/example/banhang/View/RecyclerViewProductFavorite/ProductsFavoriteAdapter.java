package com.example.banhang.View.RecyclerViewProductFavorite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banhang.R;
import com.example.banhang.View.RecyclerViewProduct.ProductAdapter;
import com.example.banhang.View.RecyclerViewProduct.ProductAdapterAdmin;
import com.example.banhang.View.RecyclerViewProduct.Products;
import com.example.banhang.View.RecyclerViewProduct.Utils;
import com.example.banhang.database.CreateDatabase;
import com.example.banhang.View.*;
import java.util.ArrayList;

public class ProductsFavoriteAdapter extends RecyclerView.Adapter<ProductsFavoriteAdapter.ProductsFavoriteViewHolder> {
    ArrayList<Products> listProducts;
    CreateDatabase databaseHelper;
    Context context;
    private ProductsFavoriteAdapter favoriteAdapter;
    public ProductsFavoriteAdapter(ArrayList<Products> listProducts, CreateDatabase createDatabase){
        this.listProducts = listProducts;
        this.databaseHelper =  createDatabase;
    }
    @NonNull
    @Override
    public ProductsFavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        // Tạo View
        View view = layoutInflater.inflate(R.layout.layout_item_product_favorite,parent,false);
        ProductsFavoriteAdapter.ProductsFavoriteViewHolder viewHolder = new ProductsFavoriteAdapter.ProductsFavoriteViewHolder(view);
        return viewHolder ;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsFavoriteViewHolder holder, int position) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        //Lay tung products cua dữ liêu
        Products products = listProducts.get(position);

        //Gán dữ liệu cho view
        holder.tvName.setText(products.getName());
        holder.tvGia.setText(products.getPrice());
        holder.imgAnhSanPham.setImageBitmap(Utils.convertToBitmapFromAssets(context,products.getImage()));
        String idSanPham = createDatabase.GetIdSanPham(products.getName());
        holder.cbYeuThich.setChecked(true);
        holder.cbYeuThich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    ProductAdapter productAdapter = new ProductAdapter();
                    productAdapter.XoaSanPhamYeuThich(products.getName(),idSanPham,context);

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    static class ProductsFavoriteViewHolder extends RecyclerView.ViewHolder{
        ImageView imgAnhSanPham;
        TextView tvName,tvGia;
        CheckBox cbYeuThich;
        public ProductsFavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTenProducts_favorite);
            tvGia = itemView.findViewById(R.id.tvGiaSanPham_favorite);
            cbYeuThich = itemView.findViewById(R.id.cbYeuThich_favorite);
            imgAnhSanPham = itemView.findViewById(R.id.imgAnhSanPham_favorite);
        }
    }


}
