package com.jelhackers.icemelt.UserStartScreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
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

public class JavaStartScreen extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginBtn, toSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // LOGIN SCREEN
        setContentView(R.layout.activity_main);


        // For retrieving strings for login

        // 1. Connect java variables to XML views
        // On login page
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginBtn = findViewById(R.id.login_btn);
        // Takes to the sign in screen
        toSignUp = findViewById(R.id.to_signup_btn);




        // Handle button clicks
        // LOGIN BUTTON
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                // Now we can use the variables

                Log.d("LOGIN", "Email: " + email);
                Log.d("LOGIN", "Password" + password);

                // Input validation
                if (TextUtils.isEmpty(email)) {
                    emailInput.setError("Email required");
                    emailInput.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    passwordInput.setError("Password required");
                    passwordInput.requestFocus();
                    return;
                }

                // Firebase login
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(JavaStartScreen.this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    Toast.makeText(JavaStartScreen.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    CurrentUser.getInstance().setid(user.getUid());
                                    startActivity(new Intent(JavaStartScreen.this, MapsActivity.class));
                                    finish();
                                }
                            } else {
                                Toast.makeText(JavaStartScreen.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });





        // TO SIGN UP BUTTON
        toSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(JavaStartScreen.this, JavaSignUpScreen.class);
                startActivity(i);
            }
        });

    }
}

