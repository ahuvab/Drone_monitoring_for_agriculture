package com.example.user.airscort_agriculture.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.airscort_agriculture.DB.DataAccess;
import com.example.user.airscort_agriculture.Fragments.HomeFragment;
import com.example.user.airscort_agriculture.Fragments.NoFieldsHomeFragment;
import com.example.user.airscort_agriculture.R;

import java.util.ArrayList;

/*
This activity is home page of this app.
the user can choose fields to scan and start scanning.
 */
public class HomeActivity extends AppCompatActivity  {

    private String firstNameUser;
    private TextView hello;
    private DataAccess dataAccess;
    private FrameLayout frameLayout;
    private HomeFragment homeFragment;
    private NoFieldsHomeFragment noFieldsHomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        dataAccess = new DataAccess(this);
        hello = (TextView) findViewById(R.id.helloText);
        firstNameUser = dataAccess.getFirstName();
        hello.setText("Hello " + firstNameUser + ",");

        if(dataAccess.getNamesFields().size()>0){
            homeFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().
                    replace(frameLayout.getId(), homeFragment, "HOME").commit();
        }
        else{
            noFieldsHomeFragment = new NoFieldsHomeFragment();
            getSupportFragmentManager().beginTransaction().
                    replace(frameLayout.getId(), noFieldsHomeFragment, "NO_FIELDS_HOME").commit();
        }

        //set tool bar title
        getSupportActionBar().setTitle("Fields");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //overflow for more options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.profile:                                     //View Profile
                Intent intent=new Intent(this, UserProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.history:                                    //view  scanning history
                ArrayList dateArray=dataAccess.getHistoryDates();
                if(dateArray.size()==0){
                    Toast.makeText(HomeActivity.this, getString(R.string.no_history), Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent2 = new Intent(this, ScanningHistoryActivity.class);
                    startActivity(intent2);
                }
                break;
            case R.id.add_fields:
                Intent intent3 = new Intent(this, MapActivity.class);
                startActivity(intent3);
                break;
            case R.id.show_fields:                                 //view fields
                if(dataAccess.getNamesFields().size()>0) {          //if the user-defined fields
                    Intent intent4 = new Intent(this, ShowFieldsActivity.class);
                    startActivity(intent4);
                }
                else{
                    Toast.makeText(HomeActivity.this, getString(R.string.no_fields), Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.current:                                     //follow after current scanning
                String date=dataAccess.getDateFromScanning();
                if(date.equals("")){
                    Toast.makeText(HomeActivity.this, getString(R.string.no_current_scanning), Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent5 = new Intent(this, CurrentMissionActivity.class);
                    startActivity(intent5);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){}


}
