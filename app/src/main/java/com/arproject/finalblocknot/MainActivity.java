package com.arproject.finalblocknot;



import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.arproject.finalblocknot.data.DBEDHelper;
import com.arproject.finalblocknot.data.DBHelper;
import com.arproject.finalblocknot.dialog.RandomEventsDialog;
import com.arproject.finalblocknot.fragment.EverydayEventsFragment_v3;
import com.arproject.finalblocknot.fragment.RandomEventsFragment;

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
    public int displayHeight, displayWidth;
    private boolean generateEEFV2 = false;

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        dbED.closeDB();
        db.closeDB();
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

    public static void addEDInDB(String text, String date, String position) {
        dbED.addEDInformation(text, date, position);
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


}

