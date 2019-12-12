package com.example.barback;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryViewHolder extends RecyclerView.ViewHolder {

    private View mView;

    // Constructor for CartViewHolder class
    public HistoryViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    //****************************************************
    // Method: setDetails
    //
    // Purpose: Method used for RecyclerView found inside
    // the CheckoutFragment class. This method sets all the
    // necessary details for individual items in the cart.
    //****************************************************
    public void setDetails(Context context, String date, String name, String image, String quantity, String totalAmount) {
        TextView orderDateTV = mView.findViewById(R.id.HistoryOrderDate);
        TextView drinkNameTV = mView.findViewById(R.id.HistoryDrinkName);
        ImageView drinkImageIV = mView.findViewById(R.id.HistoryDrinkImage);
        TextView drinkQuantityTV = mView.findViewById(R.id.HistoryDrinkQuantity);
        TextView drinkTotalAmount = mView.findViewById(R.id.HistoryDrinkTotalPrice);

        orderDateTV.setText(date);
        drinkNameTV.setText(name);
        Picasso.get().load(image).into(drinkImageIV);
        drinkQuantityTV.setText(quantity);
        drinkTotalAmount.setText(totalAmount);

    }

}
