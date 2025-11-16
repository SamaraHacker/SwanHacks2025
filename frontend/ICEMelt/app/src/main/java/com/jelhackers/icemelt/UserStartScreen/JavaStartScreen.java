package com.jelhackers.icemelt.UserStartScreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.jelhackers.icemelt.backend.User;
import com.jelhackers.icemelt.backend.UserController;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.jelhackers.icemelt.R;

public class JavaStartScreen extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private Button loginBtn, toSignUp;
    UserController uc = new UserController();

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
                ApiFuture<QuerySnapshot> future = db.collection("User").get();
                try{
                    QuerySnapshot querySnapshot = future.get();
                    List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
                    for (QueryDocumentSnapshot doc : documents) {
                        User dUser = doc.getData().get(doc.getId())
                        if(username == dUser.getUsername()){
                            if(password == dUser.getPassword()){
                                //access map screen
                                Log.d(TAG, "Login successful");
                            }
                            Log.d(TAG, "Password incorrect");
                        }
                        Log.d(TAG, "User not found");
                    }
                }catch(Error e){
                    throw e;
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

