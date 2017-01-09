package com.example.user.airscort_agriculture.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.user.airscort_agriculture.Activities.MapActivity;
import com.example.user.airscort_agriculture.R;

/* Fragment that appear  if the user has no fields */
public class NoFieldsHomeFragment extends Fragment {

    private Button defineFields;

    public void NoFieldsHomeFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View v= inflater.inflate(R.layout.fragment_no_fields_home, container, false);
        defineFields=(Button)v.findViewById(R.id.defineFields);

        defineFields.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), MapActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }

}
