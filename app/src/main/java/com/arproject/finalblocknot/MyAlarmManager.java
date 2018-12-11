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
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class MyAlarmManager extends BroadcastReceiver {
    private final String idChannel = "CHANEL_1";
    private final String nameChannel = "events_notification";

    @Override
    @SuppressLint("NewAPi")
    public void onReceive(Context context, Intent intent) {
        Intent intentActivity = new Intent(context, MainActivity.class);
        intentActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, MainActivity.ALARM_RTC, intentActivity, PendingIntent.FLAG_UPDATE_CURRENT);

        String txt = MainActivity.getTodayAndTomorrowEE(getTodayDate(), getTomorrowDate(), context);
        NotificationManager notManager = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));

        Notification notification;
        if(Build.VERSION.SDK_INT > 24) {
             notification = new Notification.Builder(context)
                    .setColor(context.getResources().getColor(R.color.colorAccent))
                    .setContentTitle(context.getResources().getString(R.string.today) + ": ")
                    .setSmallIcon(R.drawable.icon_small)
                    .setStyle(new Notification.BigTextStyle()
                            .bigText(txt))
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentText(txt)
                    .setContentIntent(pendingIntent)
                    .setChannelId(idChannel)
                    .build();

            NotificationChannel channel = new NotificationChannel(idChannel, nameChannel, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("channel_notify_events");
            channel.enableVibration(false);
            notManager.createNotificationChannel(channel);

        } else {
              notification = new Notification.Builder(context)
                    .setContentTitle(context.getResources().getString(R.string.today) + ": ")
                    .setSmallIcon(R.drawable.icon_small)
                    .setStyle(new Notification.BigTextStyle()
                            .bigText(txt))
                    .setContentText(txt)
                    .setContentIntent(pendingIntent)
                    .build();
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

    private String getTomorrowDate() {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; //отсчет месяцев идет с 0
        int currentYear = calendar.get(Calendar.YEAR);
        DateFormat df = new SimpleDateFormat("EEE");
        String dayOfWeek = df.format(calendar.getTime());
        String dateToday =  dayOfWeek + ", " + Integer.toString(currentDayOfMonth)+  "." + Integer.toString(currentMonth)
                + "." + Integer.toString(currentYear);
        return dateToday;
    }
}
