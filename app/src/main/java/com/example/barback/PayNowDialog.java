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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PayNowDialog extends DialogFragment {

    private boolean buttonClicked;
    private Long maxID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay_now_dialog, container, false);

        Button confirmOrderButton = view.findViewById(R.id.confirmOrderButton);
        Button cancelButton = view.findViewById(R.id.cancelPayNowButton);
        TextView itemsTotalTV = view.findViewById(R.id.payNowDialogItemsTV);
        TextView totalBeforeTaxTV = view.findViewById(R.id.payNowDialogTotalBeforeTaxTV);
        TextView estimatedTaxTV = view.findViewById(R.id.payNowDialogEstimatedTaxTV);
        TextView orderTotalTV = view.findViewById(R.id.payNowDialogOrderTotalTV);

        Bundle bundle = this.getArguments();

        Double subtotal = bundle.getDouble("subtotal", 0);
        Double estimatedTax = bundle.getDouble("estimatedTax", 0);
        Double orderTotal = bundle.getDouble("orderTotal",0);

        DecimalFormat df = new DecimalFormat("#.##");

        String dfSubtotal = df.format(subtotal);
        String dfEstimatedTax = df.format(estimatedTax);
        String dfOrderTotal = df.format(orderTotal);

        itemsTotalTV.setText("$" + dfSubtotal);
        totalBeforeTaxTV.setText("$" + dfSubtotal);
        estimatedTaxTV.setText("$" + dfEstimatedTax);
        orderTotalTV.setText("$" + dfOrderTotal);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                final Date date = new Date();
                final String orderDate = formatter.format(date);
                buttonClicked = true;
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Checkout").child(FirebaseAuth.getInstance().getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists() && buttonClicked == true){
                            FirebaseDatabase.getInstance().getReference("OrderHistory").child(FirebaseAuth.getInstance().getUid()).removeValue();
                            FirebaseDatabase.getInstance().getReference("OrderHistory").child(FirebaseAuth.getInstance().getUid()).setValue(dataSnapshot.getValue());
                        }

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("OrderHistory").child(FirebaseAuth.getInstance().getUid());
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    maxID = dataSnapshot.getChildrenCount();
                                    for(int i = 1; i <= maxID.intValue(); i++){
                                        FirebaseDatabase.getInstance().getReference("OrderHistory").child(FirebaseAuth.getInstance().getUid()).child(Integer.toString(i)).child("orderDate").setValue(orderDate);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        buttonClicked = false;
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                reference.removeValue();
                getDialog().dismiss();
                Toast.makeText(getContext(), R.string.order_placed, Toast.LENGTH_SHORT).show();
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
    }
}
