package com.example.barback;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HomeFragment extends Fragment {

    private TextView abvTV;
    private TextView caloriesTV;
    private ImageView drinkImage;
    private TextView drinkNameTV;
    private TextView featuredTV;
    private TextView greetingTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        abvTV = view.findViewById(R.id.home_abv);
        greetingTV = view.findViewById(R.id.text_home);
        featuredTV = view.findViewById(R.id.home_featured);
        caloriesTV = view.findViewById(R.id.home_calories);
        drinkImage = view.findViewById(R.id.home_drink_img);
        drinkNameTV = view.findViewById(R.id.home_drink_name);

        // Checks if the user is logged into facebook or not
        boolean loggedIn = AccessToken.getCurrentAccessToken() !=null;

        // Different customized greetings based on form of login
        if(loggedIn) {
            ProfilePictureView profilePictureView;

            profilePictureView = view.findViewById(R.id.friendProfilePicture);

            profilePictureView.setProfileId(Profile.getCurrentProfile().getId());

            greetingTV.setText("Welcome back, " + Profile.getCurrentProfile().getFirstName() + "!");
        }else{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String firstName = dataSnapshot.child("firstName").getValue().toString();
                    greetingTV.setText("Welcome back, " + firstName + "!");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Featured").child("1");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                Date date = new Date();
                String abv = dataSnapshot.child("abv").getValue().toString();
                String calories = dataSnapshot.child("cal").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String imageURL = dataSnapshot.child("image").getValue().toString();
                Picasso.get().load(imageURL).into(drinkImage);
                abvTV.setText("ABV: " + abv + "%");
                caloriesTV.setText("Calories: " + calories);
                drinkNameTV.setText(name);
                featuredTV.setText("Featured drink of the day: " + formatter.format(date));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}