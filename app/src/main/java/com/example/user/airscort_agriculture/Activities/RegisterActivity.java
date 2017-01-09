package com.example.user.airscort_agriculture.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.airscort_agriculture.DB.DataAccess;
import com.example.user.airscort_agriculture.R;

/*
This activity is for first regisration
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText fNameInput,lNameInput, emailInput ,passwordInput,conPassword, stationId;
    private Button regis;
    private DataAccess dataAccess;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;
    private ActionBar actionBar;
    private final int MAX_INPUT_LENGTH=40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sharedPreferences= getSharedPreferences(getString(R.string.user_details), MODE_PRIVATE);
        spEditor=sharedPreferences.edit();
        dataAccess=new DataAccess(this);
        fNameInput=(EditText)findViewById(R.id.editFNameReg);
        lNameInput=(EditText)findViewById(R.id.editLNameReg);
        emailInput=(EditText)findViewById(R.id.editEmailReg);
        passwordInput=(EditText)findViewById(R.id.editPasswordReg);
        conPassword=(EditText)findViewById(R.id.editConPasswordReg);
        stationId=(EditText)findViewById(R.id.stationId);
        regis=(Button)findViewById(R.id.registerbutton);
        regis.setOnClickListener(this);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Registration");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);     //keyboard not appear as default
    }

    public void onClick(View v) {
        String firstName, lastName, email, password, station;
        firstName=fNameInput.getText().toString();
        lastName=lNameInput.getText().toString();
        email=emailInput.getText().toString();
        password=passwordInput.getText().toString();
        station=stationId.getText().toString();

        //check the input's length
        if(firstName.length()>MAX_INPUT_LENGTH || lastName.length()>MAX_INPUT_LENGTH || email.length()>MAX_INPUT_LENGTH ||
                password.length()>MAX_INPUT_LENGTH || station.length()>MAX_INPUT_LENGTH ){
            Toast.makeText(RegisterActivity.this,getString(R.string.long_name), Toast.LENGTH_LONG).show();
        }
        else {
            if (firstName.equals("")) {
                fNameInput.setHintTextColor(getResources().getColor(R.color.red));
                fNameInput.setHint(getString(R.string.f_name));
            }
            if (lastName.equals("")) {
                lNameInput.setHintTextColor(getResources().getColor(R.color.red));
                lNameInput.setHint(getString(R.string.l_name));
            }
            if (email.equals("")) {
                emailInput.setHintTextColor(getResources().getColor(R.color.red));
                emailInput.setHint(getString(R.string.email));
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {    //check the email validate
                emailInput.setText("");
                emailInput.setHintTextColor(getResources().getColor(R.color.red));
                emailInput.setHint(getString(R.string.email_validate));
            }
            if (password.equals("")) {
                passwordInput.setHintTextColor(getResources().getColor(R.color.red));
                passwordInput.setHint(getString(R.string.password));
            } else if (password.length() < 6) {
                passwordInput.setText("");
                passwordInput.setHintTextColor(getResources().getColor(R.color.red));
                passwordInput.setHint(getString(R.string.password_length));
            }
            if (conPassword.getText().toString().equals("")) {
                conPassword.setHintTextColor(getResources().getColor(R.color.red));
                conPassword.setHint(getString(R.string.confirm_password));
            }
            if (!password.equals(conPassword.getText().toString())) {
                conPassword.setText("");
                conPassword.setHintTextColor(getResources().getColor(R.color.red));
                conPassword.setHint(getString(R.string.password_not_confirm));
            }
            if (station.equals("")) {
                stationId.setHintTextColor(getResources().getColor(R.color.red));
                stationId.setHint(getString(R.string.email));
            }
        else if(!firstName.equals("") && !lastName.equals("") &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && !password.equals("") && password.length()>=6 &&
                 password.equals(conPassword.getText().toString()) && !station.equals("")){

            if (dataAccess.existEmail(email)) {            //email already exist
                emailInput.setHintTextColor(getResources().getColor(R.color.red));
                Toast.makeText(this, getString(R.string.error_register), Toast.LENGTH_LONG).show();

            }
            //TODO: check station id
            else {
                dataAccess.addUser(firstName, lastName, email, password, station);      //add user
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
            }
        }
        }
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
