package com.example.barback;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DiscoverFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("Beers");
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(
                Model.class, R.layout.row, ViewHolder.class, ref) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, final Model model, int i) {
                viewHolder.setDetails(getContext(), model.getName(), model.getImage(), model.getCal(),model.getAbv() + "%", model.getTaste(), "$" + model.getPrice());
                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putString("abv", model.getAbv());
                        bundle.putString("cal", model.getCal());
                        bundle.putString("name", model.getName());
                        bundle.putString("imageURL", model.getImage());
                        bundle.putString("taste", model.getTaste());
                        bundle.putString("price", model.getPrice());
                        DrinkDialog drinkDialog = new DrinkDialog();
                        drinkDialog.setArguments(bundle);
                        drinkDialog.show(ft, "drinkdialog");
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

}