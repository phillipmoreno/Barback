package com.example.barback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "FACELOG";
    public static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //1 digit minimum
                    "(?=.*[a-z])" +         //1 lowercase letter minimum
                    "(?=.*[A-Z])" +         //1 lowercase letter maximum
                    "(?=.*[@#$%^&+=])" +    //1 special character minimum
                    "(?=\\S+$)" +           //no spaces allowed
                    ".{7,}" +               //7 character minimum
                    "$");

    TextView emailTV, passwordTV;
    FirebaseAuth mFireBaseAuth;
    OnCompleteListener onCompleteListener;
    CallbackManager mCallbackManager;
    FacebookCallback<LoginResult> mCallback;
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailTV = findViewById(R.id.emailLogin);
        passwordTV = findViewById(R.id.passwordLogin);
        loginButton = findViewById(R.id.facebook_login_button);
        mFireBaseAuth = FirebaseAuth.getInstance();

        onCompleteListener = new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, R.string.sign_in_unsuccessful, Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseUser currentUser = mFireBaseAuth.getCurrentUser();
                    updateUI(currentUser);
                }
            }
        };

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        mCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        };
        loginButton.setPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, mCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void verifyLogin(View view) {
        validateEmail();
        validatePassword();

        String email = emailTV.getText().toString();
        String password = passwordTV.getText().toString();

        if (validateEmail() && validatePassword()) {
            mFireBaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, onCompleteListener);
        }
    }

    public void registrationStart(View view) {
        Intent i = new Intent(this, RegistrationActivity.class);
        startActivity(i);
        Animatoo.animateFade(this);
        finish();
    }

    //****************************************************
    // Method: validateEmail
    //
    // Purpose: Method used to validate the email input
    // provided by the user. The email must meet several
    // requirements to return a true value.
    //****************************************************
    public boolean validateEmail() {
        String email = emailTV.getText().toString().trim();

        if (email.isEmpty()) {
            emailTV.setError("Missing email field");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTV.setError("Please enter a valid email address");
            return false;
        } else {
            emailTV.setError(null);
            return true;
        }
    }

    //****************************************************
    // Method: validatePassword
    //
    // Purpose: Method used to validate the password input
    // provided by the user. Password must meet several
    // requirements to return a true value.
    //****************************************************
    public boolean validatePassword() {
        String password = passwordTV.getText().toString().trim();

        if (password.isEmpty()) {
            passwordTV.setError("Missing password field");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            passwordTV.setError("Password must: " +
                    "\n- Be at least seven characters long" +
                    "\n- Have at least one capital letter" +
                    "\n- Have at least one lowercase letter" +
                    "\n- Have at least one number" +
                    "\n- Have at least one special character");
            return false;
        } else {
            passwordTV.setError(null);
            return true;
        }
    }

    public void updateUI(FirebaseUser user) {

        if (user != null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            Animatoo.animateFade(LoginActivity.this);
            finish();
        } else {
            recreate();
            finish();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mFireBaseAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFireBaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser currentUser = mFireBaseAuth.getCurrentUser();
                            UserInformation user = new UserInformation(currentUser.getEmail(), currentUser.getDisplayName());
                            FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid()).setValue(user);
                            updateUI(currentUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
}
