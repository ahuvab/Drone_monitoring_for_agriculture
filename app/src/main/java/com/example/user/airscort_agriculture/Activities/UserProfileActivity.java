package com.example.user.airscort_agriculture.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.airscort_agriculture.DB.DataAccess;
import com.example.user.airscort_agriculture.R;

import java.util.ArrayList;

/*
This activity Allows the user to view his profile details- name and last name, email and his field's names
 */
public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView profile,lName, fName,email,fields;
    private Button edit;
    private ListView fieldsList;
    private ArrayList<String> array;
    private ArrayAdapter <String> adapter;
    private ActionBar actionBar;
    private DataAccess dataAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        dataAccess=new DataAccess(this);

        profile=(TextView)findViewById(R.id.title);
        lName=(TextView)findViewById(R.id.firstName);
        fName=(TextView)findViewById(R.id.lastName);
        email=(TextView)findViewById(R.id.email);
        fields=(TextView)findViewById(R.id.fields);

        fieldsList=(ListView)findViewById(R.id.listView);
        array=new ArrayList<String>(dataAccess.getNamesFields());
        adapter=new ArrayAdapter<String>(this, R.layout.simple_listview_field,R.id.fieldName, array);
        fieldsList.setAdapter(adapter);
        edit=(Button)findViewById(R.id.editProfile);
        edit.setOnClickListener(this);

        //add back arrow on toolbar
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Profile");                //set activity title
    }

    @Override
    protected void onResume() {
        super.onResume();
        lName.setText(dataAccess.getFirstName());
        fName.setText(dataAccess.getLastName());
        email.setText(dataAccess.getEmail());
    }

    public void onClick(View v) {
        if (edit.getId() == v.getId()) {                        //edit profile details
            Intent intent=new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent=new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
