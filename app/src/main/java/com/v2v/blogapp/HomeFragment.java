package com.v2v.blogapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayout adminLayout;
    private HomeAdapter adapter;
    private ArrayList<HomeEntry> entryList;
    private DatabaseReference databaseRef;

    public HomeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewHome);
        adminLayout = view.findViewById(R.id.adminLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        entryList = new ArrayList<>();
        adapter = new HomeAdapter(getContext(), entryList);
        recyclerView.setAdapter(adapter);

        databaseRef = FirebaseDatabase.getInstance().getReference("Entries");

        fetchEntries();

        return view;
    }

    private void fetchEntries() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                entryList.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    HomeEntry entry = postSnapshot.getValue(HomeEntry.class);
                    if (entry != null) {
                        entryList.add(entry);
                    }
                }

                if (entryList.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    adminLayout.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    adminLayout.setVisibility(View.GONE);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }
}