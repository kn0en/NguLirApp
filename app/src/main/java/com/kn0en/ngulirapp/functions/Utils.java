package com.kn0en.ngulirapp.functions;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kn0en.ngulirapp.MainActivity;
import com.kn0en.ngulirapp.R;
import com.kn0en.ngulirapp.models.Restaurant;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class Utils {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final List<Restaurant> DataCache = new ArrayList<>();

    public static String searchString = "";

    public static void show(Context c, String message) {
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean validate(EditText... editTexts) {
        EditText nameTxt = editTexts[0];
        EditText descriptionTxt = editTexts[1];
        EditText phoneTxt = editTexts[2];
        EditText addressTxt = editTexts[3];

        if (nameTxt.getText() == null || nameTxt.getText().toString().isEmpty()) {
            nameTxt.setError("Name is Required Please!");
            return false;
        }
        if (descriptionTxt.getText() == null || descriptionTxt.getText().toString().isEmpty()) {
            descriptionTxt.setError("Description is Required Please!");
            return false;
        }
        if (phoneTxt.getText() == null || phoneTxt.getText().toString().isEmpty()) {
            phoneTxt.setError("Phone is Required Please!");
            return false;
        }
        if (addressTxt.getText() == null || addressTxt.getText().toString().isEmpty()) {
            addressTxt.setError("Address is Required Please!");
            return false;
        }
        return true;

    }

    @SuppressWarnings("rawtypes")
    public static void openActivity(Context c, Class clazz) {
        Intent intent = new Intent(c, clazz);
        c.startActivity(intent);
    }

    /**
     * This method will allow us show an Info dialog anywhere in our app.
     */
    public static void showInfoDialog(final AppCompatActivity activity, String title,
                                      String message) {
        new LovelyStandardDialog(activity, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes(R.color.colorPrimary)
                .setButtonsColorRes(R.color.colorPrimaryDark)
                .setIcon(R.drawable.m_info)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Relax", v -> {
                })
                .setNeutralButton("Go Home", v -> openActivity(activity, MainActivity.class))
                .show();
    }

    /**
     * This method will allow us send a serialized scientist objec  to a specified
     * activity
     */
    @SuppressWarnings("rawtypes")
    public static void sendRestaurantToActivity(Context c, Restaurant restaurant,
                                                Class clazz) {
        Intent i = new Intent(c, clazz);
        i.putExtra("RESTO_KEY", restaurant);
        c.startActivity(i);
    }

    public static Restaurant recieveRestaurant(Intent intent, Context c) {
        try {
            return (Restaurant) intent.getSerializableExtra("RESTO_KEY");
        } catch (Exception e) {
            e.printStackTrace();
            show(c, "RECEIVING-RESTAURANT ERROR: " + e.getMessage());
        }
        return null;
    }

    public static void showProgressBar(ProgressBar pb) {
        pb.setVisibility(View.VISIBLE);
    }

    public static void hideProgressBar(ProgressBar pb) {
        pb.setVisibility(View.GONE);
    }

    public static DatabaseReference getDatabaseRefence() {
        return FirebaseDatabase.getInstance().getReference();
    }

}