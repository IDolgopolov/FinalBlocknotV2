package com.arproject.finalblocknot;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class MyAlarmManager extends BroadcastReceiver {

    @Override
    @SuppressLint("NewAPi")
    public void onReceive(Context context, Intent intent) {
        Log.i("notification", "onReceive");
        Intent intentActivity = new Intent(context, MainActivity.class);
        intentActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, MainActivity.ALARM_RTC, intentActivity, PendingIntent.FLAG_UPDATE_CURRENT);

        String idChannel = "channel_1";
        Notification notification = new Notification.Builder(context)
                .setContentTitle(context.getResources().getString(R.string.today) + ": ")
                .setSmallIcon(R.mipmap.icon_lounch_round)
                .setStyle(new Notification.BigTextStyle()
                        .bigText(MainActivity.getTodayEE(getTodayDate(), context)))
                .setContentIntent(pendingIntent)
                .setChannelId(idChannel)
                .build();
        NotificationManager notManager = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));

        if(Build.VERSION.SDK_INT > 24) {
            NotificationChannel channel = new NotificationChannel(idChannel, "events_notification", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("channel_notify_events");
            channel.enableVibration(false);
            notManager.createNotificationChannel(channel);
        }

        notManager.notify(MainActivity.ALARM_RTC, notification);


    }

    private String getTodayDate() {
        Calendar calendar = new GregorianCalendar();
        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; //отсчет месяцев идет с 0
        int currentYear = calendar.get(Calendar.YEAR);
        DateFormat df = new SimpleDateFormat("EEE");
        String dayOfWeek = df.format(Calendar.getInstance().getTime());
        String dateToday =  dayOfWeek + ", " + Integer.toString(currentDayOfMonth)+  "." + Integer.toString(currentMonth)
                + "." + Integer.toString(currentYear);
        return dateToday;
    }
}
