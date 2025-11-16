package com.jelhackers.icemelt.UserStartScreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jelhackers.icemelt.MapsActivity;
import com.jelhackers.icemelt.R;
import com.jelhackers.icemelt.Users.CurrentUser;
import com.jelhackers.icemelt.Users.User;
import com.jelhackers.icemelt.Users.UserController;

public class JavaSignUpScreen extends AppCompatActivity {

    private EditText emailSignupInput, passwordSignupInput, usernameSignupInput;
    private Button signUpBtn, toLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // LOGIN SCREEN
        setContentView(R.layout.activity_sign_up);


        // sign up page
        usernameSignupInput = findViewById(R.id.username_signup_input);
        passwordSignupInput = findViewById(R.id.password_signup_input);
        emailSignupInput = findViewById(R.id.email_signup_input);
        signUpBtn = findViewById(R.id.signup_btn);
        // Takes back to login page
        toLogin = findViewById(R.id.to_login);

        usernameSignupInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Real-time validation
                if (s.length() < 3) {
                    usernameSignupInput.setError("Minimum 2 characters required");
                } else {
                    usernameSignupInput.setError(null); // Remove error if valid
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }
        });

        passwordSignupInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Real-time validation
                String pass = s.toString();

                boolean hasLower = pass.matches(".*[a-z].*");
                boolean hasUpper = pass.matches(".*[A-Z].*");
                boolean hasDigit = pass.matches(".*[0-9].*");
                boolean hasLength = pass.length() >= 8;

                if (!hasLength || !hasLower || !hasUpper || !hasDigit) {
                    passwordSignupInput.setError(
                            "Password must be 8+ chars, include uppercase, lowercase, and a number"
                    );
                } else {
                    passwordSignupInput.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }
        });

        emailSignupInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Real-time validation
                if (s.length() < 3) {
                    emailSignupInput.setError("Valid email address is requied");
                } else {
                    emailSignupInput.setError(null); // Remove error if valid
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }
        });
        // Handle button clicks
        // SIGN UP BUTTON
        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String username = usernameSignupInput.getText().toString();
                String password = passwordSignupInput.getText().toString();
                String email = emailSignupInput.getText().toString();


                // TESTING

                Log.d("SIGNUP", "Username: " + username);
                Log.d("SIGNUP", "Password" + password);
                Log.d("SIGNUP", "email: " + email);

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(username)) {
                    Toast.makeText(JavaSignUpScreen.this, "Email / Password / Username cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(JavaSignUpScreen.this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    CurrentUser.getInstance().setid(user.getUid());
                                    CurrentUser.getInstance().setEmail(email);
                                    CurrentUser.getInstance().setUsername(username);
                                    CurrentUser.getInstance().setPassword(password);

                                    UserController controller = new UserController();

                                    controller.addUser(user.getUid(), username, email, password, new UserController.AddUserCallback() {
                                        @Override
                                        public void onSuccess(User user) {
                                            Log.d("SIGNUP", "User added to Firestore!");
                                        }

                                        @Override
                                        public void onFailure(String error) {
                                            Toast.makeText(JavaSignUpScreen.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    Toast.makeText(JavaSignUpScreen.this, "Account created successfully", Toast.LENGTH_SHORT).show();

                                    // Navigate to main app screen
                                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                        startActivity(new Intent(JavaSignUpScreen.this, MapsActivity.class));
                                        finish();
                                    }, 300);
                                }

                            } else {
                                Toast.makeText(JavaSignUpScreen.this,
                                        "Sign up failed: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // TO LOGIN PAGE BUTTON
        toLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(JavaSignUpScreen.this, JavaStartScreen.class);
                startActivity(i);
            }
        });


    }

}

