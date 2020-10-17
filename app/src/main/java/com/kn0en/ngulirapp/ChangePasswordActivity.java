package com.kn0en.ngulirapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

@SuppressWarnings("DefaultAnnotationParam")
public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {

    Validator validator;
    MaterialButton buttonChange;
    String email, oldPassword, newPassword;
    private FirebaseAuth mAuth;
    @Password(scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS)
    private EditText editOldPassword;
    @Password(min = 6, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS)
    private EditText editNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editOldPassword = findViewById(R.id.edt_tx_oldpass);
        editNewPassword = findViewById(R.id.edt_tx_newpass);
        buttonChange = findViewById(R.id.btnChangepass);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        validator = new Validator(this);
        validator.setValidationListener(this);

        buttonChange.setOnClickListener(this);
    }

    private void updatePassword() {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        email = user.getEmail();
        oldPassword = editOldPassword.getText().toString().trim();
        newPassword = editNewPassword.getText().toString().trim();
        assert email != null;

        mAuth.signInWithEmailAndPassword(email, oldPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mAuth.getCurrentUser()
                        .updatePassword(newPassword)
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                mAuth.signOut();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                Toast.makeText(ChangePasswordActivity.this, "Password Successfully Update!! Please login again..", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ChangePasswordActivity.this, "Something went wrong. Please try again later.", Toast.LENGTH_SHORT).show();

                            }
                        });
            } else {
                Toast.makeText(ChangePasswordActivity.this, "Authentication Failed!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        updatePassword();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(ChangePasswordActivity.this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
