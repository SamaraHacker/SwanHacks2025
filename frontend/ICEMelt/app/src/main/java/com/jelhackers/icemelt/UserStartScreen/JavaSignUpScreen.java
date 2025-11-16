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


                if (username.equals("admin") && password.equals("123") && email.equals("esantana@iastate.edu")) {
                    Toast.makeText(com.jelhackers.icemelt.UserStartScreen.JavaSignUpScreen.this, "Sign up success!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(com.jelhackers.icemelt.UserStartScreen.JavaSignUpScreen.this, "Sign up failed!", Toast.LENGTH_SHORT).show();
                }


            }
        });





        // TO LOGIN PAGE BUTTON
        toLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent i = new Intent(JavaSignUpScreen.this, JavaStartScreen.class);
                startActivity(i);
                // setContentView(R.layout.activity_main);

            }
        });



















    }

}

