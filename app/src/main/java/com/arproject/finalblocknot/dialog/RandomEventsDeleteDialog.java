package com.arproject.finalblocknot.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.arproject.finalblocknot.MainActivity;

public class RandomEventsDeleteDialog extends DialogFragment {
    private int id;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        id = getArguments().getInt("ID");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Удалить запись?").setPositiveButton("отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setNegativeButton("удалить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.deleteRandomEventFromDB(id);
            }
        });

        return builder.create();
    }


}
