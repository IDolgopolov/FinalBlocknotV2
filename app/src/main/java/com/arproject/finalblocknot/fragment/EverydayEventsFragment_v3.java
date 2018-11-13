package com.arproject.finalblocknot.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.arproject.finalblocknot.MainActivity;
import com.arproject.finalblocknot.R;
import com.arproject.finalblocknot.dialog.EverydayDeleteAllDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class EverydayEventsFragment_v3 extends Fragment {

    private static ArrayList<EditText> arrayEditText = new ArrayList<>();
    private ArrayList<TextView> arrayDateTextView = new ArrayList<>();
    private ArrayList<TableLayout> arrayTable = new ArrayList<>();
    private LinearLayout layoutParent;
    private FloatingActionButton buttonDeleteAll;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final int widthTl = (int) Math.round(getArguments().getInt("width") * 0.46);
        final int heightRow = (int) Math.round(getArguments().getInt("height") * 0.04);
        layoutParent = (LinearLayout) inflater.inflate(R.layout.everyday_fragment_v3, null);

        for (int i = 0; i < layoutParent.getChildCount() - 1; i++) {
            LinearLayout layout = (LinearLayout) layoutParent.getChildAt(i);
            for (int g = 0; g < layout.getChildCount(); g++) {
                TableLayout tl = (TableLayout) layout.getChildAt(g);
                tl.setMinimumWidth(widthTl);
                arrayTable.add(tl);
                for(int d = 0; d < tl.getChildCount(); d++) {
                    final TableRow row = (TableRow) tl.getChildAt(d);
                    row.setMinimumHeight(heightRow);
                    row.setMinimumWidth(widthTl);
                    if (d == 0) {
                        arrayDateTextView.add((TextView) row.getChildAt(0));
                    } else {
                        final TextView tV = (TextView) row.getChildAt(0);
                         final EditText eT = (EditText) row.getChildAt(1);
                         arrayEditText.add(eT);
                         tV.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                int widthTV = tV.getWidth();
                                tV.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                TableRow.LayoutParams params = new TableRow.LayoutParams( widthTl - widthTV, heightRow);
                                eT.setLayoutParams(params);
                            }
                        });


                    }
                }
            }
        }

        buttonDeleteAll = (FloatingActionButton) layoutParent.findViewById(R.id.fab_delete_all);
        buttonDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EverydayDeleteAllDialog dialog = new EverydayDeleteAllDialog();
                dialog.show(MainActivity.sFragmentManager, "DELETE_ALL_ED");
            }
        });


        setDate();
        Log.i("information_my", arrayEditText.size() + "size");
        for(int i = 0; i < arrayEditText.size(); i++) {

            final EditText editText =  arrayEditText.get(i);

            String id = getResources().getResourceEntryName(editText.getId());

            final String idPosition = Character.toString(id.charAt(3));
            int tableNumber = Character.getNumericValue(id.charAt(1));
            final String date = arrayDateTextView.get(tableNumber).getText().toString();
            String information = MainActivity.getEDInformation(date, idPosition);

            if(information != null) editText.setText(information);


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


        return layoutParent;
    }

    private void setDate() {
        Calendar calendar = new GregorianCalendar();
        int maxDayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; //отсчет месяцев идет с 0
        int currentYear = calendar.get(Calendar.YEAR);
        DateFormat df = new SimpleDateFormat("EEE");
        String dayOfWeek = df.format(Calendar.getInstance().getTime());

        String dateToday =  dayOfWeek + ", " + Integer.toString(currentDayOfMonth)+  "." + Integer.toString(currentMonth)
                + "." + Integer.toString(currentYear);
        arrayDateTextView.get(0).setText(dateToday);
        for (int i = 1; i < arrayDateTextView.size(); i++) {
            calendar.add(Calendar.DAY_OF_WEEK, 1);
            dayOfWeek = df.format(calendar.getTime());
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
            dateToday =  dayOfWeek + ", " +  Integer.toString(currentDayOfMonth)+  "." + Integer.toString(currentMonth)
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