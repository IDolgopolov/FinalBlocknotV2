package com.arproject.finalblocknot.dialog;

import android.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.arproject.finalblocknot.MainActivity;
import com.arproject.finalblocknot.fragment.EverydayEventsFragment_v3;


public class EverydayDeleteAllDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setNegativeButton("Удалить безвозвратно все записи", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.deleteAllEDInformation();
                EverydayEventsFragment_v3.deleteAllInformation();
            }
        }).setPositiveButton("отменить удаление", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }

}
