package com.arproject.finalblocknot.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.arproject.finalblocknot.MainActivity;
import com.arproject.finalblocknot.OneRandomEvent;
import com.arproject.finalblocknot.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DBEDHelper extends SQLiteOpenHelper {
    private static final String FILE_DBED_NAME = "everyday_events.db";
    private static SQLiteDatabase db;

    public DBEDHelper(Context context) {
        super(context, FILE_DBED_NAME, null, DBConstants.DB_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String DB_PARAMETERS = "CREATE TABLE " + DBConstants.TABLE_ED_NAME +
                "(" + DBConstants._ID + " INTEGER PRIMARY KEY, " + DBConstants.INFORMATION +
                " TEXT, " + DBConstants.DATE + " TEXT, " + DBConstants.POSITION + " TEXT, " + DBConstants.SUM_DATE + " INTEGER" +")";

        sqLiteDatabase.execSQL(DB_PARAMETERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addEDInformation(String txt, String date, String position, int sumDate) {
        if (!db.isOpen()) db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DBConstants.INFORMATION, txt);
        cv.put(DBConstants.DATE, date);
        cv.put(DBConstants.POSITION, position);
        cv.put(DBConstants.SUM_DATE, sumDate);

        db.insert(DBConstants.TABLE_ED_NAME, null, cv);
    }

    public void updateEDInformation(String text, String date, String position) {
        if (!db.isOpen()) db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBConstants.INFORMATION, text);

        db.update(DBConstants.TABLE_ED_NAME, cv, DBConstants.DATE + " = ?"
                + " AND " + DBConstants.POSITION + " = ?", new String[] {date, position});

    }
    public void updatePastEDInformation(String text, String date, String newText, String pos) {
        if (!db.isOpen()) db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBConstants.INFORMATION, newText);

        db.update(DBConstants.TABLE_ED_NAME, cv, DBConstants.DATE + " = ? AND "  +
                DBConstants.INFORMATION + " = ? AND " + DBConstants.POSITION + " = ?" , new String[] {date, text, pos });

    }

    public String getInformation(String date, String position) {
        if (!db.isOpen()) db = this.getWritableDatabase();

        Cursor cursor = db.query(
                DBConstants.TABLE_ED_NAME,
                new String[] { DBConstants.DATE, DBConstants.INFORMATION},
                DBConstants.DATE + " = ?" + " AND "
                        +  DBConstants.POSITION + " = ?",
                new String[] {date, position},
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            Log.e("DB", "error generate cursor");
        }
        String text = null;
        try {
            text = cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.INFORMATION));
        } catch(Exception e) {
            text = null;
        }
        cursor.close();

        return text;
    }

    public void deleteAllEDInformation() {
        if (!db.isOpen()) db = this.getWritableDatabase();
        db.delete(DBConstants.TABLE_ED_NAME, null, null);

    }

    public void closeDB() {
        if (db != null) db.close();
    }

    public ArrayList<OneRandomEvent> getPastInformation() {
        if (!db.isOpen()) db = this.getWritableDatabase();

        Cursor cursor = db.query(
                DBConstants.TABLE_ED_NAME,
                new String[] { DBConstants.INFORMATION, DBConstants.POSITION,DBConstants.SUM_DATE,  DBConstants.DATE },
                null, null,
                null, null, null
        );

        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            Log.e("DB", "error generate cursor");
        }

        ArrayList<OneRandomEvent> listORE = new ArrayList<OneRandomEvent>();
        Calendar calendar = new GregorianCalendar();
        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; //отсчет месяцев идет с 0
        int currentYear = calendar.get(Calendar.YEAR);
        int sumDate = currentDayOfMonth + currentMonth * 100 + currentYear * 10000;

        for(int i = 0; i < cursor.getCount(); i++) {

            if(sumDate > cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.SUM_DATE))) {
                String txt = cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.INFORMATION));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.DATE));
                int pos = cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.POSITION));
                listORE.add(new OneRandomEvent(txt, pos, date));
            }
            cursor.moveToNext();
        }
        cursor.close();
        return listORE;

    }

    public void deletePastEvent(String date, String txt, int id) {
        if (!db.isOpen()) db = this.getWritableDatabase();
        db.delete(DBConstants.TABLE_ED_NAME, DBConstants.DATE + " = ? AND " + DBConstants.INFORMATION + " = ? AND " +
                DBConstants.POSITION + " = ?",
                new String[] {date, txt, Integer.toString(id)});

    }

    public String getTodayEndTomorrowEvent(String dateToday, String dateTomorrow, Context context) {
        if (!db.isOpen()) db = this.getWritableDatabase();

        Cursor cursorToday = db.query(
                DBConstants.TABLE_ED_NAME,
                new String[] { DBConstants.DATE, DBConstants.INFORMATION},
                DBConstants.DATE + " = ?", new String[] {dateToday},
                null, null, null);

        if (cursorToday != null) {
            cursorToday.moveToFirst();
        } else {
            Log.e("DB", "error generate cursor");
        }
        String infoToday = "";
        for(int i = 0; i < cursorToday.getCount(); i++) {
                if(cursorToday.isLast()) {
                    infoToday+= cursorToday.getString(cursorToday.getColumnIndexOrThrow(DBConstants.INFORMATION));
                } else {
                    infoToday+= cursorToday.getString(cursorToday.getColumnIndexOrThrow(DBConstants.INFORMATION)) + ", ";
                }
            cursorToday.moveToNext();
        }

        if(infoToday.equals("")) infoToday = context.getResources().getString(R.string.no_events_today);
        cursorToday.close();

        String infoTomorrow = "";
        Cursor cursorTomorrow = db.query(
                DBConstants.TABLE_ED_NAME,
                new String[] { DBConstants.DATE, DBConstants.INFORMATION},
                DBConstants.DATE + " = ?", new String[] {dateTomorrow},
                null, null, null);

        if (cursorTomorrow != null) {
            cursorTomorrow.moveToFirst();
        } else {
            Log.e("DB", "error generate cursor");
        }

        for(int i = 0; i < cursorTomorrow.getCount(); i++) {
            if(cursorTomorrow.isLast()) {
                infoTomorrow+= cursorTomorrow.getString(cursorTomorrow.getColumnIndexOrThrow(DBConstants.INFORMATION));
            } else {
                infoTomorrow+= cursorTomorrow.getString(cursorTomorrow.getColumnIndexOrThrow(DBConstants.INFORMATION)) + ", ";
            }
            cursorTomorrow.moveToNext();
        }
        if(infoTomorrow.equals("")) infoTomorrow = context.getResources().getString(R.string.no_events_today);
        cursorTomorrow.close();
        Log.i("notification", dateTomorrow);


        return infoToday + "\n\n" + context.getString(R.string.tomorrow) + " " + infoTomorrow;
    }

    public void deleteEE(String date, String pos) {
        if (!db.isOpen()) db = this.getWritableDatabase();
        db.delete(DBConstants.TABLE_ED_NAME, DBConstants.DATE + " = ? AND "
                + DBConstants.POSITION + " = ?", new String[] {date, pos});
    }


}
