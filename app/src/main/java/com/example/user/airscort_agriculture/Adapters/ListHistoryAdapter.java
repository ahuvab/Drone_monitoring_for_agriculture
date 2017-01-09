package com.example.user.airscort_agriculture.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.user.airscort_agriculture.DB.DataAccess;
import com.example.user.airscort_agriculture.R;

import java.util.ArrayList;

/* adaper for history list. include 2 text view each item*/
public class ListHistoryAdapter extends ArrayAdapter<String> {

    private Context con;
    private ArrayList<String> array;
    private DataAccess dataAccess;
    private boolean [] isClickedItem;


    public ListHistoryAdapter(Context context, int textViewResourceId, ArrayList<String> array) {
        super(context, textViewResourceId, array);
        this.array = new ArrayList <String>();
        this.array.addAll(array);
        con=context;
        dataAccess=new DataAccess(con);
        isClickedItem=new boolean[array.size()];
        for(int i=0; i<isClickedItem.length; i++){
            isClickedItem[i]=false;
        }
    }
    public class ViewHolder {
        TextView tv1;
        TextView tv2;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        String date="";
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.single_listview_history, parent, false);

            holder = new ViewHolder();
            holder.tv1 = (TextView) convertView.findViewById(R.id.date);
            holder.tv2 = (TextView) convertView.findViewById(R.id.fields);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(isClickedItem[position]){
            convertView.setBackgroundColor(Color.argb(90, 239, 244, 255));
        }
        else{
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }
        date=array.get(position);
        holder.tv1.setText(date);
        String fieldsStr=dataAccess.getFieldsListHistory(date).toString();
        holder.tv2.setText(fieldsStr.substring(1, fieldsStr.length() - 1));

        return convertView;
    }

    public void clickItem(int index){
        isClickedItem[index]=true;
    }

    public void cancelClickItem(int index){
        isClickedItem[index]=false;
    }

    public int getCount() {
        return array.size();
    }
}