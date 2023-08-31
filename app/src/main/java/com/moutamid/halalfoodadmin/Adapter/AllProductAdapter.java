package com.moutamid.halalfoodadmin.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.halalfoodadmin.Activities.AddProductsActivity;
import com.moutamid.halalfoodadmin.Model.ProductModel;
import com.moutamid.halalfoodadmin.R;
import com.moutamid.halalfoodadmin.helper.Config;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class AllProductAdapter extends RecyclerView.Adapter<AllProductAdapter.GalleryPhotosViewHolder> {


    Context ctx;
    List<ProductModel> productModels;
    public AllProductAdapter(Context ctx, List<ProductModel> productModels) {
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
        final String item_barcode = productModels.get(position).getItem_barcode();
        final String item_category = productModels.get(position).getItem_category();
        final String item_type = productModels.get(position).getItem_type();
        holder.item_barcode.setText(item_barcode);
        holder.item_category.setText(item_category);
        holder.item_type.setText(item_type);
        holder.item_name.setText(item_name);
//        holder.itemView.setOnClickListener(view -> {
//            Intent intent = new Intent(ctx, AddProductsActivity.class);
//            intent.putExtra("barcode", item_barcode);
//            intent.putExtra("category", item_category);
//            intent.putExtra("type", item_type);
//            intent.putExtra("name", item_name);
//            intent.putExtra("key", productModels.get(position).getKey());
//            ctx.startActivity(intent);
//        });
        holder.remove_herb.setOnClickListener(view -> {
            AlertDialog.Builder dialog=new AlertDialog.Builder(ctx);
            dialog.setTitle("Delete Product");
            dialog.setMessage("Are you sure to delete this product");
            dialog.setPositiveButton("Yes", (dialogInterface, i) -> {
                dialogInterface.dismiss();
                Config.showProgressDialog(ctx);
                Config.databaseReference().child("Product").child(productModels.get(position).getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
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


    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public class GalleryPhotosViewHolder extends RecyclerView.ViewHolder {

        TextView item_name, item_type, item_category, item_barcode;
        ImageView remove_herb;

        public GalleryPhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name);
            item_type = itemView.findViewById(R.id.item_type);
            item_category = itemView.findViewById(R.id.item_category);
            item_barcode = itemView.findViewById(R.id.item_barcode);
            remove_herb=itemView.findViewById(R.id.remove_herb);

        }
    }
}
