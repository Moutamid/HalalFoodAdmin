package com.moutamid.halalfoodadmin.Activities.User_product;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.halalfoodadmin.Adapter.AllProductAdapter;
import com.moutamid.halalfoodadmin.Adapter.UserProductAdapter;
import com.moutamid.halalfoodadmin.Model.ProductModel;
import com.moutamid.halalfoodadmin.R;
import com.moutamid.halalfoodadmin.helper.Config;

import java.util.ArrayList;
import java.util.List;

public class UserProductActivity extends AppCompatActivity {

    RecyclerView content_rcv;

    public List<ProductModel> productModelList = new ArrayList<>();
    UserProductAdapter model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product);
        content_rcv = findViewById(R.id.content_rcv);
        content_rcv.setLayoutManager(new GridLayoutManager(this, 1));
        model = new UserProductAdapter(this, productModelList);
        content_rcv.setAdapter(model);
        if (Config.isNetworkAvailable(UserProductActivity.this)) {
            getProducts();
        } else {
            Toast.makeText(UserProductActivity.this, "No network connection available.", Toast.LENGTH_SHORT).show();
        }

    }


    private void getProducts() {
        Config.databaseReference().child("UserProduct").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productModelList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ProductModel herbsModel = ds.getValue(ProductModel.class);
                    productModelList.add(herbsModel);
                }
                model.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Config.dismissProgressDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Config.isNetworkAvailable(UserProductActivity.this)) {
            getProducts();
        } else {
            Toast.makeText(UserProductActivity.this, "No network connection available.", Toast.LENGTH_SHORT).show();
        }
    }

    public void backpress(View view) {
        onBackPressed();
    }
}