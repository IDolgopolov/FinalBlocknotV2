package com.arproject.finalblocknot.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.arproject.finalblocknot.OneRandomEvent;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private final static String FILE_DB_NAME = "random_events.db";

    public DBHelper(Context context) {
        super(context, FILE_DB_NAME, null, DBConstants.DB_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String DB_PARAMETERS = "CREATE TABLE " + DBConstants.TABLE_NAME +
                "(" + DBConstants._ID + " INTEGER PRIMARY KEY, " + DBConstants.INFORMATION +
                " TEXT, " + DBConstants.DATE + " TEXT" + ")";
        sqLiteDatabase.execSQL(DB_PARAMETERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addInformation(String text, String date) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put(DBConstants.INFORMATION, text);
            cv.put(DBConstants.DATE, date);

            db.insert(DBConstants.TABLE_NAME, null, cv);
            db.close();

    }


    public void deleteInformation(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(DBConstants.TABLE_NAME, DBConstants._ID + " = " + Integer.toString(id), null);
        db.close();
    }


    public String getInformation(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(
                DBConstants.TABLE_NAME,
                new String[] { DBConstants._ID, DBConstants.INFORMATION},
                DBConstants._ID + " = ?",
                new String[] {Integer.toString(id)},
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            Log.e("DB", "error generate cursor");
        }

        String text = cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.INFORMATION));
        cursor.close();
        db.close();

        return text;
    }

    public ArrayList<OneRandomEvent> getAllInformation() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                DBConstants.TABLE_NAME,
                new String[] { DBConstants.INFORMATION, DBConstants._ID, DBConstants.DATE},
                null, null,
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            Log.e("DB", "error generate cursor");
        }

        ArrayList<OneRandomEvent> listORE = new ArrayList<OneRandomEvent>();

        for(int i = 0; i < cursor.getCount(); i++) {
            String information = cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.INFORMATION));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.DATE));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants._ID));
            OneRandomEvent ORE = new OneRandomEvent(information, id, date);
            listORE.add(ORE);
            if(!cursor.isLast()) cursor.moveToNext();
        }
        db.close();

        return listORE;
    }

    public void updateInformation(String text, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBConstants.INFORMATION, text);

        db.update(DBConstants.TABLE_NAME, cv, DBConstants._ID + " = ?", new String[] {Integer.toString(id)});

        db.close();
    }


}
