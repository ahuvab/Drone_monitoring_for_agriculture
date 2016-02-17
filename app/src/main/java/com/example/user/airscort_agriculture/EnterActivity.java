package com.example.user.airscort_agriculture;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EnterActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView logo;
    private EditText enterEmail, enterPassword;
    private Button log;
    private TextView notMember, register, forgetPass;
    private DataAccess dal;
    private String email, password;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        logo=(ImageView)findViewById(R.id.ImageLogo);
        enterEmail=(EditText)findViewById(R.id.editEmailLog);
        enterPassword=(EditText)findViewById(R.id.editPasswordLog);
        log = (Button) findViewById(R.id.buttonLog);
        notMember=(TextView)findViewById(R.id.textViewNot);
        register=(TextView)findViewById(R.id.textViewReg);
        forgetPass=(TextView)findViewById(R.id.forgetPass);
        register.setOnClickListener(this);
        forgetPass.setOnClickListener(this);
        log.setOnClickListener(this);
        dal=new DataAccess(this);
    }

    public void onClick(View v) {
        if (register.getId() == v.getId()){
            Intent intent=new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
        if (log.getId() == v.getId()){
            //validate the fields
            email=enterEmail.getText().toString();
            password=enterPassword.getText().toString();
            if(email.equals("")){
                enterEmail.setHintTextColor(getResources().getColor(R.color.red));
                enterEmail.setHint(getString(R.string.email));
            }
            else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                enterEmail.setText("");
                enterEmail.setHintTextColor(getResources().getColor(R.color.red));
                enterEmail.setHint(getString(R.string.email_validate));
            }
            if(password.equals("")){
                enterPassword.setHintTextColor(getResources().getColor(R.color.red));
                enterPassword.setHint(getString(R.string.password));
            }
//            else if(!email.equals("") && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
//                && !email.equals("")){

                if(dal.existUser(email,password)) {
                    Intent intent=new Intent(this, ChooseFieldsToScanActivity.class);
                    intent.putExtra(getString(R.string.email), email);
                    startActivity(intent);
                }
                else{     //the user is not exist or the details are incorrect
                    Toast.makeText(this, getText(R.string.error_login), Toast.LENGTH_LONG).show();
                }
//            }
        }
        if (forgetPass.getId() == v.getId()){

        }
    }
}
