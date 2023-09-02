package com.moutamid.halalfoodadmin.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.moutamid.halalfoodadmin.Activities.Products.EditProductsActivity;
import com.moutamid.halalfoodadmin.Model.ProductModel;
import com.moutamid.halalfoodadmin.R;
import com.moutamid.halalfoodadmin.helper.Config;

import java.util.List;

public class UserProductAdapter extends RecyclerView.Adapter<UserProductAdapter.GalleryPhotosViewHolder> {


    Context ctx;
    List<ProductModel> productModels;

    public UserProductAdapter(Context ctx, List<ProductModel> productModels) {
        this.ctx = ctx;
        this.productModels = productModels;
    }

    @NonNull
    @Override
    public GalleryPhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.products, parent, false);
        return new GalleryPhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryPhotosViewHolder holder, final int position) {
        final String item_name = productModels.get(position).getItem_name();
        final String item_category = productModels.get(position).getItem_category();
        final String item_type = productModels.get(position).getItem_type();
        final String item_key = productModels.get(position).getKey();
        final String item_barcode = productModels.get(position).getItem_barcode();
        final String name = productModels.get(position).getName();
        final String age = productModels.get(position).getAge();
        final String cnic = productModels.get(position).getCnic();
        holder.item_category.setText(item_category + " item");
        holder.item_type.setText(item_type);
        holder.item_name.setText(item_name);
        holder.name.setText("Name : "+name);
        holder.age.setText("Age : "+age);
        holder.gender.setText("CNIC : "+cnic);
        holder.item_barcode.setText("Item Barcode : "+item_barcode);
//        holder.itemView.setOnClickListener(view -> {
//            Intent intent = new Intent(ctx, AddProductsActivity.class);
//            intent.putExtra("barcode", item_barcode);
//            intent.putExtra("category", item_category);
//            intent.putExtra("type", item_type);
//            intent.putExtra("name", item_name);
//            intent.putExtra("key", productModels.get(position).getKey());
//            ctx.startActivity(intent);
//        });

        holder.remove.setOnClickListener(view -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
            dialog.setTitle("Delete Product");
            dialog.setMessage("Are you sure to delete this product");
            dialog.setPositiveButton("Yes", (dialogInterface, i) -> {
                dialogInterface.dismiss();
                Config.showProgressDialog(ctx);
                Config.databaseReference().child("UserProduct").child(productModels.get(position).getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Config.dismissProgressDialog();
//                        if (task.isSuccessful()){
//                            productModels.remove(position);
//                            notifyDataSetChanged();
//                        }
                    }
                }).addOnFailureListener(e -> {
                    Config.dismissProgressDialog();
                    Toast.makeText(ctx, "Something went wrong.", Toast.LENGTH_SHORT).show();
                });
            }).setNegativeButton("No", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
            dialog.show();
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, EditProductsActivity.class);
                intent.putExtra("item_name", item_name);
                intent.putExtra("item_barcode", item_barcode);
                intent.putExtra("item_category", item_category);
                intent.putExtra("item_type", item_type);
                intent.putExtra("item_key", item_key);
                ctx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public class GalleryPhotosViewHolder extends RecyclerView.ViewHolder {

        TextView item_name, item_type, item_category, name, age, gender, item_barcode;
        ImageView remove, edit;

        public GalleryPhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name);
            item_type = itemView.findViewById(R.id.item_type);
            item_category = itemView.findViewById(R.id.item_category);
            remove = itemView.findViewById(R.id.remove);
            edit = itemView.findViewById(R.id.edit);
            name = itemView.findViewById(R.id.name);
            age = itemView.findViewById(R.id.age);
            gender = itemView.findViewById(R.id.cnic);
            item_barcode = itemView.findViewById(R.id.item_barcode);

        }
    }
}
