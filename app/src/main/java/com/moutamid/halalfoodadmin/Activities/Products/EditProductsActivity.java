package com.moutamid.halalfoodadmin.Activities.Products;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.moutamid.halalfoodadmin.Model.ProductModel;
import com.moutamid.halalfoodadmin.R;
import com.moutamid.halalfoodadmin.helper.Config;


public class EditProductsActivity extends AppCompatActivity {

    EditText item_name;
    RadioGroup item_category, item_types;
    String item_category_str = "Halal", item_types_str = "Risk Free", item_name_str, item_barcode_str;
    TextView add_btn, item_barcode;
    RadioButton halal, haram, doubtful, risky, limited_risky, riskfree;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_products);

        initViews();
        String itemName = getIntent().getStringExtra("item_name");
        String itemBarcode = getIntent().getStringExtra("item_barcode");
        String itemCategory = getIntent().getStringExtra("item_category");
        String itemType = getIntent().getStringExtra("item_type");
        key = getIntent().getStringExtra("item_key");
        item_name.setText(itemName);
        item_barcode.setText(itemBarcode);
        if (itemCategory.equals("Halal")) {
            halal.setChecked(true);

        } else if (itemCategory.equals("Haram")) {
            haram.setChecked(true);

        } else {
            doubtful.setChecked(true);
        }
        if (itemType.equals("Risky")) {
            risky.setChecked(true);

        } else if (itemType.equals("Limited Risk")) {
            limited_risky.setChecked(true);

        } else {
            riskfree.setChecked(true);
        }
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (initValues()) {
                    Config.showProgressDialog(EditProductsActivity.this);
                    ProductModel productModel = new ProductModel();
                    productModel.setItem_barcode(item_barcode_str);
                    productModel.setItem_name(item_name_str);
                    productModel.setItem_category(item_category_str);
                    productModel.setItem_type(item_types_str);
                    productModel.setKey(key);
                    Config.databaseReference().child("Product").child(key).setValue(productModel).addOnSuccessListener(aVoid -> {
                        Config.dismissProgressDialog();
                        Toast.makeText(EditProductsActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        finish();
                    }).addOnFailureListener(e -> {
                        Config.dismissProgressDialog();
                        Toast.makeText(EditProductsActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    private void initViews() {
        item_name = findViewById(R.id.item_name);
        item_types = findViewById(R.id.item_type);
        item_category = findViewById(R.id.item_category);
        item_barcode = findViewById(R.id.item_barcode);
        doubtful = findViewById(R.id.doubtful);
        halal = findViewById(R.id.halal);
        haram = findViewById(R.id.haram);
        risky = findViewById(R.id.risky);
        riskfree = findViewById(R.id.riskfree);
        limited_risky = findViewById(R.id.limited_risky);
        add_btn = findViewById(R.id.add_btn);
        item_types.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                item_types_str = radioButton.getText().toString();

            }
        });
        item_category.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                item_category_str = radioButton.getText().toString();
            }
        });
    }


    public boolean initValues() {
        if (item_name.getText().toString().isEmpty()) {
            item_name.setError("Enter here");
        } else {
            item_name_str = item_name.getText().toString();
            return true;


        }
        return false;
    }

    public void backpress(View view) {
        onBackPressed();
    }
}