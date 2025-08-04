package com.v2v.blogapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class ProfileFragment extends Fragment {

    TextView name, email, userType;
    FirebaseAuth mAuth;
    DatabaseReference userRef;

    public ProfileFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        name = view.findViewById(R.id.textName);
        email = view.findViewById(R.id.textEmail);
        userType = view.findViewById(R.id.textUserType);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            userRef = FirebaseDatabase.getInstance().getReference("Users")
                    .child(mAuth.getCurrentUser().getUid());

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String userName = snapshot.child("name").getValue(String.class);
                        String userEmail = snapshot.child("email").getValue(String.class);
                        String type = snapshot.child("userType").getValue(String.class);

                        name.setText("Name: " + (userName != null ? userName : "Not available"));
                        email.setText("Email: " + (userEmail != null ? userEmail : "Not available"));
                        userType.setText("User Type: " + (type != null ? type : "Not available"));
                    } else {
                        name.setText("Pranjali Chauhan");
                        email.setText("pranjalic829@gmail.com");
                        userType.setText("Admin");
                    }
                }

                @Override public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("ProfileFragment", "Database error: " + error.getMessage());
                }
            });
        } else {
            name.setText("Name: Not logged in");
            email.setText("Email: Not logged in");
            userType.setText("User Type: Not logged in");
        }

        return view;
    }
}