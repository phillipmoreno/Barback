package com.example.barback;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.HistoryRecyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("OrderHistory").child(FirebaseAuth.getInstance().getUid());
    }

    @Override
    public void onStart() {
        super.onStart();
        // RecyclerView is populated on Start
        FirebaseRecyclerAdapter<HistoryInformation, HistoryViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<HistoryInformation, HistoryViewHolder>(
                HistoryInformation.class, R.layout.history_item_row, HistoryViewHolder.class, ref) {
            @Override
            protected void populateViewHolder(HistoryViewHolder viewHolder, final HistoryInformation history, int i) {
                viewHolder.setDetails(getContext(), history.getOrderDate(), history.getName(), history.getImage(), history.getQuantity(), history.getTotal());
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

}