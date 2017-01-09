package com.example.user.airscort_agriculture.Services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.user.airscort_agriculture.DB.DataAccess;
import com.example.user.airscort_agriculture.R;

public class MyIntentService extends IntentService {

    private DataAccess dataAccess;

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        dataAccess=new DataAccess(MyIntentService.this);
        dataAccess.deleteScanningTime();     //delete time from db
        //send notification
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setColor(0x107704);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setSmallIcon(R.mipmap.logo_image);
        builder.setContentTitle("Fields");
        builder.setContentText("The scanning completed successfully");
        int id = 0;
        nm.notify(id, builder.build());
        dataAccess.finishScanning();         //finish scanning
    }
}