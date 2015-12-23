package com.example.user.airscort_agriculture;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Savepoint;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView title;
    private EditText fNameEdit, lNameEdit, emailEdit,passwordEdit;
    private String first, last, email ,password;
    private Button save;
    private DataAccess dal;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        title=(TextView)findViewById(R.id.title);
        fNameEdit=(EditText)findViewById(R.id.editFName);
        lNameEdit=(EditText)findViewById(R.id.editLName);
        emailEdit=(EditText)findViewById(R.id.editEmail);
        passwordEdit=(EditText)findViewById(R.id.editPassword);
        save=(Button)findViewById(R.id.saveChangesButton);
        dal=new DataAccess(this);
        fNameEdit.setText(dal.getFirstName());
        lNameEdit.setText(dal.getLastName());
        emailEdit.setText(dal.getEmail());
        passwordEdit.setText(dal.getPassword());
        save.setOnClickListener(this);

        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    public void onClick(View v) {
        if (save.getId() == v.getId()) {
            first=fNameEdit.getText().toString();
            last=lNameEdit.getText().toString();
            email=emailEdit.getText().toString();
            password=passwordEdit.getText().toString();

            //TODO check the validation
            Toast.makeText(EditProfileActivity.this, getString(R.string.profile_update), Toast.LENGTH_SHORT).show();
            dal.editUser(first, last, email, password);
            //TODO Wwait and go to profile activity with new details
//            Intent intent=new Intent(this, UserProfileActivity.class);
//            startActivity(intent);

        }
    }
}
