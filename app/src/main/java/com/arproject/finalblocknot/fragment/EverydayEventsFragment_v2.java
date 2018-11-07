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
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.arproject.finalblocknot.MainActivity;
import com.arproject.finalblocknot.R;
import com.arproject.finalblocknot.dialog.EverydayDeleteAllDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class EverydayEventsFragment_v2 extends Fragment {

    private static ArrayList<EditText> arrayEditText = new ArrayList<>();
    private ArrayList<TextView> arrayDateTextView = new ArrayList<>();
    private ArrayList<TableLayout> arrayTable = new ArrayList<>();
    private GridLayout grid;
    private FloatingActionButton buttonDeleteAll;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        grid = (GridLayout) inflater.inflate(R.layout.everyday_fragment_v2, null);
        for (int i = 0; i < grid.getChildCount() - 1; i++) {
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
        buttonDeleteAll = (FloatingActionButton) grid.findViewById(R.id.fab_delete_all);
        buttonDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EverydayDeleteAllDialog dialog = new EverydayDeleteAllDialog();
                dialog.show(MainActivity.sFragmentManager, "DELETE_ALL_ED");
            }
        });

        grid.removeAllViews();

        setDate();

        for(int i = 0; i < arrayEditText.size(); i++) {

            final EditText editText =  arrayEditText.get(i);

            String id = getResources().getResourceEntryName(editText.getId());
            Log.e("idPosition", id);
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

        GridLayout gridFragment = new GridLayout(getContext());
        gridFragment.setColumnCount(2);
        gridFragment.setUseDefaultMargins(true);
        gridFragment.setLayoutParams(new ViewGroup.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT));
        for(int i = 0; i < arrayTable.size(); i++) {
            gridFragment.addView(arrayTable.get(i));
        }
        gridFragment.addView(buttonDeleteAll);

        LinearLayout emptyLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150);
        emptyLayout.setLayoutParams(params);
        gridFragment.addView(emptyLayout);


        return gridFragment;
    }

    private void setDate() {
        Calendar calendar = new GregorianCalendar();
        int maxDayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; //отсчет месяцев идет с 0
        int currentYear = calendar.get(Calendar.YEAR);
        String dateToday = Integer.toString(currentDayOfMonth)+  "." + Integer.toString(currentMonth)
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
            dateToday = Integer.toString(currentDayOfMonth)+  "." + Integer.toString(currentMonth)
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