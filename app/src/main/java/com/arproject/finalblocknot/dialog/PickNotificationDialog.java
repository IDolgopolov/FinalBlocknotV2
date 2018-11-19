package com.arproject.finalblocknot.dialog;

import android.app.AlarmManager;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.arproject.finalblocknot.MainActivity;
import com.arproject.finalblocknot.R;
import com.arproject.finalblocknot.fragment.EverydayEventsFragment_v3;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class PickNotificationDialog extends DialogFragment {
    private boolean changeDate= false;
    private Spinner spinner;
    private String result;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LinearLayout layout = (LinearLayout) View.inflate(getContext(), R.layout.notification_picker_dialog, null);
        builder.setView(layout);

        spinner = layout.findViewById(R.id.spinner_hour_pick);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(getContext(), R.array.notification_sent_time, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    changeDate = true;
            }
        });
        spinner.setSelection(3);

        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if(changeDate) {
            String[] arr = getResources().getStringArray(R.array.notification_sent_time);
            String result = spinner.getSelectedItem().toString();
            long millis = 0;
            int hour = 0;
            if(result.equals(arr[0])) {
                millis = 0;
            } else if(result.equals(arr[1])) {
                millis = 30 * 60 * 1000;
            } else if(result.equals(arr[2])) {
                millis = 60 * 60 * 1000;
            } else if(result.equals(arr[3])) {
                millis = 2 *60 * 60 * 1000;
            } else if(result.equals(arr[4])) {
                millis = 3 * 60 * 60 * 1000;
            } else if(result.equals(arr[5])) {
                millis = 4 * 60 * 60 * 1000;
            } else if(result.equals(arr[6])) {
                millis = 5 * 60 * 60 * 1000;
            } else if(result.equals(arr[7])) {
                millis = 6 * 60 * 60 * 1000;
            }else if(result.equals(arr[8])) {
                hour = 6;
            }else if(result.equals(arr[9])) {
                hour = 12;
            }
            Log.i("check_not", "millis:" + millis );
            Log.i("check_not", "hour" + hour);
            if(millis != 0 ) {
                MainActivity.generateAlarm(millis, getContext());
            } else if (hour != 0) {
                MainActivity.generateAlarm(hour, 0, AlarmManager.INTERVAL_DAY, getContext());
            } else if(millis == 0 && hour == 0) {
                MainActivity.deleteAllAlarm(getContext());
            }

        }
    }
}
