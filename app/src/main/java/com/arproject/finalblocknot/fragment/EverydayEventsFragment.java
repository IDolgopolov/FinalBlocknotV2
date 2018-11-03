package com.arproject.finalblocknot.fragment;

import android.app.FragmentManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.arproject.finalblocknot.MainActivity;
import com.arproject.finalblocknot.R;
import com.arproject.finalblocknot.dialog.EverydayDeleteAllDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class EverydayEventsFragment extends Fragment {
    private ArrayList<TableLayout> arrayTable = new ArrayList<>();
    private ArrayList<TableRow> arrayRow = new ArrayList<>();
    private static ArrayList<EditText> arrayEditText = new ArrayList<>();
    private ArrayList<TextView> arrayDateTextView = new ArrayList<>();
    private int idForEditText;
    private int positionForEditText;
    private Resources contextResources;
    private String packageName;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        for(int i = 0; i < 7; i++) {
            TableLayout tl = (TableLayout) inflater.inflate(R.layout.everyday_fragment, null);
            arrayTable.add(tl);
            arrayDateTextView.add((TextView) ((TableRow) tl.getChildAt(0)).getChildAt(0));
            for(int g = 1; g < tl.getChildCount(); g++) {
                TableRow tr = (TableRow) tl.getChildAt(g);
                arrayRow.add(tr);
            }
        }

        contextResources = getContext().getResources();
        packageName = getContext().getPackageName();
        for(int i = 0; i < arrayRow.size(); i++) {
            EditText editText = (EditText) arrayRow.get(i).getChildAt(1);
            arrayEditText.add(editText);

            if(i < 5) {
                String pos = Integer.toString(positionForEditText);
               idForEditText = contextResources.getIdentifier("1" + positionForEditText, "id", packageName);
               editText.setId(idForEditText);
                Log.i("idET", idForEditText + "");
            } else if (i < 10) {
                String pos = Integer.toString(positionForEditText);
                idForEditText = contextResources.getIdentifier("2" + positionForEditText, "id", packageName);
                editText.setId(idForEditText);
                Log.i("idET", idForEditText + "");
            } else if (i < 15) {
                String pos = Integer.toString(positionForEditText);
                idForEditText = contextResources.getIdentifier("3" + positionForEditText, "id", packageName);
                editText.setId(idForEditText);
                Log.i("idET", idForEditText + "");
            } else if (i < 20) {
                String pos = Integer.toString(positionForEditText);
                idForEditText = contextResources.getIdentifier("4" + positionForEditText, "id", packageName);
                editText.setId(idForEditText);
                Log.i("idET", idForEditText + "");
            } else if (i < 25) {
                String pos = Integer.toString(positionForEditText);
                idForEditText = contextResources.getIdentifier("5" + positionForEditText, "id", packageName);
                editText.setId(idForEditText);
                Log.i("idET", idForEditText + "");
            } else if (i < 30) {
                String pos = Integer.toString(positionForEditText);
                idForEditText = contextResources.getIdentifier("6" + positionForEditText, "id", packageName);
                editText.setId(idForEditText);
                Log.i("idET", idForEditText + "");
            } else if (i < 35) {
                String pos = Integer.toString(positionForEditText);
                idForEditText = contextResources.getIdentifier("7" + positionForEditText, "id", packageName);
                editText.setId(idForEditText);
                Log.i("idET", idForEditText + "");
            }
            if(positionForEditText < 4) {
                positionForEditText++;
            } else {
                positionForEditText = 0;
            }
            arrayEditText.add(editText);
        }


        setDate();


        for(int g = 0; g < arrayEditText.size(); g++) {

           final EditText editText =  arrayEditText.get(g);

           String idET = Integer.toString(editText.getId());
           final String idPosition = Character.toString(idET.charAt(1));

           final String date = arrayDateTextView.get( Character.getNumericValue(idET.charAt(0)) - 1 ).getText().toString();
           String information = MainActivity.getEDInformation(date, idPosition);

           if(information != null) {
               editText.setText(information);
           } else {
               MainActivity.addEDInDB("while null", date, idPosition);
           }

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
        for (int i = 1; i < arrayTable.size(); i++) {
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
            dateToday = "date: gjjhhj"  + Integer.toString(currentDayOfMonth)+  "." + Integer.toString(currentMonth)
                    + "." + Integer.toString(currentYear);
            arrayDateTextView.get(i).setText(dateToday);
        }
    }

    public static void deleteAllInfromation() {
        for(int i = 0; i < arrayEditText.size(); i++) {
            arrayEditText.get(i).setText("");
        }
    }
}
