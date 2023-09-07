package com.moutamid.halalfoodadmin.Activities.Products;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.moutamid.halalfoodadmin.Adapter.AllProductAdapter;
import com.moutamid.halalfoodadmin.Model.ProductModel;
import com.moutamid.halalfoodadmin.R;
import com.moutamid.halalfoodadmin.helper.Config;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllProductsActivity extends AppCompatActivity {

    RecyclerView content_rcv;

    public List<ProductModel> productModelList = new ArrayList<>();
    AllProductAdapter model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);
        content_rcv = findViewById(R.id.content_rcv);
        content_rcv.setLayoutManager(new GridLayoutManager(this, 1));
        model = new AllProductAdapter(this, productModelList);
        content_rcv.setAdapter(model);
        if (Config.isNetworkAvailable(AllProductsActivity.this)) {
            Config.showProgressDialog(AllProductsActivity.this);
            getProducts();
        } else {
            Toast.makeText(AllProductsActivity.this, "No network connection available.", Toast.LENGTH_SHORT).show();
        }

    }

    public void add_details(View view) {
        Intent intent = new Intent(this, AddProductsActivity.class);
        intent.putExtra("item_name", "");
        intent.putExtra("item_barcode", "");
        intent.putExtra("item_category", "");
        intent.putExtra("item_type", "");
        intent.putExtra("key", "");
        startActivity(intent);
    }

    private void getProducts() {
        Config.databaseReference().child("Product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productModelList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ProductModel herbsModel = ds.getValue(ProductModel.class);
                    productModelList.add(herbsModel);
                }
                model.notifyDataSetChanged();
                Config.dismissProgressDialog();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Config.dismissProgressDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Config.isNetworkAvailable(AllProductsActivity.this)) {
            getProducts();
        } else {
            Toast.makeText(AllProductsActivity.this, "No network connection available.", Toast.LENGTH_SHORT).show();
        }
    }
}