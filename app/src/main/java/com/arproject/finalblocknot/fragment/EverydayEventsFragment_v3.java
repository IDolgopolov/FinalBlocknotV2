package com.arproject.finalblocknot.fragment;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.arproject.finalblocknot.MainActivity;
import com.arproject.finalblocknot.R;
import com.arproject.finalblocknot.dialog.EverydayDeleteAllDialog;
import com.arproject.finalblocknot.dialog.PastEverydayDialog;
import com.arproject.finalblocknot.dialog.PickNotificationDialog;

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
    private FloatingActionButton buttonPast;
    private int[] arrDateSum = new int[8];
    private FloatingActionButton buttonPickNotification;
    private ImageButton buttonBackwardsPage, buttonNextPage;
    private TextView pageNumberView;
    private int pageNumber = 1;
    private TextWatcher[] arrayTextWatcher = new TextWatcher[8 * 5];




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final int widthTl = (int) Math.round(getArguments().getInt("width") * 0.46);
        final int heightRow = (int) Math.round(getArguments().getInt("height") * 0.06);
        layoutParent = (LinearLayout) inflater.inflate(R.layout.everyday_fragment_v3, null);

        if(arrayEditText.size() != 0) arrayEditText.clear();
        for (int i = 1; i < layoutParent.getChildCount() - 1; i++) {
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
        new Thread(new Runnable() {
            @Override
            public void run() {

                setDate(pageNumber);

                final Context context = getContext();
                for (int i = 0; i < arrayEditText.size(); i++) {

                    final EditText editText = arrayEditText.get(i);

                    String id = getResources().getResourceEntryName(editText.getId());

                    final String idPosition = Character.toString(id.charAt(3));
                    final int tableNumber = Character.getNumericValue(id.charAt(1));
                    final String date = arrayDateTextView.get(tableNumber).getText().toString();
                    final String information = MainActivity.getEDInformation(date, idPosition);


                    if (information != null) editText.setText(information);

                    TextWatcher textWatcher = generateTextWatcher(date, idPosition, tableNumber, context, editText);
                    arrayTextWatcher[i] = textWatcher;
                    editText.addTextChangedListener(textWatcher);

                }


                buttonDeleteAll = (FloatingActionButton) layoutParent.findViewById(R.id.fab_delete_all);
                buttonDeleteAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EverydayDeleteAllDialog dialog = new EverydayDeleteAllDialog();
                        dialog.show(MainActivity.sFragmentManager, "DELETE_ALL_ED");

                    }
                });

                buttonPast = (FloatingActionButton) layoutParent.findViewById(R.id.fab_past_everyday);
                buttonPast.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PastEverydayDialog dialog = new PastEverydayDialog();
                        dialog.show(MainActivity.sFragmentManager, "PAST_EVENT");
                    }

                });

                buttonPickNotification = (FloatingActionButton) layoutParent.findViewById(R.id.fab_pick_notification);
                buttonPickNotification.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PickNotificationDialog dialog = new PickNotificationDialog();
                        dialog.show(MainActivity.sFragmentManager, "PICK_NOTIFICATION");
                    }
                });

                pageNumberView = (TextView) layoutParent.findViewById(R.id.view_page_count);
                pageNumberView.setText(Integer.toString(pageNumber));

                buttonBackwardsPage = (ImageButton) layoutParent.findViewById(R.id.button_backwards);
                buttonBackwardsPage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            pageNumber--;
                            if (pageNumber < 1) {
                                pageNumberView.setText(Integer.toString(pageNumber - 1));
                            } else {
                                pageNumberView.setText(Integer.toString(pageNumber));
                            }
                            setDate(pageNumber);
                            for (int i = 0; i < arrayEditText.size(); i++) {
                                EditText editText = arrayEditText.get(i);
                                editText.removeTextChangedListener(arrayTextWatcher[i]);
                                arrayTextWatcher[i] = null;
                                String id = getResources().getResourceEntryName(editText.getId());

                                final String idPosition = Character.toString(id.charAt(3));
                                final int tableNumber = Character.getNumericValue(id.charAt(1));
                                final String date = arrayDateTextView.get(tableNumber).getText().toString();
                                String information = MainActivity.getEDInformation(date, idPosition);

                                if (information != null) {
                                    editText.setText(information);
                                } else {
                                    editText.setText("");
                                }
                                final Context context = getContext();
                                TextWatcher textWatcher = generateTextWatcher(date, idPosition, tableNumber, context, editText);
                                arrayTextWatcher[i] = textWatcher;
                                editText.addTextChangedListener(textWatcher);
                            }
                        }catch(Exception e) { }
                        }
                });


                buttonNextPage = (ImageButton) layoutParent.findViewById(R.id.button_next);
                buttonNextPage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                        pageNumber++;
                        if(pageNumber < 1) {
                            pageNumberView.setText(Integer.toString(pageNumber-1));
                        } else {
                            pageNumberView.setText(Integer.toString(pageNumber));
                        }
                        setDate(pageNumber);

                        for(int i = 0; i < arrayEditText.size(); i++) {
                            EditText editText = arrayEditText.get(i);
                            editText.removeTextChangedListener(arrayTextWatcher[i]);
                            arrayTextWatcher[i] = null;

                            String id = getResources().getResourceEntryName(editText.getId());
                            final String idPosition = Character.toString(id.charAt(3));
                            final int tableNumber = Character.getNumericValue(id.charAt(1));
                            final String date = arrayDateTextView.get(tableNumber).getText().toString();
                            String information = MainActivity.getEDInformation(date, idPosition);


                            if (information != null) {
                                editText.setText(information);
                            } else {
                                editText.setText("");
                            }

                            final Context context = getContext();
                            TextWatcher textWatcher = generateTextWatcher(date, idPosition, tableNumber, context, editText);
                            arrayTextWatcher[i] = textWatcher;
                            editText.addTextChangedListener(textWatcher);

                        }
                        }catch(Exception e) { }
                    }

                });

            }
        }).start();


        return layoutParent;
    }



    private void setDate(int pageNumberAdd) {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, (pageNumberAdd - 1) * 8);
        int maxDayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; //отсчет месяцев идет с 0
        int currentYear = calendar.get(Calendar.YEAR);
        DateFormat df = new SimpleDateFormat("EEE");
        String dayOfWeek = df.format(calendar.getTime());

        arrDateSum[0] =  currentDayOfMonth + currentMonth*100 + currentYear*10000;
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
            arrDateSum[i] = currentDayOfMonth + currentMonth*100 + currentYear*10000;
            arrayDateTextView.get(i).setText(dateToday);
        }
    }

    public static void deleteAllInformation() {
        for(int i = 0; i < arrayEditText.size(); i++) {
            arrayEditText.get(i).setText("");
        }
    }

    private TextWatcher generateTextWatcher(final String date, final String idPosition,
                                     final int tableNumber, final Context context, final EditText editText) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (MainActivity.getEDInformation(date, idPosition) == null) {
                    if (editText.getText().toString().isEmpty()) return;
                    MainActivity.addEDInDB(charSequence.toString(), date, idPosition, arrDateSum[tableNumber]);
                } else {
                    if(editText.getText().toString().equals("")) MainActivity.deleteEE(date, idPosition, context);
                    MainActivity.updateED(charSequence.toString(), date, idPosition);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        return textWatcher;
    }

}