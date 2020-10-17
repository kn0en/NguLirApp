package com.kn0en.ngulirapp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kn0en.ngulirapp.adapter.RestoAdapter;
import com.kn0en.ngulirapp.functions.FirebaseCRUDHelper;
import com.kn0en.ngulirapp.functions.Utils;
import com.kn0en.ngulirapp.models.Restaurant;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class RestoActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private final FirebaseCRUDHelper crudHelper = new FirebaseCRUDHelper();
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public RecyclerView rv;
    public ProgressBar mProgressBar;
    public RestoAdapter restoAdapter;
    public Restaurant recieveRestaurant;
    private boolean fabExpanded = false;
    private FloatingActionButton fabMenu;
    private LinearLayout layoutFabAddData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resto);

        initializeViews();
        bindData();

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(true);
        searchView.setQueryHint("Search");

    }

    private void initializeViews() {
        mProgressBar = findViewById(R.id.mProgressBarLoad);
        mProgressBar.setIndeterminate(true);
        Utils.showProgressBar(mProgressBar);
        rv = findViewById(R.id.mRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(),
                layoutManager.getOrientation());
        rv.addItemDecoration(dividerItemDecoration);
        restoAdapter = new RestoAdapter(this, Utils.DataCache);
        rv.setAdapter(restoAdapter);

        fabMenu = (FloatingActionButton) this.findViewById(R.id.fabmenuAddData);

        layoutFabAddData = (LinearLayout) this.findViewById(R.id.layoutFabAddData);

        fabMenu.setOnClickListener(view -> {
            if (fabExpanded) {
                closeSubMenusFab();
            } else {
                openSubMenusFab();
            }
        });
        closeSubMenusFab();
    }

    private void bindData() {
        crudHelper.select(this, Utils.getDatabaseRefence(), user, mProgressBar, rv, restoAdapter);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        Utils.searchString = query;
        RestoAdapter adapter = new RestoAdapter(this, Utils.DataCache);
        adapter.getFilter().filter(query);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        return false;
    }

    private void closeSubMenusFab() {
        layoutFabAddData.setVisibility(View.INVISIBLE);
        layoutFabAddData.animate().translationY(0);

        fabMenu.setImageResource(R.drawable.add);
        fabExpanded = false;
    }

    private void openSubMenusFab() {
        layoutFabAddData.setVisibility(View.VISIBLE);
        layoutFabAddData.animate().translationY(-getResources().getDimension(R.dimen.standard_55));

        fabMenu.setImageResource(R.drawable.close);
        fabExpanded = true;
    }

    public void clickAddData(View v) {
        Utils.sendRestaurantToActivity(this, recieveRestaurant, CRUDActivity.class);
        finish();
    }

}