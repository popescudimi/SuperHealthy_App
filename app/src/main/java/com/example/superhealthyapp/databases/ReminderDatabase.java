package com.example.superhealthyapp.databases;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.superhealthyapp.adapters.ReminderViewModel;

public class ReminderDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reminder";
    private static final int DATABASE_VERSION = 4;
    public static final String KEY_ID = "_id";
    public static final String TITLE = "title";
    public static final String TYPE = "type";
    public static final String TABLE_NAME = "tasks";
    public static final String DETAIL = "detail";
    public static final String DATE = "date";
    public static final String TIME = "time";
    private static final String REPEAT = "repeat";
    private static final String REPEAT_NO = "repeat_no";
    private static final String REPEAT_TYPE = "repeat_type";

    public ReminderDatabase(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDB = "create table if not exists " + TABLE_NAME + " ( "
                + KEY_ID+ " integer primary key autoincrement, "
                + TITLE + " text, "
                + DETAIL + " text, "
                + TYPE + " text, "
                + TIME + " text, "
                + DATE + " text)";
        db.execSQL(createDB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      /*  if (oldVersion >= newVersion)
            return;
        else {
        */

        db.execSQL("DROP TABLE " + TABLE_NAME);
        onCreate(db);
    }


    public int addNewReminder (ReminderViewModel reminder){

        getWritableDatabase().beginTransaction();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TITLE, reminder.getTitle());
        values.put(DETAIL, reminder.getDetails());
        values.put(DATE, reminder.getDate());
       values.put(REPEAT, reminder.getRepeat());
        values.put(REPEAT_NO, reminder.getRepeat());
        values.put(REPEAT_TYPE, reminder.getRepeatType());

        long row = db.insert(TABLE_NAME, null, values);

        db.close();

        return (int) row;


    }

    public int getRemindersCount(){
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);
        cursor.close();

        return cursor.getCount();
    }



}
