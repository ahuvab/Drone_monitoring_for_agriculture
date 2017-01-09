package com.example.user.airscort_agriculture.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.user.airscort_agriculture.DB.DataAccess;

/* To send a notification if you scan completed successfully even if the device was turned off */
public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DataAccess dataAccess=new DataAccess(context);
        long time=dataAccess.getScanningTime();
        if(time!=0){                                        //if there is current scanning
            if(System.currentTimeMillis()>time){            //If the time has passed, send notification about finish scanning
                Intent service=new Intent(context, MyIntentService.class);
                context.startService(service);
            }
            else{                                          //else, set alarm receiver
                AlarmReciever alarmReciever=new AlarmReciever();
                alarmReciever.setAlarm(context,time);
            }
        }

    }
}
