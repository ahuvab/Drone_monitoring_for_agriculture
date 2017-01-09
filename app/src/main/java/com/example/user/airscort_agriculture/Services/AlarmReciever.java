package com.example.user.airscort_agriculture.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context,Intent intent){
        Intent service=new Intent(context, MyIntentService.class);
        context.startService(service);
    }

    public AlarmReciever(){}

    public void setAlarm(Context context, Long timeInMills) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmReciever.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMills, pi);
    }


    public void cancelAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent=new Intent(context,AlarmReciever.class);
        PendingIntent sender=PendingIntent.getBroadcast(context,0,intent,0);
        alarmManager.cancel(sender);
    }

}
