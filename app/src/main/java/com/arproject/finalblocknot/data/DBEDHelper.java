package com.arproject.finalblocknot.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
                " TEXT, " + DBConstants.DATE + " TEXT, " + DBConstants.IMPORTANCE + " INTEGER, "
                + DBConstants.POSITION + " TEXT" + ")";
        sqLiteDatabase.execSQL(DB_PARAMETERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addEDInformation(String txt, String date, String position) {
        if (db == null) db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DBConstants.INFORMATION, txt);
        cv.put(DBConstants.DATE, date);
        cv.put(DBConstants.POSITION, position);

        db.insert(DBConstants.TABLE_ED_NAME, null, cv);
    }

    public void updateEDInformation(String text, String date, String position) {
        if (db == null) db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBConstants.INFORMATION, text);

        db.update(DBConstants.TABLE_ED_NAME, cv, DBConstants.DATE + " = ?"
                + " AND " + DBConstants.POSITION + " = ?", new String[] {date, position});

    }

    public String getInformation(String date, String position) {
        if (db == null) db = this.getWritableDatabase();

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
        if (db == null) db = this.getWritableDatabase();
        db.delete(DBConstants.TABLE_ED_NAME, null, null);

    }

    public void closeDB() {
        if (db == null) db.close();
    }


}
