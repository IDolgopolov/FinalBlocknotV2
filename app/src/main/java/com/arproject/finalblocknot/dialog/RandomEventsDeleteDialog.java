package com.arproject.finalblocknot.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.arproject.finalblocknot.MainActivity;
import com.arproject.finalblocknot.R;

public class RandomEventsDeleteDialog extends DialogFragment {
    private int id;
    public static  final int OPTION_RANDOM = 1;
    public static final int OPTION_EVERYDAY = 2;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        id = getArguments().getInt("ID");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.question_delete_all).setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               if (getArguments().getInt("OPTION") == OPTION_RANDOM) {
                   MainActivity.deleteRandomEventFromDB(id);
               } else if (getArguments().getInt("OPTION") == OPTION_EVERYDAY) {
                   MainActivity.dbED.deletePastEvent(getArguments().getString("DATE"), getArguments().getString("TEXT"),
                           getArguments().getInt("POSITION"));
                   PastEverydayDialog.updateList();
               }

            }
        });

        return builder.create();
    }


}
