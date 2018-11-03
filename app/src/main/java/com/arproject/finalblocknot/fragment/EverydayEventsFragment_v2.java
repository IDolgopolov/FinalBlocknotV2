package com.arproject.finalblocknot.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TimingLogger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.arproject.finalblocknot.MainActivity;
import com.arproject.finalblocknot.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class EverydayEventsFragment_v2 extends Fragment {

    private static ArrayList<EditText> arrayEditText = new ArrayList<>();
    private ArrayList<TextView> arrayDateTextView = new ArrayList<>();
    private ArrayList<TableLayout> arrayTable = new ArrayList<>();
    private GridLayout grid;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        long startTime = System.currentTimeMillis();

        grid = (GridLayout) inflater.inflate(R.layout.everyday_fragment_v2, null);
        for (int i = 0; i < grid.getChildCount(); i++) {
            TableLayout tl = (TableLayout) grid.getChildAt(i);
            arrayTable.add(tl);
            for(int g = 0; g < tl.getChildCount(); g++) {
                TableRow row = (TableRow) tl.getChildAt(g);
                if (g == 0) {
                    arrayDateTextView.add((TextView) row.getChildAt(0));
                } else {
                    arrayEditText.add((EditText) row.getChildAt(1));
                }

            }
        }
        grid.removeAllViews();
        Log.i("timeCount", System.currentTimeMillis() - startTime + "");
        startTime = System.currentTimeMillis();

        setDate();
        Log.i("timeCount", System.currentTimeMillis() - startTime + "");
        startTime = System.currentTimeMillis();

        for(int i = 0; i < arrayEditText.size(); i++) {

            final EditText editText =  arrayEditText.get(i);

            final String idPosition = Integer.toString(editText.getId());
            int tableNumber = Character.getNumericValue(idPosition.charAt(1));
            final String date = arrayDateTextView.get(tableNumber).getText().toString();
            String information = MainActivity.getEDInformation(date, idPosition);

            if(information != null) {
                editText.setText(information);
            } /* else {
                MainActivity.addEDInDB("while null", date, idPosition);
            } */

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    if(MainActivity.getEDInformation(date, idPosition) == null) {
                        if(editText.getText().toString().isEmpty()) return;
                        MainActivity.addEDInDB(charSequence.toString(), date, idPosition);
                    } else {
                        MainActivity.updateED(charSequence.toString(), date, idPosition);
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }
        Log.i("timeCount", System.currentTimeMillis() - startTime + "");
        startTime = System.currentTimeMillis();

        GridLayout gridFragment = new GridLayout(getContext());
        gridFragment.setColumnCount(2);
        gridFragment.setUseDefaultMargins(true);
        gridFragment.setLayoutParams(new ViewGroup.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT));
        for(int i = 0; i < arrayTable.size(); i++) {
            gridFragment.addView(arrayTable.get(i));
        }
        Log.i("timeCount", System.currentTimeMillis() - startTime + "");
        startTime = System.currentTimeMillis();

        return gridFragment;
    }

    private void setDate() {
        Calendar calendar = new GregorianCalendar();
        int maxDayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; //отсчет месяцев идет с 0
        int currentYear = calendar.get(Calendar.YEAR);
        String dateToday = "date: "  + Integer.toString(currentDayOfMonth)+  "." + Integer.toString(currentMonth)
                + "." + Integer.toString(currentYear);
        arrayDateTextView.get(0).setText(dateToday);
        for (int i = 1; i < arrayDateTextView.size(); i++) {
            if(currentDayOfMonth < maxDayInMonth) {
                currentDayOfMonth++;
            } else {
                currentDayOfMonth = 1;
                if(currentMonth == 12) {
                    currentMonth = 1;
                    currentYear++;
                } else {
                    currentMonth++;
                }
            }
            dateToday = "date: "  + Integer.toString(currentDayOfMonth)+  "." + Integer.toString(currentMonth)
                    + "." + Integer.toString(currentYear);
            arrayDateTextView.get(i).setText(dateToday);
        }
    }

    public static void deleteAllInformation() {
        for(int i = 0; i < arrayEditText.size(); i++) {
            arrayEditText.get(i).setText("");
        }
    }
}