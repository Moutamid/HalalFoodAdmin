package com.moutamid.halalfoodadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.moutamid.halalfoodadmin.Activities.AllProductsActivity;

public class MainActivity extends AppCompatActivity {
    Button add_products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add_products = findViewById(R.id.add_products);
        add_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AllProductsActivity.class));
            }
        });
    }
}