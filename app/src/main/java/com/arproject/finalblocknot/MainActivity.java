package com.arproject.finalblocknot;



import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.arproject.finalblocknot.data.DBEDHelper;
import com.arproject.finalblocknot.data.DBHelper;
import com.arproject.finalblocknot.dialog.PastEverydayDialog;
import com.arproject.finalblocknot.dialog.RandomEventsDialog;
import com.arproject.finalblocknot.fragment.EverydayEventsFragment_v3;
import com.arproject.finalblocknot.fragment.RandomEventsFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity  {
    public static DBHelper db;
    public static DBEDHelper dbED;
    private DialogFragment dialogRandomEvents;
    private static RandomEventsFragment fragmentRandomEvents;
    private static EverydayEventsFragment_v3 fragmentEverydayEvents;
    public static FragmentManager sFragmentManager;
    public final static String TAG_CREATE_EVENT = "dialog_for_creating";
    public final static String TAG_EDITING_EVENT = "dialog_for_editing";
    public final static String TAG_EDITING_PAST = "dialog_for_editing_past";
    public int displayHeight, displayWidth;
    private boolean generateEEFV2 = false;
    public static final int ALARM_RTC = 0;
    private SharedPreferences sPref;
    private final String APPLICATION_FIRST_LAUNCH = "apllication_launch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Display display = getWindowManager().getDefaultDisplay();
        Point pointSize = new Point();
        display.getSize(pointSize);
        displayHeight = pointSize.y;
        displayWidth = pointSize.x;

        int heightForRE = (int) Math.round(displayHeight * 0.4);
        CoordinatorLayout layoutRE = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        RelativeLayout.LayoutParams paramsRE = (RelativeLayout.LayoutParams) layoutRE.getLayoutParams();
        paramsRE.height = heightForRE;
        paramsRE.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutRE.setLayoutParams(paramsRE);

        db = new DBHelper(getApplicationContext());
        dbED = new DBEDHelper(getApplicationContext());
        sFragmentManager = getSupportFragmentManager();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(db != null) {
                    dialogRandomEvents = new RandomEventsDialog();
                    dialogRandomEvents.show(sFragmentManager, TAG_CREATE_EVENT);
                }
            }
        });

        fragmentRandomEvents = new RandomEventsFragment();

        final FragmentTransaction fragmentTransaction = sFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.layout_for_recycler_view, (Fragment) fragmentRandomEvents);
        fragmentTransaction.commit();
        generateEEFV2 = true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (generateEEFV2) {
            final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
            executor.schedule(
                    new Runnable() {
                        @Override
                        public void run() {
                            Bundle args = new Bundle();
                            args.putInt("width", displayWidth);
                            args.putInt("height", displayHeight);
                            FragmentTransaction fT = sFragmentManager.beginTransaction();
                            fragmentEverydayEvents = new EverydayEventsFragment_v3();
                            fragmentEverydayEvents.setArguments(args);
                            fT.add(R.id.layout_for_everyday_events, (Fragment) fragmentEverydayEvents);
                            fT.commit();

                        }
                    }, 100, TimeUnit.MILLISECONDS);
            generateEEFV2 = false;

        }

        if(checkFirstLaunch()) {
            generateAlarm(3 * 60 * 60 * 1000, getApplicationContext());
        }

    }

    @Override
    protected void onDestroy() {
        dbED.close();
        db.close();

        super.onDestroy();



    }

    public static void addRandomEventInBD(String txt, String date) {
        db.addInformation(txt, date);
        fragmentRandomEvents.updateListRandomEvents();
    }

    public static void deleteRandomEventFromDB(int id) {
        db.deleteInformation(id);
        fragmentRandomEvents.updateListRandomEvents();
    }

    public static void updateRandomEvent(String txt, int id) {
        db.updateInformation(txt, id);
        fragmentRandomEvents.updateListRandomEvents();
    }

    public static void openDialogForEditingRandomEvent(String text, int id) {
        RandomEventsDialog dialog = new RandomEventsDialog();
        dialog.setTextForEditing(text, id);
        dialog.show(sFragmentManager, TAG_EDITING_EVENT);
    }

    public static void addEDInDB(String text, String date, String position, int sumDate) {
        dbED.addEDInformation(text, date, position, sumDate);
    }
    public static void updateED(String text, String date, String position) {
        dbED.updateEDInformation(text, date, position);
    }

    public static String getEDInformation(String date, String position) {
        return dbED.getInformation(date, position);
    }

    public static void deleteAllEDInformation() {
        dbED.deleteAllEDInformation();
    }

    public static void openDialogForEditingPastEvent(String txt, String date, String pos) {
        RandomEventsDialog dialog = new RandomEventsDialog();
        Bundle args = new Bundle();
        args.putString("TEXT", txt);
        args.putString("POSITION", pos);
        args.putString("DATE", date);
        dialog.setArguments(args);
        dialog.show(sFragmentManager, TAG_EDITING_PAST);
    }
    public static void updatePastED(String text, String date, String newText, String pos) {
        dbED.updatePastEDInformation(text, date, newText, pos);
        PastEverydayDialog.updateList();
    }

    public static String getTodayAndTomorrowEE(String dateToday, String dateTomorrow, Context context) {
        if (dbED == null) dbED = new DBEDHelper(context);
        return dbED.getTodayEndTomorrowEvent(dateToday, dateTomorrow, context);
    }

    public static void generateAlarm(int hour, int min, long interval,  Context context) {
        Log.i("check_not", "alarm hour generate" );
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        deleteAllAlarm(context);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour, min);
        Log.i("notification", "generate alarm");
        Intent intent = new Intent(context, MyAlarmManager.class);
        PendingIntent pendingIntent =  PendingIntent.getBroadcast(context, ALARM_RTC, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, GregorianCalendar.getInstance().getTimeInMillis(), interval, pendingIntent);
    }
    public static void generateAlarm(long interval,  Context context) {
        Log.i("check_not", "alarm millis generate" );
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        deleteAllAlarm(context);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        Intent intent = new Intent(context, MyAlarmManager.class);
        PendingIntent pendingIntent =  PendingIntent.getBroadcast(context, ALARM_RTC, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, GregorianCalendar.getInstance().getTimeInMillis(), interval, pendingIntent);
    }
    public static void deleteAllAlarm(Context context) {
        try {
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, MyAlarmManager.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_RTC, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            manager.cancel(pendingIntent);
            pendingIntent.cancel();
        } catch(Exception e) {
            Log.e("notification", "error cancel alarm");
        }
    }

    private boolean checkFirstLaunch() {
        sPref  = getPreferences(MODE_PRIVATE);
        if(sPref.getString(APPLICATION_FIRST_LAUNCH, "def").equals("def")) {
            SharedPreferences.Editor editor = sPref.edit();
            editor.putString(APPLICATION_FIRST_LAUNCH, APPLICATION_FIRST_LAUNCH);
            editor.apply();
            return true;
        }
        return false;

    }


}

