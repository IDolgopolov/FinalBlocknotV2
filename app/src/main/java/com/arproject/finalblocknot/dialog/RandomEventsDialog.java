package com.arproject.finalblocknot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

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
        if( getTag().equals(MainActivity.TAG_EDITING_PAST)) {
            textForEditing = getArguments().getString("TEXT");
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
        Log.i("delete_all", "hello");
        return dialog;
    }





    private EditText generateView() {
       EditText editText= new EditText(getContext());
        editText.setHeight(160);
        editText.setWidth(60);
        editText.setHorizontallyScrolling(false);
        editText.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
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
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.saving_complete), Toast.LENGTH_SHORT).show();
                if(getTag().equals(MainActivity.TAG_CREATE_EVENT)) {
                    MainActivity.addRandomEventInBD(txt, date);
                } else if(getTag().equals(MainActivity.TAG_EDITING_EVENT)) {
                    MainActivity.updateRandomEvent(txt, idEditingEvent);
                } else if(getTag().equals(MainActivity.TAG_EDITING_PAST)) {
                    Bundle args = getArguments();
                    MainActivity.updatePastED(args.getString("TEXT"), args.getString("DATE"), txt);
                }
            }
        }
    }

    public void setTextForEditing(String text, int id) {
        textForEditing = text;
        idEditingEvent = id;
    }
}
