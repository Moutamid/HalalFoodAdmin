package com.moutamid.halalfoodadmin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.halalfoodadmin.Adapter.Like_Dislike_ProductAdapter;
import com.moutamid.halalfoodadmin.Model.ProductModel;
import com.moutamid.halalfoodadmin.R;
import com.moutamid.halalfoodadmin.helper.Config;

import java.util.ArrayList;
import java.util.List;


public class DislikeFragment extends Fragment {

    RecyclerView content_rcv;

    public List<ProductModel> productModelList = new ArrayList<>();
    Like_Dislike_ProductAdapter model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getContext() fragment
        View view = inflater.inflate(R.layout.fragment_like, container, false);

        content_rcv = view.findViewById(R.id.content_rcv);
        content_rcv.setLayoutManager(new GridLayoutManager(getContext(), 1));
        model = new Like_Dislike_ProductAdapter(getContext(), productModelList, "Dislike");
        content_rcv.setAdapter(model);
        if (Config.isNetworkAvailable(getContext())) {
            getProducts();
        } else {
            Toast.makeText(getContext(), "No network connection available.", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
    private void getProducts() {
        Config.databaseReference().child("Feedback").child("Dislike").addValueEventListener(new ValueEventListener() {
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
    public void onResume() {
        super.onResume();
        getProducts();
    }
}