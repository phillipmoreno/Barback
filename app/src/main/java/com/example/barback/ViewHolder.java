package com.example.barback;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    private View mView;
    private ViewHolder.ClickListener mClickListener;

    public ViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });
    }

    //****************************************************
    // Method: setDetails
    //
    // Purpose: Method used for RecyclerView found inside
    // the DiscoverDrink class. This method sets all the
    // necessary details for individual drinks found in the
    // Recycler View.
    //****************************************************
    public void setDetails(Context context, String drinkName, String image, String calories, String abv, String taste, String price){
        TextView drinkNameTV = mView.findViewById(R.id.rDrinkName);
        ImageView drinkImageIV = mView.findViewById(R.id.rDrinkImage);
        TextView calTV = mView.findViewById(R.id.rCalories);
        TextView abvTV = mView.findViewById(R.id.rABV);
        TextView tasteTV = mView.findViewById(R.id.rTaste);
        TextView priceTV = mView.findViewById(R.id.rPrice);

        drinkNameTV.setText(drinkName);
        Picasso.get().load(image).into(drinkImageIV);
        calTV.setText(calories);
        abvTV.setText(abv);
        tasteTV.setText(taste);
        priceTV.setText(price);
    }

    // Interface used for displaying the Drink Dialog Fragment when tapping on the drink in the
    // Discover Fragment
    public interface ClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }

}
