package com.v2v.blogapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    private DatabaseReference userRef;
    private String userType = "";
    private String name = "", email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottomNavigationView);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

        if (uid == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        userRef = FirebaseDatabase.getInstance().getReference("users");

        userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    name = snapshot.child("name").getValue(String.class);
                    email = snapshot.child("email").getValue(String.class);
                    userType = snapshot.child("userType").getValue(String.class);

                    Log.d("MainActivity", "name: " + name + ", email: " + email + ", userType: " + userType);

                    // Load default fragment
                    if ("Admin".equalsIgnoreCase(userType)) {
                        loadFragment(new HomeFragment());
                    } else {
                        loadFragment(new HomeFragment());
                    }

                    setupNavigation(); // Now that userType is known, set up the nav
                } else {
                    Toast.makeText(MainActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "No data found at path: users/" + uid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Database error: " + error.getMessage());
            }
        });
    }

    private void setupNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();

            if ("Admin".equalsIgnoreCase(userType)) {
                if (itemId == R.id.nav_home) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.nav_log) {
                    selectedFragment = new LogFragment();
                } else if (itemId == R.id.nav_profile) {
                    selectedFragment = new ProfileFragment();
                }
            } else {
                if (itemId == R.id.nav_home) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.nav_log) {
                    selectedFragment = new LogFragment();
                } else if (itemId == R.id.nav_profile) {
                    selectedFragment = new ProfileFragment();
                }
            }

            return loadFragment(selectedFragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}