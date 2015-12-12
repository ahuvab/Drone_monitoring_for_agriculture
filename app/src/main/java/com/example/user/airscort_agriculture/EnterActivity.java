package com.example.user.airscort_agriculture;

import android.content.Intent;
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
    private TextView notMember, register;
    private DataAccess dal;



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
        register.setOnClickListener(this);
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
            if(enterEmail.getText().toString().equals("")){
                enterEmail.setHint(getString(R.string.fill_field));
            }
            else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(enterEmail.getText().toString()).matches()){
                enterEmail.setText("");
                enterEmail.setHint(getString(R.string.email_validate));
            }
            if(enterPassword.getText().toString().equals("")){
                enterPassword.setHint(getString(R.string.fill_field));
            }
            else if(!enterEmail.getText().toString().equals("") && android.util.Patterns.EMAIL_ADDRESS.matcher(enterEmail.getText().toString()).matches()
                && !enterPassword.getText().toString().equals("")){

                if(dal.existUser(enterEmail.getText().toString(),enterPassword.getText().toString())) {
                    Toast.makeText(this, "go to field activity", Toast.LENGTH_LONG).show();
                }
                else{     //the user is not exist or the details are incorrect
                    Toast.makeText(this, getText(R.string.error_login), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
