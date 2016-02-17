package com.example.user.airscort_agriculture;

import android.app.AlertDialog;
import android.support.v4.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class CurrentMissionActivity extends FragmentActivity implements View.OnClickListener {
    private MapFragment map;
    private FrameLayout frameLayout;
    private ImageButton stop;
    private DataAccess dal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_mission);
        dal=new DataAccess(this);
        stop = (ImageButton) findViewById(R.id.imageButton);
        stop.setOnClickListener(this);

        frameLayout = (FrameLayout) findViewById(R.id.current_mission_map);

        map = new MapFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(frameLayout.getId(), map, "MAP_FRAGMENT_CURRENT_MISSION")
                .commit();

        Thread thread = new Thread() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        while (!map.getFinished()) {
                        }
                                                              //draw fields on map
                        map.addHomeMarker(dal.getHomePoint());      //draw drone on map
                    }
                });

            }

        };
        thread.start();
    }


    @Override
    public void onClick(View v) {
        if (stop.getId() == v.getId()) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("stop scanning")
                    .setMessage("Are you sure you want to stop this scanning?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //stop scanning
                            finish();
                            Intent intent = new Intent(CurrentMissionActivity.this, ChooseFieldsToScanActivity.class);
                            startActivity(intent);
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

}
