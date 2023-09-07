package com.moutamid.halalfoodadmin.Activities.Products;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.moutamid.halalfoodadmin.Model.ProductModel;
import com.moutamid.halalfoodadmin.R;
import com.moutamid.halalfoodadmin.helper.Config;

import java.io.IOException;


public class AddProductsActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    String intentData = "";
    EditText item_name;
    RadioGroup item_category, item_types;
    String item_category_str = "Halal", item_types_str = "Limited Risk", item_name_str, item_barcode_str;
    TextView add_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        initViews();

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (initValues()) {
                    Config.showProgressDialog(AddProductsActivity.this);
                    String key = Config.databaseReference().child("Product").push().getKey();
                    ProductModel productModel = new ProductModel();
                    productModel.setItem_barcode(item_barcode_str);
                    productModel.setItem_name(item_name_str);
                    productModel.setItem_category(item_category_str);
                    productModel.setItem_type(item_types_str);
                    productModel.setKey(key);
                    Config.databaseReference().child("Product").child(key).setValue(productModel)
                            .addOnSuccessListener(aVoid -> {
                                Config.dismissProgressDialog();
                                Toast.makeText(AddProductsActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Config.dismissProgressDialog();
                                Toast.makeText(AddProductsActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.item_barcode);
        surfaceView = findViewById(R.id.surfaceView);
        item_name = findViewById(R.id.item_name);
        item_types = findViewById(R.id.item_type);
        item_category = findViewById(R.id.item_category);
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

    private void initialiseDetectorsAndSources() {

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(AddProductsActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(AddProductsActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
//                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    txtBarcodeValue.post(new Runnable() {
                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                txtBarcodeValue.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;
                                txtBarcodeValue.setText(intentData);
                                surfaceView.setVisibility(View.GONE);
                            } else {
                                intentData = barcodes.valueAt(0).displayValue;
                                txtBarcodeValue.setText(intentData);
                                surfaceView.setVisibility(View.GONE);

                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    public boolean initValues() {
        if (item_name.getText().toString().isEmpty()) {
            item_name.setError("Enter here");
        } else {
            item_barcode_str = txtBarcodeValue.getText().toString();
            item_name_str = item_name.getText().toString();
            return true;


        }
        return false;
    }

    public void backpress(View view) {
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                startActivity(new Intent(AddProductsActivity.this, AddProductsActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}