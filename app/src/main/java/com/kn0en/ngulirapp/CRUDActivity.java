package com.kn0en.ngulirapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.kn0en.ngulirapp.functions.FirebaseCRUDHelper;
import com.kn0en.ngulirapp.functions.Utils;
import com.kn0en.ngulirapp.models.Restaurant;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class CRUDActivity extends AppCompatActivity {

    private final Context c = CRUDActivity.this;
    private final FirebaseCRUDHelper crudHelper = new FirebaseCRUDHelper();
    private final DatabaseReference db = Utils.getDatabaseRefence();
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    // --Commented out by Inspection (10/16/20 6:34 PM):private ImageView imageRestoUri;
    private EditText nameTxt, phoneTxt, addressTxt, descriptionTxt;
    private MaterialButton btnSendData, btnShowAll, btnUpdateData, btnDeleteData;
    private TextView headerTxt, txt_header;
    private ProgressBar mProgressBar;
    private Restaurant receiveRestaurant;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);

        this.initializeWidgets();

        if (receiveRestaurant == null) {
            headerTxt.setText("Add New Restaurant");
            btnUpdateData.setVisibility(View.GONE);
            btnSendData.setOnClickListener(v -> insertData());

            btnDeleteData.setVisibility(View.GONE);
            btnShowAll.setOnClickListener(v -> {
                Intent i = new Intent(CRUDActivity.this, RestoActivity.class);
                startActivity(i);
                finish();
            });
        }
    }

    private void initializeWidgets() {
        mProgressBar = findViewById(R.id.mProgressBarSave);

        headerTxt = findViewById(R.id.header_text);
        txt_header = findViewById(R.id.txt_header);
        nameTxt = findViewById(R.id.edt_tx_nameResto);
        phoneTxt = findViewById(R.id.edt_tx_phoneResto);
        addressTxt = findViewById(R.id.edt_tx_addressResto);
        descriptionTxt = findViewById(R.id.edt_tx_descResto);

        btnSendData = findViewById(R.id.btnSendData);
        btnUpdateData = findViewById(R.id.btnUpdateData);
        btnDeleteData = findViewById(R.id.btnDeleteData);
        btnShowAll = findViewById(R.id.btnShowAll);

    }

    private void insertData() {
        String name, description, phone, address;
        if (Utils.validate(nameTxt, descriptionTxt, phoneTxt, addressTxt)) {
            name = nameTxt.getText().toString();
            phone = phoneTxt.getText().toString();
            address = addressTxt.getText().toString();
            description = descriptionTxt.getText().toString();

            Restaurant newRestaurant = new Restaurant(name, phone, address, description);
            crudHelper.insert(this, db, user, mProgressBar, newRestaurant);

        }
    }

    private void updateData() {
        String name, description, phone, address;
        if (Utils.validate(nameTxt, descriptionTxt, phoneTxt, addressTxt)) {
            name = nameTxt.getText().toString();
            phone = phoneTxt.getText().toString();
            address = addressTxt.getText().toString();
            description = descriptionTxt.getText().toString();

            Restaurant newRestaurant = new Restaurant(name, phone, address, description);
            crudHelper.update(this, db, user, mProgressBar, receiveRestaurant, newRestaurant);

        }
    }

    private void deleteData() {
        crudHelper.delete(this, db, user, mProgressBar, receiveRestaurant);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        Restaurant o = Utils.recieveRestaurant(getIntent(), c);
        if (o != null) {
            receiveRestaurant = o;
            nameTxt.setText(receiveRestaurant.getNameResto());
            phoneTxt.setText(receiveRestaurant.getPhone());
            addressTxt.setText(receiveRestaurant.getAddress());
            descriptionTxt.setText(receiveRestaurant.getDescription());

            headerTxt.setText("Edit Existing Restaurant");
            txt_header.setText("Fill any information below to edit exiting restaurant");

            btnSendData.setVisibility(View.GONE);
            btnShowAll.setVisibility(View.GONE);
            btnDeleteData.setVisibility(View.VISIBLE);
            btnUpdateData.setVisibility(View.VISIBLE);
            btnUpdateData.setOnClickListener(v -> updateData());
            btnDeleteData.setOnClickListener(v -> deleteData());
        }
    }
}
