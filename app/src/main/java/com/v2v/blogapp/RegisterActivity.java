package com.v2v.blogapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword;
    RadioGroup userTypeGroup;
    Button btnRegister;
    TextView tvLogin;

    FirebaseAuth mAuth;
    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.rgName);
        etEmail = findViewById(R.id.rgEmail);
        etPassword = findViewById(R.id.rgPassword);
        userTypeGroup = findViewById(R.id.userTypeGroup);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        int selectedId = userTypeGroup.getCheckedRadioButtonId();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Enter name");
            return;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter valid email");
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }

        if (selectedId == -1) {
            Toast.makeText(this, "Select a user type", Toast.LENGTH_SHORT).show();
            return;
        }

        String userType = ((RadioButton) findViewById(selectedId)).getText().toString().toLowerCase();
        String username = email.split("@")[0].toLowerCase();  // generate a username from email

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

                        if (uid == null) {
                            Toast.makeText(this, "User ID is null", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        HashMap<String, String> userMap = new HashMap<>();
                        userMap.put("name", name);
                        userMap.put("email", email);
                        userMap.put("username", username);
                        userMap.put("usertype", userType);

                        usersRef.child(uid).setValue(userMap)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}