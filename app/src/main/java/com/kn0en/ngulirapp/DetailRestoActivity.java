package com.kn0en.ngulirapp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kn0en.ngulirapp.functions.Utils;
import com.kn0en.ngulirapp.models.Restaurant;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

@SuppressWarnings("deprecation")
public class DetailRestoActivity extends AppCompatActivity {

    private boolean fabExpanded = false;
    private FloatingActionButton fabMenu;

    private LinearLayout layoutFabEditData;

    private TextView nameTV, phoneTV, addressTV, descriptionTV;
    private Restaurant recieveRestaurant;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_resto);

        initializeWidgets();
        receiveAndShowData();
    }

    private void initializeWidgets() {
        nameTV = findViewById(R.id.nameTV);
        descriptionTV = findViewById(R.id.descriptionTV);
        phoneTV = findViewById(R.id.phoneTV);
        addressTV = findViewById(R.id.addressTV);
        mCollapsingToolbarLayout = findViewById(R.id.mCollapsingToolbarLayout);
        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().
                getColor(R.color.colorW));

        fabMenu = (FloatingActionButton) this.findViewById(R.id.fabmenuEditData);

        layoutFabEditData = (LinearLayout) this.findViewById(R.id.layoutFabEditData);

        fabMenu.setOnClickListener(view -> {
            if (fabExpanded) {
                closeSubMenusFab();
            } else {
                openSubMenusFab();
            }
        });
        closeSubMenusFab();
    }

    private void receiveAndShowData() {
        recieveRestaurant = Utils.recieveRestaurant(getIntent(), DetailRestoActivity.this);

        if (recieveRestaurant != null) {
            nameTV.setText(recieveRestaurant.getNameResto());
            phoneTV.setText(recieveRestaurant.getPhone());
            addressTV.setText(recieveRestaurant.getAddress());
            descriptionTV.setText(recieveRestaurant.getDescription());

            mCollapsingToolbarLayout.setTitle(recieveRestaurant.getNameResto());
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    private void closeSubMenusFab() {
        layoutFabEditData.setVisibility(View.INVISIBLE);
        layoutFabEditData.animate().translationY(0);

        fabMenu.setImageResource(R.drawable.add);
        fabExpanded = false;
    }

    private void openSubMenusFab() {
        layoutFabEditData.setVisibility(View.VISIBLE);
        layoutFabEditData.animate().translationY(-getResources().getDimension(R.dimen.standard_55));

        fabMenu.setImageResource(R.drawable.close);
        fabExpanded = true;
    }

    public void clickEditData(View v) {
        Utils.sendRestaurantToActivity(this, recieveRestaurant, CRUDActivity.class);
        finish();
    }
}