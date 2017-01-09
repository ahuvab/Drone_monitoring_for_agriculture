package com.example.user.airscort_agriculture.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.airscort_agriculture.DB.DataAccess;
import com.example.user.airscort_agriculture.R;

/*
This activity is an enter page in this app.
If the user is registered, he can enter by his email and password. (if not, he move to register)
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView logo;
    private EditText enterEmail, enterPassword;
    private TextView notMember, forgetPass;
    private Button log,register;
    private String email, password;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;
    private DataAccess dataAccess;
    private final int MAX_INPUT_LENGTH=40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        sharedPreferences= getSharedPreferences(getString(R.string.user_details), MODE_PRIVATE);
        spEditor=sharedPreferences.edit();

        dataAccess=new DataAccess(this);
        logo=(ImageView)findViewById(R.id.ImageLogo);
        enterEmail=(EditText)findViewById(R.id.editEmailLog);
        enterPassword=(EditText)findViewById(R.id.editPasswordLog);
        log = (Button) findViewById(R.id.buttonLog);
        notMember=(TextView)findViewById(R.id.textViewNot);
        register=(Button)findViewById(R.id.textViewReg);
        forgetPass=(TextView)findViewById(R.id.forgetPass);
        register.setOnClickListener(this);
        forgetPass.setOnClickListener(this);
        log.setOnClickListener(this);

        enterEmail.setText(dataAccess.getEmail());
        enterPassword.setText(dataAccess.getPassword());

        //set tool bar
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);     //keyboard not appear as default
    }

    public void onClick(View v) {
        if (register.getId() == v.getId()){
            Intent intent=new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
        if (log.getId() == v.getId()){
            validateFields();
        }
        if (forgetPass.getId() == v.getId()){
            //TODO: implement
        }
    }

    /* Checks and verifies the correct input */
    public boolean validateFields(){
        email=enterEmail.getText().toString();
        password=enterPassword.getText().toString();
        if(email.length()>MAX_INPUT_LENGTH || password.length()>MAX_INPUT_LENGTH){
            Toast.makeText(LoginActivity.this,getString(R.string.long_name), Toast.LENGTH_LONG).show();
        }
        else {
            if (email.equals("")) {
                enterEmail.setHintTextColor(getResources().getColor(R.color.red));
                enterEmail.setHint(getString(R.string.email));
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                enterEmail.setText("");
                enterEmail.setHintTextColor(getResources().getColor(R.color.red));
                enterEmail.setHint(getString(R.string.email_validate));
            }
            if (password.equals("")) {
                enterPassword.setHintTextColor(getResources().getColor(R.color.red));
                enterPassword.setHint(getString(R.string.password));
            }
            else if(!email.equals("") && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    && !email.equals("")){
                if(dataAccess.login(email,password)) {
                    login();
                    return true;
                }
                else{
                    Toast.makeText(LoginActivity.this, getString(R.string.error_login), Toast.LENGTH_LONG).show();
                }
            }
        }
        return false;
    }

    /* login and move to appropriate page */
    public void login(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

}
