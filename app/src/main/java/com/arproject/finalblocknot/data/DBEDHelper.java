package com.arproject.finalblocknot.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBEDHelper extends SQLiteOpenHelper {
    private static final String FILE_DBED_NAME = "everyday_events.db";

    public DBEDHelper(Context context) {
        super(context, FILE_DBED_NAME, null, DBConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String DB_PARAMETERS = "CREATE TABLE " + DBConstants.TABLE_ED_NAME +
                "(" + DBConstants._ID + " INTEGER PRIMARY KEY, " + DBConstants.INFORMATION +
                " TEXT, " + DBConstants.DATE + " TEXT, " + DBConstants.IMPORTANCE + " INTEGER, "
                + DBConstants.POSITION + " TEXT" + ")";
        sqLiteDatabase.execSQL(DB_PARAMETERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addEDInformation(String txt, String date, String position) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DBConstants.INFORMATION, txt);
        cv.put(DBConstants.DATE, date);
        cv.put(DBConstants.POSITION, position);

        db.insert(DBConstants.TABLE_ED_NAME, null, cv);
        db.close();
    }

    public void updateEDInformation(String text, String date, String position) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBConstants.INFORMATION, text);

        db.update(DBConstants.TABLE_ED_NAME, cv, DBConstants.DATE + " = ?"
                + " AND " + DBConstants.POSITION + " = ?", new String[] {date, position});

        db.close();
    }

    public String getInformation(String date, String position) {
        SQLiteDatabase db = this.getWritableDatabase();

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
        db.close();

        return text;
    }

    public void deleteAllEDInformation() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBConstants.TABLE_ED_NAME, null, null);
        db.close();
    }


}
