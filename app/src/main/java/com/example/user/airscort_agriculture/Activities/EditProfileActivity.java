package com.example.user.airscort_agriculture.Activities;

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
This activity allowing to user to change his profile details-  name and last name, email or password
 */
public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText fNameEdit, lNameEdit, emailEdit,passwordEdit ,confirmPassword;
    private String first, last, email ,password ,confirmPass;
    private Button save;
    private DataAccess dataAccess;
    private ActionBar actionBar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;
    private final int MAX_INPUT_LENGTH=30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        sharedPreferences= getSharedPreferences(getString(R.string.user_details), MODE_PRIVATE);
        spEditor=sharedPreferences.edit();
        fNameEdit=(EditText)findViewById(R.id.editFName);
        lNameEdit=(EditText)findViewById(R.id.editLName);
        emailEdit=(EditText)findViewById(R.id.editEmail);
        passwordEdit=(EditText)findViewById(R.id.editPassword);
        confirmPassword=(EditText)findViewById(R.id.confirmeditPassword);
        save=(Button)findViewById(R.id.saveChangesButton);
        dataAccess=new DataAccess(this);

        fNameEdit.setText(dataAccess.getFirstName());
        lNameEdit.setText(dataAccess.getLastName());
        emailEdit.setText(dataAccess.getEmail());
        passwordEdit.setText(dataAccess.getPassword());
        confirmPassword.setText(dataAccess.getPassword());
        save.setOnClickListener(this);

        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Edit profile");              //set activity title

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);     //keyboard not appear as default
    }

    public void onClick(View v) {
        if (save.getId() == v.getId()) {               //save changes
            first=fNameEdit.getText().toString();
            last=lNameEdit.getText().toString();
            email=emailEdit.getText().toString();
            password=passwordEdit.getText().toString();
            confirmPass=confirmPassword.getText().toString();
            if(first.length()>MAX_INPUT_LENGTH || last.length()>MAX_INPUT_LENGTH || email.length()>MAX_INPUT_LENGTH
                    || password.length()>MAX_INPUT_LENGTH ){
                Toast.makeText(EditProfileActivity.this, getString(R.string.long_name), Toast.LENGTH_LONG).show();
            }
            else if(first.equals("")|| last.equals("")||email.equals("")|| password.equals("")||confirmPass.equals("")){
                Toast.makeText(EditProfileActivity.this, getString(R.string.fill_fields), Toast.LENGTH_LONG).show();
            }
            else if(!password.equals(confirmPass)){
                Toast.makeText(EditProfileActivity.this, getString(R.string.password_not_confirm), Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(EditProfileActivity.this, getString(R.string.profile_update), Toast.LENGTH_LONG).show();
                dataAccess.editUser(first, last, email, password);
                onBackPressed();
            }
        }
    }
}
