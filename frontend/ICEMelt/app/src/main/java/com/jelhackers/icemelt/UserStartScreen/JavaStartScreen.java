package com.jelhackers.icemelt.UserStartScreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.jelhackers.icemelt.R;

public class JavaStartScreen extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private Button loginBtn, toSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // LOGIN SCREEN
        setContentView(R.layout.activity_main);


        // For retrieving strings for login

        // 1. Connect java variables to XML views
        // On login page
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        loginBtn = findViewById(R.id.login_btn);
        // Takes to the sign in screen
        toSignUp = findViewById(R.id.to_signup_btn);




        // Handle button clicks
        // LOGIN BUTTON
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();

                // Now we can use the variables


                // TESTING: Works


                Log.d("LOGIN", "Username: " + username);
                Log.d("LOGIN", "Password" + password);


                if (username.equals("admin") && password.equals("123")) {
                    Toast.makeText(com.jelhackers.icemelt.UserStartScreen.JavaStartScreen.this, "Login success!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(com.jelhackers.icemelt.UserStartScreen.JavaStartScreen.this, "Login failed!", Toast.LENGTH_SHORT).show();
                }

            }
        });





        // TO SIGN UP BUTTON
        toSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(JavaStartScreen.this, JavaSignUpScreen.class);
                startActivity(i);

                // setContentView(R.layout.activity_sign_up);

            }
        });

    }
}

