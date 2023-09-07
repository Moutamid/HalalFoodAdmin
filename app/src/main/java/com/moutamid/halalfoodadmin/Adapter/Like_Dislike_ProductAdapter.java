package com.moutamid.halalfoodadmin.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

public class Like_Dislike_ProductAdapter extends RecyclerView.Adapter<Like_Dislike_ProductAdapter.GalleryPhotosViewHolder> {


    Context ctx;
    List<ProductModel> productModels;
    String type;

    public Like_Dislike_ProductAdapter(Context ctx, List<ProductModel> productModels, String type) {
        this.ctx = ctx;
        this.productModels = productModels;
        this.type = type;
    }

    @NonNull
    @Override
    public GalleryPhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.product, parent, false);
        return new GalleryPhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryPhotosViewHolder holder, final int position) {
        final String item_name = productModels.get(position).getItem_name();
        final String item_category = productModels.get(position).getItem_category();
        final String item_type = productModels.get(position).getItem_type();
        final String item_key = productModels.get(position).getKey();
        final String item_barcode = productModels.get(position).getItem_barcode();
        holder.item_category.setText(item_category + " item");
        holder.item_type.setText(item_type);
        holder.item_name.setText(item_name);

        holder.remove.setOnClickListener(view -> {
            Log.d("data", type+"   "+ productModels.get(position).getKey());

            AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
            dialog.setTitle("Delete Product");
            dialog.setMessage("Are you sure to delete this product");
            dialog.setPositiveButton("Yes", (dialogInterface, i) -> {
                dialogInterface.dismiss();
                Config.showProgressDialog(ctx);
                Config.databaseReference().child("Feedback").child(type).child(productModels.get(position).getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
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
                intent.putExtra("type", type);
                ctx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public class GalleryPhotosViewHolder extends RecyclerView.ViewHolder {

        TextView item_name, item_type, item_category, change_type;
        ImageView remove, edit;

        public GalleryPhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name);
            item_type = itemView.findViewById(R.id.item_type);
            item_category = itemView.findViewById(R.id.item_category);
            remove = itemView.findViewById(R.id.remove);
            edit = itemView.findViewById(R.id.edit);

        }
    }
}
