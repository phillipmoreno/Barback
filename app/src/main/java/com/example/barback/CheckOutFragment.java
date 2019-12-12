package com.example.barback;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.DecimalFormat;

public class CheckOutFragment extends Fragment {
    private Button clearCartButton;
    private Button payNowButton;
    private RecyclerView recyclerView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private TextView subtotalTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_check_out, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clearCartButton = view.findViewById(R.id.clear_cart_button);
        payNowButton = view.findViewById(R.id.payNowButton);
        recyclerView = view.findViewById(R.id.CartRecyclerView);
        subtotalTV = view.findViewById(R.id.subtotalTV);
        final PayNowDialog payNowDialog = new PayNowDialog();

        // Clear Cart Button is given instructions
        clearCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                // Create and show the dialog.
                ClearCartDialog clearCartDialog = new ClearCartDialog();
                clearCartDialog.show(ft, "dialog");

            }
        });


        recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutManager.getItemCount() > 0) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    payNowDialog.show(ft, "dialog");
                }else{
                    // If cart is empty, user will receive a Toast message saying so
                    Toast.makeText(getContext(), R.string.no_items, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Instance of Firebase Realtime Database is retrieved
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Checkout").child(FirebaseAuth.getInstance().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long longChildCount = dataSnapshot.getChildrenCount();
                    int childCount = (int) longChildCount;

                    double totalAmount;
                    double subtotal = 0;
                    DecimalFormat df = new DecimalFormat("#.##");
                    // For loop used to calculate the subtotal of all the items in the cart
                    for (int i = 1; i <= childCount; i++) {
                        String integerString = Integer.toString(i);
                        String totalAmountString = dataSnapshot.child(integerString).child("total").getValue(String.class);
                        totalAmount = Double.valueOf(totalAmountString.substring(1));
                        subtotal += totalAmount;
                    }
                    // All necessary cart information is passed to the Pay Now Dialog Fragment
                    Bundle bundle = new Bundle();
                    double estimatedTax = subtotal * 0.08875;
                    double orderTotal = subtotal + estimatedTax;
                    bundle.putDouble("subtotal", subtotal);
                    bundle.putDouble("estimatedTax", estimatedTax);
                    bundle.putDouble("orderTotal", orderTotal);
                    payNowDialog.setArguments(bundle);
                    subtotalTV.setText("Your subtotal is: $" + df.format(subtotal));
                } else {
                    subtotalTV.setText("Your cart is currently empty");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // RecyclerView is populated on Start
        FirebaseRecyclerAdapter<CartInformation, CartViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CartInformation, CartViewHolder>(
                CartInformation.class, R.layout.cart_item_row, CartViewHolder.class, reference) {
            @Override
            protected void populateViewHolder(CartViewHolder viewHolder, final CartInformation cartInformation, int i) {
                viewHolder.setDetails(getContext(), cartInformation.getName(), cartInformation.getImage(), cartInformation.getQuantity(), cartInformation.getUnitPrice(), cartInformation.getTotal());
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
