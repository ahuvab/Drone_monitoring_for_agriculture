package com.example.user.airscort_agriculture.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.user.airscort_agriculture.Interfaces.FieldsToScan;
import com.example.user.airscort_agriculture.R;

import java.util.ArrayList;

/* adaper for fields list. include text view and checkbox each item*/
public class ListFieldsAdapter extends ArrayAdapter<String> {
    private Context con;
    private ArrayList<String> array;
    private FieldsToScan mFieldsToScan;


    public ListFieldsAdapter(Context context, int textViewResourceId, ArrayList<String> array) {
        super(context, textViewResourceId, array);
        this.array = new ArrayList<String>();
        this.array.addAll(array);
        con=context;
    }

    private class ViewHolder {
        TextView fieldName;
        CheckBox choose;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.single_listview_field_checkbox, parent,false);

            holder = new ViewHolder();
            holder.fieldName = (TextView) convertView.findViewById(R.id.fieldName);
            holder.choose = (CheckBox) convertView.findViewById(R.id.checkBox);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.fieldName.setText(array.get(position));

        if(mFieldsToScan.ifContainField(array.get(position))){
            holder.choose.setChecked(true);
        }
        else{
            holder.choose.setChecked(false);
        }

        holder.choose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(!mFieldsToScan.ifContainField(holder.fieldName.getText().toString())) {
                        mFieldsToScan.addField(holder.fieldName.getText().toString());
                    }}
                else {
                    mFieldsToScan.deleteField(holder.fieldName.getText().toString());
                }
            }
        });
        return convertView;
    }

    public void setFieldsToScan( FieldsToScan r ) {
        mFieldsToScan=r;
    }
}
