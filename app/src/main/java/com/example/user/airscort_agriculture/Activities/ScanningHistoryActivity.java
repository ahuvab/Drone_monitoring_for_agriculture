package com.example.user.airscort_agriculture.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.airscort_agriculture.DB.DataAccess;
import com.example.user.airscort_agriculture.Adapters.ListHistoryAdapter;
import com.example.user.airscort_agriculture.R;

import java.util.ArrayList;

/*
This activity allows to user to view his scanning history: date and the scanned fields
 */
public class ScanningHistoryActivity extends AppCompatActivity {

    private ListView historyList;
    private TextView title;
    private ArrayList<String> dateArray;
    private ListHistoryAdapter adapter;
    private ActionBar actionBar;
    private DataAccess dataAccess;
    private ArrayList<Integer> chosenItem;      //chosen items to delete
    private boolean lastItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning_history);
        historyList=(ListView)findViewById(R.id.listView);
        title=(TextView)findViewById(R.id.title);
        dataAccess=new DataAccess(this);
        dateArray=new ArrayList<String>(dataAccess.getHistoryDates());
        adapter=new ListHistoryAdapter(this, R.layout.single_listview_history, dateArray);
        historyList.setAdapter(adapter);
        historyList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lastItem=false;
        chosenItem=new ArrayList<>();

        //check if there is a bundle
        if (savedInstanceState != null) {
            chosenItem.addAll(savedInstanceState.getIntegerArrayList("chosenItemsList"));
            for(int i=0; i<chosenItem.size();i++){
                adapter.clickItem(chosenItem.get(i));
            }
            adapter.notifyDataSetChanged();
        }

        //mark selected item and the user can delete the marked items
        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                int i;
                lastItem=false;
                for(i=0; i<chosenItem.size();i++) {
                    if (chosenItem.get(i)==position) {         // if the item has been clicked
                       adapter.cancelClickItem(position);      //cancel and remove from chosen items list
                        if(i==chosenItem.size()-1){
                            lastItem=true;
                        }
                        chosenItem.remove(i);
                        break;
                    }
                }
                if(i==chosenItem.size()&& !lastItem){        //add to chosen items list
                    adapter.clickItem(position);
                    chosenItem.add(position);
                }
                adapter.notifyDataSetChanged();              //update the view of the list
            }
        });

        //add back arrow on toolbar
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("History");              //set activity title
    }

    //delete history
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_history, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.delete:
                String message;
                if(!chosenItem.isEmpty()){       //delete selected item
                   message= getString(R.string.delete_item_from_history);
                }
                else{                         //delete all history
                    message= getString(R.string.delete_history);
                }
                alertForDelete(message);     //dialog to confirm deleting
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void alertForDelete(String message){
        new AlertDialog.Builder(this)      //to confirm the delete
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("delete history")
                .setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!chosenItem.isEmpty()) {                //delete selected item
                            for (int i = 0; i < chosenItem.size(); i++) {
                                int index=chosenItem.get(i);
                                dataAccess.deleteHistory(dateArray.get(index), dataAccess.getFieldsListHistory(dateArray.get(index)));
                                finish();
                                startActivity(getIntent());
                            }

                        } else {                                    //delete all messages
                            dataAccess.deleteAllHistory();
                            finish();
                            Intent intent = new Intent(ScanningHistoryActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onBackPressed(){
        Intent intent=new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList("chosenItemsList", chosenItem);
    }
}
