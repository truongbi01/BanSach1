package com.example.banhang.View.RecyclerViewProduct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banhang.R;
import com.example.banhang.View.RecyclerViewCategory.CategoryAdapter;
import com.example.banhang.database.CreateDatabase;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    ArrayList<Products> listProducts ;
    CategoryAdapter.OnItemClickListener mListener;
    CreateDatabase databaseHelper;
    Context context;
    public ProductAdapter(ArrayList<Products> listProducts , CreateDatabase  database){
        this.listProducts = listProducts;
        this.databaseHelper = database;
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //Nạp layout cho view biểu diễn phần từ User
        View userView = inflater.inflate(R.layout.layout_item_product,parent,false);
        //
        ProductAdapter.ProductViewHolder viewHolder = new   ProductAdapter.ProductViewHolder(userView);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        //Lấy từng item của dữ liệu
        Products item = listProducts.get(position);

        //gán vào item của view
        holder.tvName.setText(item.getName());
        holder.tvGia.setText(item.getPrice());
        holder.imgAnhSanPham.setImageBitmap(Utils.convertToBitmapFromAssets(context,item.getImage()));

    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder{
        ImageView imgAnhSanPham;
        TextView tvName,tvGia;
        CheckBox cbYeuThich;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTenProducts);
            tvGia = itemView.findViewById(R.id.tvGiaSanPham);
            cbYeuThich = itemView.findViewById(R.id.cbYeuThich);
            imgAnhSanPham = itemView.findViewById(R.id.imgAnhSanPham);
        }

    }
}
