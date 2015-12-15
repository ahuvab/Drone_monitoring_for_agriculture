package com.example.user.airscort_agriculture;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private DataAccess dal;
    private EditText fNameInput,lNameInput, emailInput ,passwordInput,conPassword;
    private Button regis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fNameInput=(EditText)findViewById(R.id.editFNameReg);
        lNameInput=(EditText)findViewById(R.id.editLNameReg);
        emailInput=(EditText)findViewById(R.id.editEmailReg);
        passwordInput=(EditText)findViewById(R.id.editPasswordReg);
        conPassword=(EditText)findViewById(R.id.editConPasswordReg);
        regis=(Button)findViewById(R.id.registerbutton);
        regis.setOnClickListener(this);
        dal=new DataAccess(this);
    }

    public void onClick(View v) {
        String firstName, lastName, email, password;
        firstName=fNameInput.getText().toString();
        lastName=lNameInput.getText().toString();
        email=emailInput.getText().toString();
        password=passwordInput.getText().toString();

//        if(firstName.equals("")){
//            fNameInput.setHint(getString(R.string.fill_field));
//        }
//        if(lastName.equals("")){
//            lNameInput.setHint(getString(R.string.fill_field));
//        }
//        if(email.equals("")){
//            emailInput.setHint(getString(R.string.fill_field));
//        }
//        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){    //check the email validate
//            emailInput.setText("");
//            emailInput.setHint(getString(R.string.email_validate));
//        }
//        if(password.equals("")){
//            passwordInput.setHint(getString(R.string.fill_field));
//        }
//        else if(password.length()<6){
//            passwordInput.setText("");
//            passwordInput.setHint(getString(R.string.password_length));
//        }
//        if(conPassword.getText().toString().equals("")){
//            conPassword.setHint(getString(R.string.fill_field));
//        }
//        if(!password.equals(conPassword.getText().toString())){
//            conPassword.setText("");
//            conPassword.setHint(getString(R.string.password_not_confirm));
//        }
//        else if(!firstName.equals("") && !lastName.equals("") &&
//                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && !password.equals("") && password.length()>=6 &&
//                 password.equals(conPassword.getText().toString())){

            if(dal.existEmail(email)){            //email or password already exist
                Toast.makeText(this, getString(R.string.error_register), Toast.LENGTH_LONG).show();
            }
            else{      //add user and go to choose fields activity
                if(!dal.addUser(firstName,lastName, email, password)){
                    Toast.makeText(this,  getString(R.string.can_not_register), Toast.LENGTH_LONG).show();
                }
                Intent intent=new Intent(this, MapActivity.class);
                startActivity(intent);
            }
//        }
    }


}
