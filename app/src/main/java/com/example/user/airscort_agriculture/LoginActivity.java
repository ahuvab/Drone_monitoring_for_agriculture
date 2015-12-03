package com.example.user.airscort_agriculture;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    DataAccess dal;
    private EditText emailInput,passwordInput;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput=(EditText)findViewById(R.id.editEmailLog);
        passwordInput=(EditText)findViewById(R.id.editPasswordLog);
        login=(Button)findViewById(R.id.button2);
        login.setOnClickListener(this);
        dal=new DataAccess(this);
    }

    public void onClick(View v) {
        String email, password;
        email=emailInput.getText().toString();
        password=passwordInput.getText().toString();

        if(email.equals("")){
            emailInput.setHint(getString(R.string.fill_field));
        }
        if(password.equals("")){
            passwordInput.setHint(getString(R.string.fill_field));
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput.getText().toString()).matches()){
            emailInput.setText("");
            emailInput.setHint(getString(R.string.email_validate));
        }
        else if(!email.equals("")&& !password.equals("")){

            if(!dal.existUser(email, password)){     //error massage
                Toast.makeText(this, getString(R.string.error_login), Toast.LENGTH_LONG).show();
                emailInput.setText("");
                passwordInput.setText("");
                emailInput.setHint(getString(R.string.email));
                passwordInput.setHint(getString(R.string.password));
            }
            else{      //go to next activity- to scan or to map
                Toast.makeText(this, "move to fields activity", Toast.LENGTH_LONG).show();
            }
        }
    }


}