package com.kn0en.ngulirapp.functions;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kn0en.ngulirapp.RestoActivity;
import com.kn0en.ngulirapp.adapter.RestoAdapter;
import com.kn0en.ngulirapp.models.Restaurant;

import java.util.Objects;

import static com.kn0en.ngulirapp.functions.Utils.DataCache;


@SuppressWarnings("deprecation")
public class FirebaseCRUDHelper {

    public void insert(final AppCompatActivity a,
                       final DatabaseReference mDatabaseRef,
                       FirebaseUser mUser,
                       final ProgressBar pb, final Restaurant restaurant) {
        //check if they have passed us a valid scientist. If so then return false.
        if (restaurant == null) {
            Utils.showInfoDialog(a, "VALIDATION FAILED", "Restaurant is null");
        } else {
            //otherwise try to push data to firebase database.
            Utils.showProgressBar(pb);
            //push data to FirebaseDatabase. Table or Child called Scientist will be
            // created.
            mDatabaseRef.child("Users").child(mUser.getUid()).child("Restaurants").push().setValue(restaurant).
                    addOnCompleteListener(task -> {
                        Utils.hideProgressBar(pb);

                        if (task.isSuccessful()) {
                            Utils.openActivity(a, RestoActivity.class);
                            a.finish();
                            Utils.show(a, "Congrats! INSERT SUCCESSFUL");
                        } else {
                            Utils.showInfoDialog(a, "UNSUCCESSFUL", Objects.requireNonNull(task.getException()).
                                    getMessage());
                        }
                    });
        }
    }

    public void select(final AppCompatActivity a, DatabaseReference db,
                       FirebaseUser mUser,
                       final ProgressBar pb,
                       final RecyclerView rv, RestoAdapter adapter) {
        Utils.showProgressBar(pb);

        db.child("Users").child(mUser.getUid()).child("Restaurants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataCache.clear();
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //Now get Restaurant Objects and populate our arraylist.
                        Restaurant restaurant = ds.getValue(Restaurant.class);
                        Objects.requireNonNull(restaurant).setKey(ds.getKey());
                        DataCache.add(restaurant);
                    }

                    adapter.notifyDataSetChanged();

                    new Handler().post(() -> {
                        Utils.hideProgressBar(pb);
                        rv.smoothScrollToPosition(DataCache.size());
                    });
                } else {
                    Utils.show(a, "No more item found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("FIREBASE CRUD", databaseError.getMessage());
                Utils.hideProgressBar(pb);
                Utils.showInfoDialog(a, "CANCELLED", databaseError.getMessage());
            }
        });
    }

    public void update(final AppCompatActivity a,
                       final DatabaseReference mDatabaseRef,
                       FirebaseUser mUser,
                       final ProgressBar pb,
                       final Restaurant oldRestaurant,
                       final Restaurant newRestaurant) {

        if (oldRestaurant == null) {
            Utils.showInfoDialog(a, "VALIDATION FAILED", "Old Scientist is null");
            return;
        }

        Utils.showProgressBar(pb);
        mDatabaseRef.child("Users").child(mUser.getUid()).child("Restaurants").child(oldRestaurant.getKey()).setValue(
                newRestaurant)
                .addOnCompleteListener(task -> {
                    Utils.hideProgressBar(pb);

                    if (task.isSuccessful()) {
                        Utils.show(a, oldRestaurant.getNameResto() + " Update Successful.");
                        Intent intent = new Intent(a.getApplicationContext(), RestoActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        a.startActivity(intent);
                    } else {
                        Utils.showInfoDialog(a, "UNSUCCESSFUL", Objects.requireNonNull(task.getException()).
                                getMessage());
                    }
                });
    }

    public void delete(final AppCompatActivity a, final DatabaseReference mDatabaseRef,
                       FirebaseUser mUser,
                       final ProgressBar pb, final Restaurant selectedRestaurant) {
        Utils.showProgressBar(pb);
        final String selectedRestaurantKey = selectedRestaurant.getKey();
        mDatabaseRef.child("Users").child(mUser.getUid()).child("Restaurants").child(selectedRestaurantKey).removeValue().
                addOnCompleteListener(task -> {
                    Utils.hideProgressBar(pb);

                    if (task.isSuccessful()) {
                        Utils.show(a, selectedRestaurant.getNameResto() + " Successfully Deleted.");
                        Intent intent = new Intent(a.getApplicationContext(), RestoActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        a.startActivity(intent);
                    } else {
                        Utils.showInfoDialog(a, "UNSUCCESSFUL", Objects.requireNonNull(task.getException()).getMessage());
                    }
                });

    }
}
