package com.moutamid.halalfoodadmin.Activities;



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
    AllProductAdapter herbsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);
        content_rcv = findViewById(R.id.content_rcv);
        content_rcv.setLayoutManager(new GridLayoutManager(this, 2));
        herbsAdapter = new AllProductAdapter(this, productModelList);
        content_rcv.setAdapter(herbsAdapter);
        if (Config.isNetworkAvailable(AllProductsActivity.this)) {
            getProducts();
        } else {
            Toast.makeText(AllProductsActivity.this, "No network connection available.", Toast.LENGTH_SHORT).show();
        }

    }

    public void add_details(View view) {
        Intent intent = new Intent(this, AddProductsActivity.class);
        intent.putExtra("english_name", "");
        intent.putExtra("benefits", "");
        intent.putExtra("urduName", "");
        intent.putExtra("remedies", "");
        intent.putExtra("key", "");
        startActivity(intent);
    }

    private void getProducts() {
        Config.databaseReference().child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productModelList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ProductModel herbsModel = ds.getValue(ProductModel.class);
                    productModelList.add(herbsModel);
                }
                herbsAdapter.notifyDataSetChanged();

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
        if (Config.isNetworkAvailable(AllProductsActivity.this)) {
            getProducts();
        } else {
            Toast.makeText(AllProductsActivity.this, "No network connection available.", Toast.LENGTH_SHORT).show();
        }
    }
}