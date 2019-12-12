package com.example.barback;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;


public class DrinkDialog extends DialogFragment {

    private Button addQuantityButton;
    private Button addToCartButton;
    private Button subtractQuantityButton;
    private FirebaseAuth firebaseAuth;
    private TextView drinkNameTV;
    private TextView drinkPriceTV;
    private TextView quantityNumTV;
    private long maxId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drink_dialog, container, false);
        addToCartButton = view.findViewById(R.id.drink_dialog_add_to_cart_button);
        addQuantityButton = view.findViewById(R.id.quantity_add_button);
        subtractQuantityButton = view.findViewById(R.id.quantity_subtract_button);
        quantityNumTV = view.findViewById(R.id.quantity_number_tv);
        drinkNameTV = view.findViewById(R.id.drink_dialog_name);
        drinkPriceTV = view.findViewById(R.id.drink_dialog_price);

        ImageView drinkIV = view.findViewById(R.id.drink_dialog_image);
        TextView drinkABVTV = view.findViewById(R.id.drink_dialog_abv);
        TextView drinkCaloriesTV = view.findViewById(R.id.drink_dialog_calories);
        TextView drinkTasteTV = view.findViewById(R.id.drink_dialog_taste);

        firebaseAuth = FirebaseAuth.getInstance();

        Bundle bundle = this.getArguments();
        String imageURL = bundle.getString("imageURL", "");
        drinkABVTV.setText(bundle.getString("abv", "") + "%");
        drinkCaloriesTV.setText(bundle.getString("cal", ""));
        drinkNameTV.setText(bundle.getString("name", ""));
        drinkTasteTV.setText(bundle.getString("taste", ""));
        drinkPriceTV.setText("$" + bundle.getString("price", ""));
        Picasso.get().load(imageURL).into(drinkIV);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Checkout").child(FirebaseAuth.getInstance().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxId = dataSnapshot.getChildrenCount();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setGravity(Gravity.CENTER);
        addQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(quantityNumTV.getText().toString());
                quantity++;
                String quantityString = Integer.toString(quantity);
                quantityNumTV.setText(quantityString);
            }
        });

        subtractQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(quantityNumTV.getText().toString());
                quantity--;
                if(quantity >= 1){
                    String quantityString = Integer.toString(quantity);
                    quantityNumTV.setText(quantityString);
                }
            }
        });

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getArguments();
                String imageURL = bundle.getString("imageURL", "");
                int quantity = Integer.parseInt((quantityNumTV.getText().toString()));
                double price = Double.parseDouble(bundle.getString("price", ""));
                double total = quantity * price;

                DecimalFormat df = new DecimalFormat("#.00");
                String totalString = df.format(total);

                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                CartInformation cartInformation = new CartInformation(drinkNameTV.getText().toString(), imageURL, quantityNumTV.getText().toString(), drinkPriceTV.getText().toString(), "$" + totalString);

                FirebaseDatabase.getInstance().getReference("Checkout").child(currentUser.getUid()).child(String.valueOf(++maxId)).setValue(cartInformation);
                Toast.makeText(getContext(), R.string.added_to_cart, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
