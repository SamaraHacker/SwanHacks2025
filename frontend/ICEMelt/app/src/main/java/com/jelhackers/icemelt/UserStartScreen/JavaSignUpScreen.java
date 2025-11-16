package com.jelhackers.icemelt.UserStartScreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.jelhackers.icemelt.backend.User;
import com.jelhackers.icemelt.backend.UserController;
import androidx.appcompat.app.AppCompatActivity;



import com.jelhackers.icemelt.R;

public class JavaSignUpScreen extends AppCompatActivity {

    private EditText emailSignupInput, passwordSignupInput, usernameSignupInput;
    private Button signUpBtn, toLogin;

    UserController uc = new UserController();

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
                try{
                    System.out.println(uc.addUser(username, password, email).toString());
                }catch(Error e){
                    throw e;
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

