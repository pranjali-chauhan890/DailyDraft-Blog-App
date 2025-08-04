package com.v2v.blogapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogFragment extends Fragment {

    private EditText titleEditText, descriptionEditText, imageUrlEditText;
    private Button submitButton;

    private DatabaseReference databaseRef;

    public LogFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log, container, false);

        // Initialize views
        titleEditText = view.findViewById(R.id.entryTitle);
        descriptionEditText = view.findViewById(R.id.entryDescription);
        imageUrlEditText = view.findViewById(R.id.entryUrl);
        submitButton = view.findViewById(R.id.btnSaveLog);

        // Firebase reference
        databaseRef = FirebaseDatabase.getInstance().getReference("Entries");

        // Submit button logic
        submitButton.setOnClickListener(v -> submitEntry());

        return view;
    }

    private void submitEntry() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String imageUrl = imageUrlEditText.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(getContext(), "Title is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(description)) {
            Toast.makeText(getContext(), "Description is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(imageUrl)) {
            Toast.makeText(getContext(), "Image URL is required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get author name from Firebase user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String author = currentUser != null && currentUser.getDisplayName() != null
                ? currentUser.getDisplayName()
                : "Anonymous";

        // Generate ID and create entry
        String id = databaseRef.push().getKey();
        HomeEntry entry = new HomeEntry(title, author, description, imageUrl);

        // Push to Firebase
        databaseRef.child(id).setValue(entry)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "Entry submitted", Toast.LENGTH_SHORT).show();
                    titleEditText.setText("");
                    descriptionEditText.setText("");
                    imageUrlEditText.setText("");
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to submit entry", Toast.LENGTH_SHORT).show());
    }
}