package com.kn0en.ngulirapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kn0en.ngulirapp.models.UserInfo;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static int PICK_IMAGE = 123;
    Button btnsave;
    Uri imagePath;
    UserInfo nameUser, birthDateUser, phoneUser, addressUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private EditText editTextName;
    private EditText editTextBirthdate;
    private EditText editTextPhoneNo;
    private EditText editTextAddress;
    private ImageView profileImageView;
    private StorageReference storageReference;

    public ProfileActivity() {
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null) {
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Window window = ProfileActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(ProfileActivity.this, R.color.colorProfileStatusBar));

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        initialize();
        showData();

    }

    private void initialize() {
        databaseReference = FirebaseDatabase.getInstance().getReference();

        editTextName = (EditText) findViewById(R.id.edt_txt_nameUser);
        editTextBirthdate = (EditText) findViewById(R.id.edt_txt_birthateUser);
        editTextPhoneNo = (EditText) findViewById(R.id.edt_txt_userPhone);
        editTextAddress = (EditText) findViewById(R.id.edt_txt_userAddress);

        btnsave = (Button) findViewById(R.id.btnCreateProfile);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        btnsave.setOnClickListener(this);

        TextView textViewemailname = (TextView) findViewById(R.id.edt_txt_userEmail);
        textViewemailname.setText(user.getEmail());

        profileImageView = findViewById(R.id.imgUser);

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        profileImageView.setOnClickListener(v -> {
            Intent profileIntent = new Intent();
            profileIntent.setType("image/*");
            profileIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(profileIntent, "Select Image."), PICK_IMAGE);
        });
    }

    private void userInformation() {
        String name = editTextName.getText().toString().trim();
        String birthdate = editTextBirthdate.getText().toString().trim();
        String phoneno = editTextPhoneNo.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        UserInfo userinformation = new UserInfo(name, birthdate, phoneno, address);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        databaseReference.child("Users").child(user.getUid()).child("userInfo").setValue(userinformation);
        Toast.makeText(getApplicationContext(), "User information updated", Toast.LENGTH_LONG).show();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onClick(View view) {
        if (view == btnsave) {
            if (imagePath == null) {

                this.getResources().getDrawable(R.drawable.avatar);
                BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
                // openSelectProfilePictureDialog();
                userInformation();
                // sendUserData();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                userInformation();
                sendUserData();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    private void sendUserData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        // Get "User UID" from Firebase > Authentification > Users.
        firebaseDatabase.getReference(Objects.requireNonNull(firebaseAuth.getUid()));
        StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Pic"); //User id/Images/Profile Pic.jpg
        UploadTask uploadTask = imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(e ->
                Toast.makeText(ProfileActivity.this, "Error: Uploading profile picture",
                        Toast.LENGTH_SHORT).show()).addOnSuccessListener(taskSnapshot ->
                Toast.makeText(ProfileActivity.this, "Profile picture uploaded",
                        Toast.LENGTH_SHORT).show());
    }

    private void showData() {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(user.getUid()).child("userInfo").getRef();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    nameUser = snapshot.getValue(UserInfo.class);
                    birthDateUser = snapshot.getValue(UserInfo.class);
                    phoneUser = snapshot.getValue(UserInfo.class);
                    addressUser = snapshot.getValue(UserInfo.class);
                    editTextName.setText(nameUser.getNameUser());
                    editTextBirthdate.setText(nameUser.getBdUser());
                    editTextPhoneNo.setText(nameUser.getPhoneUser());
                    editTextAddress.setText(nameUser.getAddressUser());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        // Get the image stored on Firebase via "User id/Images/Profile Pic.jpg".
        storageReference.child(user.getUid()).child("Images").child("Profile Pic").getDownloadUrl().addOnSuccessListener(uri -> {
            // Using "Picasso" (http://square.github.io/picasso/) after adding the dependency in the Gradle.
            // ".fit().centerInside()" fits the entire image into the specified area.
            // Finally, add "READ" and "WRITE" external storage permissions in the Manifest.
            Picasso.get().load(uri).fit().centerInside().into(profileImageView);
        });
    }
}

