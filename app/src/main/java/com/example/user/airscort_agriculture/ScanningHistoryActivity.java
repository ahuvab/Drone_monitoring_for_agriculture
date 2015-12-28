package com.example.user.airscort_agriculture;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class ScanningHistoryActivity extends AppCompatActivity {

    private ListView historyList;
    private TextView title;
    private ArrayList<String> dateArray;
//    private ArrayList<String> fieldsArray;
    private ListHistoryAdapter adapter;
    private DataAccess dal;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning_history);
        historyList=(ListView)findViewById(R.id.listView);
        title=(TextView)findViewById(R.id.title);
        dal=new DataAccess(this);
        dateArray=new ArrayList<String>(dal.getHistoryDates());
//        fieldsArray=new ArrayList<String>(dal.getHistoryFields());
        adapter=new ListHistoryAdapter(this, R.layout.single_listview_history, dateArray);
        historyList.setAdapter(adapter);

        //add back arrow on toolbar
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
