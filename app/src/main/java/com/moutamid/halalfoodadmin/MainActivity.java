package com.moutamid.halalfoodadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.moutamid.halalfoodadmin.Activities.Products.AllProductsActivity;
import com.moutamid.halalfoodadmin.Activities.Resturants.AllResturantsActivity;

public class MainActivity extends AppCompatActivity {
    Button add_products, add_restuarant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add_restuarant = findViewById(R.id.add_restuarant);
        add_products = findViewById(R.id.add_products);

        add_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AllProductsActivity.class));
            }
        });
        add_restuarant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AllResturantsActivity.class));
            }
        });
    }
}