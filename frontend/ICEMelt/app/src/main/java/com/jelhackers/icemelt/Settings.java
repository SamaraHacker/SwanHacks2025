package com.jelhackers.icemelt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.jelhackers.icemelt.UserStartScreen.JavaSignUpScreen;
import com.jelhackers.icemelt.UserStartScreen.JavaStartScreen;
import com.jelhackers.icemelt.Users.CurrentUser;
import com.jelhackers.icemelt.Users.UserController;

public class Settings extends AppCompatActivity {

    private EditText usernameInput, emailInput, passwordInput;
    private Button save, toMap;

    private CurrentUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        currentUser = CurrentUser.getInstance();
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

                UserController controller = new UserController();
                // TODO
                if (!NewUsername.isEmpty()){
                    // Update username
                    controller.updateUser(currentUser.getid(), NewUsername, currentUser.getPassword(), currentUser.getEmail());
                }
                if (!NewPassword.isEmpty()){
                    // update password
                    controller.updateUser(currentUser.getid(), currentUser.getUsername(), NewPassword, currentUser.getEmail());
                }
                if (!NewEmail.isEmpty()){
                    // update email
                    controller.updateUser(currentUser.getid(), currentUser.getUsername(), currentUser.getPassword(), NewEmail);

                }
                Toast.makeText(Settings.this, "Account updated successfully", Toast.LENGTH_SHORT).show();

            }
        });

        toMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(Settings.this, MapsActivity.class);
                startActivity(i);
            }
        });



    }

}
