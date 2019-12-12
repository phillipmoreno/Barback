package com.example.barback;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import androidx.recyclerview.widget.RecyclerView;

public class CartViewHolder extends RecyclerView.ViewHolder {

    private View mView;

    // Constructor for CartViewHolder class
    public CartViewHolder(View itemView) {
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
    public void setDetails(Context context, String drinkName, String drinkImage, String quantity, String unitPrice, String totalAmount) {
        TextView drinkNameTV = mView.findViewById(R.id.CartDrinkName);
        ImageView drinkImageIV = mView.findViewById(R.id.CartDrinkImage);
        TextView drinkQuantityTV = mView.findViewById(R.id.CartDrinkQuantity);
        TextView drinkUnitPrice = mView.findViewById(R.id.CartDrinkUnitPrice);
        TextView drinkTotalAmount = mView.findViewById(R.id.CartDrinkTotalPrice);

        drinkNameTV.setText(drinkName);
        Picasso.get().load(drinkImage).into(drinkImageIV);
        drinkQuantityTV.setText(quantity);
        drinkUnitPrice.setText(unitPrice);
        drinkTotalAmount.setText(totalAmount);

    }

}
