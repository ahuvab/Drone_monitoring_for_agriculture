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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_mission);

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
                        drawPolygons();                                          //draw fields on map
                        map.addDroneMarker(new LatLng(32.578613, 35.266115));      //draw drone on map
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


    public void drawPolygons() {

        PolygonOptions rectOptions = new PolygonOptions()
                .add(new LatLng(32.576077, 35.261569),
                        new LatLng(32.574825, 35.267523),
                        new LatLng(32.572840, 35.266841),
                        new LatLng(32.574093, 35.261014),
                        new LatLng(32.576077, 35.261569));

        map.draw(rectOptions);

        rectOptions = new PolygonOptions()
                .add(new LatLng(32.583623, 35.266114),
                        new LatLng(32.583472, 35.270545),
                        new LatLng(32.580863, 35.272182),
                        new LatLng(32.579840, 35.274427),
                        new LatLng(32.576977, 35.273881),
                        new LatLng(32.578613, 35.266115),
                        new LatLng(32.583623, 35.266114));
        map.draw(rectOptions);

        rectOptions = new PolygonOptions()
                .add(new LatLng(32.584769, 35.257547),
                        new LatLng(32.583798, 35.262099),
                        new LatLng(32.583696, 35.263677),
                        new LatLng(32.581137, 35.263799),
                        new LatLng(32.582264, 35.258336),
                        new LatLng(32.583133, 35.258094),
                        new LatLng(32.583491, 35.257729),
                        new LatLng(32.584769, 35.257547));
        map.draw(rectOptions);

    }
}
