package com.jelhackers.icemelt;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    private EditText usernameInput, emailInput, passwordInput;
    private Button save, toMap;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        setContentView(R.layout.settings);

        usernameInput = findViewById(R.id.edit_username);
        emailInput = findViewById(R.id.edit_email);
        passwordInput = findViewById(R.id.edit_password);

        save = findViewById(R.id.btn_save_settings);
        toMap = findViewById(R.id.toMap);

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String NewUsername = usernameInput.getText().toString();
                String NewPassword = passwordInput.getText().toString();
                String NewEmail = emailInput.getText().toString();


                // TODO
                if (!NewUsername.isEmpty()){
                    // Update username
                }
                if (!NewPassword.isEmpty()){
                    // update password
                }
                if (!NewEmail.isEmpty()){
                    // update email

                }


            }
        });

        toMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // TODO
                // Return to map

            }
        });



    }

}
