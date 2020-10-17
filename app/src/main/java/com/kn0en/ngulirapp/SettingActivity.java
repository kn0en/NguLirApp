package com.kn0en.ngulirapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

@SuppressWarnings("ALL")
public class SettingActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

    }

    public void clickChangePw(@SuppressWarnings("unused") View v) {
        Intent i = new Intent(SettingActivity.this, ChangePasswordActivity.class);
        startActivity(i);
    }

    public void clickSignout(@SuppressWarnings("unused") View v) {
        //logging out the user
        mAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Toast.makeText(this, "Sign out successful!!", Toast.LENGTH_SHORT).show();
    }

}