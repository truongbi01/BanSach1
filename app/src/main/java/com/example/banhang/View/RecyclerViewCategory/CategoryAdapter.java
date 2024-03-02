package com.example.banhang.View.RecyclerViewCategory;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.banhang.R;
import com.example.banhang.database.CreateDatabase;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.UserViewHolder> {
    Context context;
    ArrayList<ProductsCategory> lstCategory;
    private OnItemClickListener mListener;
    private CreateDatabase databaseHelper;
    public CategoryAdapter(ArrayList<ProductsCategory> lstCategory,CreateDatabase  database) {
        this.lstCategory = lstCategory;
        this.databaseHelper = database;
    }
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //Nạp layout cho view biểu diễn phần từ User
        View userView = inflater.inflate(R.layout.layout_item_category,parent,false);
        //
        UserViewHolder viewHolder = new UserViewHolder(userView);
        return viewHolder;
    }
    Context context1;
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        //Lấy từng item của dữ liệu
        ProductsCategory item = lstCategory.get(position);
        //gán vào item của view
        holder.tvid.setText(item.getID());
        holder.tvName.setText(item.getName());
        String name = holder.tvName.getText().toString();
        holder.btnDeleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra xem mListener có được thiết lập không
                if (mListener != null) {
                    // Chuyển sự kiện xóa đến interface
                    mListener.onDeleteClick(holder.getAdapterPosition());

                    // Gọi phương thức để xóa từ cơ sở dữ liệu
                    XoaDuLieu(item.getID());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return lstCategory.size();
    }
    static class UserViewHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvid;
        Button btnDeleted;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvid = itemView.findViewById(R.id.tvid);
            tvName = itemView.findViewById(R.id.tvName);
            btnDeleted = itemView.findViewById(R.id.btnDeleted);
        }

    }

    public void removeCategory(int position) {
        // Lấy thể loại cần xóa từ danh sách
        ProductsCategory removedCategory = lstCategory.get(position);

        // Thực hiện xóa khỏi danh sách
        lstCategory.remove(position);

        // Thông báo sự kiện đã thay đổi dữ liệu để cập nhật RecyclerView
        notifyItemRemoved(position);

        // Gọi phương thức để xóa từ cơ sở dữ liệu
        XoaDuLieu(removedCategory.getID());
    }

    private void XoaDuLieu(String id) {
        // Tạo và mở kết nối đến cơ sở dữ liệu
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Xác định điều kiện xóa
        String selection = CreateDatabase.CL_THE_LOAI_SAN_PHAM_ID + " = ?";
        String[] selectionArgs = {id};

        // Thực hiện xóa
        db.delete(CreateDatabase.TB_LOAI_SAN_PHAM, selection, selectionArgs);

        // Đóng kết nối đến cơ sở dữ liệu
        db.close();
    }

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public void setData(ArrayList<ProductsCategory> newData) {
        lstCategory = newData;
        notifyDataSetChanged(); // Cập nhật RecyclerView
    }
}

