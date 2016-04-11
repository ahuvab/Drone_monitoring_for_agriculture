package com.example.user.airscort_agriculture;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.airscort_agriculture.DB.DataAccess;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListHistoryAdapter extends ArrayAdapter<String> {

    private Context con;
    private ArrayList<String> array;
    private DataAccess dataAccess;


    public ListHistoryAdapter(Context context, int textViewResourceId, ArrayList<String> array) {
        super(context, textViewResourceId, array);
        this.array = new ArrayList<String>();
        this.array.addAll(array);
        con=context;
        dataAccess=new DataAccess(con);
    }


    public class ViewHolder
    {
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
        date=array.get(position);
        holder.tv1.setText(date);
        String fieldsStr=dataAccess.getFieldsListHistory(date).toString();
        holder.tv2.setText(fieldsStr.substring(1,fieldsStr.length()-1));

        return convertView;
    }

    public int getCount() {
        return array.size();
    }
}