package com.arproject.finalblocknot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.arproject.finalblocknot.MainActivity;
import com.arproject.finalblocknot.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class RandomEventsDialog extends DialogFragment {
    private boolean saveOrNot = true;
    private EditText textEditView;
    private int idEditingEvent;
    private String textForEditing;
    private InputMethodManager imm;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        textEditView = generateView();
        if(getTag().equals(MainActivity.TAG_EDITING_EVENT)) {
            textEditView.setText(textForEditing);
        }

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        if(getTag().equals(MainActivity.TAG_CREATE_EVENT)) {
            builder.setView(textEditView).setPositiveButton(R.string.save,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            saveOrNot = true;
                        }
                    }).setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    saveOrNot = false;
                }
            });
        } else {
            builder.setView(textEditView).setPositiveButton(R.string.save,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            saveOrNot = true;
                        }
                    });
        }
        Dialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        return dialog;
    }





    private EditText generateView() {
       EditText editText= new EditText(getContext());
        editText.setHeight(160);
        editText.setWidth(60);
        editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setHorizontallyScrolling(false);
        editText.setLines(10);
        editText.setHint(R.string.hint_random_events);
        editText.setPadding(30, 0, 30, 50);


        return editText;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        if(saveOrNot) {

            String txt = textEditView.getText().toString();
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
            String date = df.format(Calendar.getInstance().getTime());

            if (!txt.isEmpty()) {
                if(getTag().equals(MainActivity.TAG_CREATE_EVENT)) {
                    MainActivity.addRandomEventInBD(txt, date);
                } else {
                    MainActivity.updateRandomEvent(txt, idEditingEvent);
                }
            }
        }
    }

    public void setTextForEditing(String text, int id) {
        textForEditing = text;
        idEditingEvent = id;
    }
}
