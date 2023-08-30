package com.moutamid.halalfoodadmin.helper;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.icu.lang.UCharacter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.moutamid.halalfoodadmin.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Config {
    public static String preference_name = "HalalFoodFinder";


    static SharedPreferences sharedpreferences;
    static SharedPreferences.Editor editor;

    private static Dialog progressDialog;



    public static DatabaseReference databaseReference() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("HalalFoodFinder");
        db.keepSynced(true);
        return db;
    }

    public static void storeValue(String key, String value, Context context) {
        sharedpreferences = context.getSharedPreferences(preference_name, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getValue(String key) {
        return sharedpreferences.getString(key, " ");
    }

    public static String getValue(String key, Context context) {
        sharedpreferences = context.getSharedPreferences(preference_name, Context.MODE_PRIVATE);
        return sharedpreferences.getString(key, " ");
    }

    public static void showProgressDialog(Context context) {
        progressDialog = new Dialog(context);
        progressDialog.setContentView(R.layout.loading);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(UCharacter.JoiningType.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
