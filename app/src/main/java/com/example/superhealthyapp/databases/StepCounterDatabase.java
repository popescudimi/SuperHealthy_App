package com.example.superhealthyapp.databases;

/**
 Sources used below
 1) https://www.programmersought.net/article/329313409.html
 2) https://github.com/pubnative/easy-steps-android/blob/master/app/src/main/java/net/pubnative/easysteps/Database.java
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import com.example.superhealthyapp.BuildConfig;
import com.example.superhealthyapp.managers.DateManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class StepCounterDatabase extends SQLiteOpenHelper {
    private final static String DB_NAME = "steps";
    private final static String DB_NAME_ENCRYPT = "steps_encrypt";
    private final static int DB_VERSION = 2;

    private static StepCounterDatabase instance;
    private static final AtomicInteger openCounter = new AtomicInteger();
   // DateManager dateManager;

    private StepCounterDatabase(final Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized StepCounterDatabase getInstance(final Context c) {
        if (instance == null) {
            instance = new StepCounterDatabase(c.getApplicationContext());
        }
        openCounter.incrementAndGet();
        return instance;
    }

    @Override
    public void close() {
        if (openCounter.decrementAndGet() == 0) {
            super.close();
        }
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DB_NAME + " (date INTEGER, steps INTEGER)");
        db.execSQL("CREATE TABLE " + DB_NAME_ENCRYPT + "(date INTEGER, steps TEXT, sensor TEXT, off INTEGER)");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            // drop PRIMARY KEY constraint
            db.execSQL("CREATE TABLE " + DB_NAME + "2 (date INTEGER, steps INTEGER)");
            db.execSQL("INSERT INTO " + DB_NAME + "2 (date, steps) SELECT date, steps FROM " +
                    DB_NAME);
            db.execSQL("DROP TABLE " + DB_NAME);
            db.execSQL("ALTER TABLE " + DB_NAME + "2 RENAME TO " + DB_NAME + "");
        }
    }


    public Cursor query(final String[] columns, final String selection,
                        final String[] selectionArgs, final String groupBy, final String having,
                        final String orderBy, final String limit) {
        return getReadableDatabase()
                .query(DB_NAME, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }



    public void insertNewDay(long date, int steps) {
        getWritableDatabase().beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("date", date);
            values.put("steps", -steps);

            Cursor c = getReadableDatabase().query(DB_NAME, new String[]{"date"}, "date = ?",
                    new String[]{String.valueOf(date)}, null, null, null);
            if (c.getCount() == 0 && steps >= 0) {

                addToLastEntry(steps);

                getWritableDatabase().insert(DB_NAME, null, values);
            }
            c.close();
            if (BuildConfig.DEBUG) {
                logState();
            }
            getWritableDatabase().setTransactionSuccessful();
        } finally {
            getWritableDatabase().endTransaction();
        }
    }

   // Adds the given number of steps to the last entry in the database

    public void addToLastEntry(int steps) {
        getWritableDatabase().execSQL("UPDATE " + DB_NAME + " SET steps = steps + " + steps +
                " WHERE date = (SELECT MAX(date) FROM " + DB_NAME + ")");
    }

    public boolean insertDayFromBackup(long date, int steps) {
        getWritableDatabase().beginTransaction();
        boolean newEntryCreated = false;
        try {
            ContentValues values = new ContentValues();
            values.put("steps", steps);
            int updatedRows = getWritableDatabase()
                    .update(DB_NAME, values, "date = ?", new String[]{String.valueOf(date)});
            if (updatedRows == 0) {
                values.put("date", date);
                getWritableDatabase().insert(DB_NAME, null, values);
                newEntryCreated = true;
            }
            getWritableDatabase().setTransactionSuccessful();
        } finally {
            getWritableDatabase().endTransaction();
        }
        return newEntryCreated;
    }


    // Writes the current steps database to the log

    public void logState() {
        if (BuildConfig.DEBUG) {
            Cursor c = getReadableDatabase()
                    .query(DB_NAME, null, null, null, null, null, "date DESC", "7");
            c.close();
        }
    }


    // Get the total of steps taken without today's value
    public int getTotalWithoutToday() {
        Cursor c = getReadableDatabase()
                .query(DB_NAME, new String[]{"SUM(steps)"}, "steps > 0 AND date > 0 AND date < ?",
                        new String[]{String.valueOf(DateManager.getToday())}, null, null, null);
        c.moveToFirst();
        int re = c.getInt(0);
        c.close();
        return re;
    }


    // Get the maximum of steps walked in one day
    public int getRecord() {
        Cursor c = getReadableDatabase()
                .query(DB_NAME, new String[]{"MAX(steps)"}, "date > 0", null, null, null, null);
        c.moveToFirst();
        int re = c.getInt(0);
        c.close();
        return re;
    }

    public Pair<Date, Integer> getRecordData() {
        Cursor c = getReadableDatabase()
                .query(DB_NAME, new String[]{"date, steps"}, "date > 0", null, null, null,
                        "steps DESC", "1");
        c.moveToFirst();
        Pair<Date, Integer> p = new Pair<Date, Integer>(new Date(c.getLong(0)), c.getInt(1));
        c.close();
        return p;
    }

    // Get the number of steps taken for a specific date.
    public int getSteps(final long date) {
        Cursor c = getReadableDatabase().query(DB_NAME, new String[]{"steps"}, "date = ?",
                new String[]{String.valueOf(date)}, null, null, null);
        c.moveToFirst();
        int re;
        if (c.getCount() == 0) re = Integer.MIN_VALUE;
        else re = c.getInt(0);
        c.close();
        return re;
    }

    // Gets the last num entries in descending order of date (newest first)
    public List<Pair<Long, Integer>> getLastEntries(int num) {
        Cursor c = getReadableDatabase()
                .query(DB_NAME, new String[]{"date", "steps"}, "date > 0", null, null, null,
                        "date DESC", String.valueOf(num));
        int max = c.getCount();
        List<Pair<Long, Integer>> result = new ArrayList<>(max);
        if (c.moveToFirst()) {
            do {
                result.add(new Pair<>(c.getLong(0), c.getInt(1)));
            } while (c.moveToNext());
        }
        return result;
    }

// Get the number of steps taken between 'start' and 'end' date
    public int getSteps(final long start, final long end) {
        Cursor c = getReadableDatabase()
                .query(DB_NAME, new String[]{"SUM(steps)"}, "date >= ? AND date <= ?",
                        new String[]{String.valueOf(start), String.valueOf(end)}, null, null, null);
        int re;
        if (c.getCount() == 0) {
            re = 0;
        } else {
            c.moveToFirst();
            re = c.getInt(0);
        }
        c.close();
        return re;
    }

    //  Removes all entries with negative values.
    void removeNegativeEntries() {
        getWritableDatabase().delete(DB_NAME, "steps < ?", new String[]{"0"});
    }

    // Removes invalid entries from the database ( for example steps >= 9999999)
    public void removeInvalidEntries() {
        getWritableDatabase().delete(DB_NAME, "steps >= ?", new String[]{"9999999"});
    }


   // Get the number of 'valid' days (= days with a step value > 0).
    private int getDaysWithoutToday() {
        Cursor c = getReadableDatabase()
                .query(DB_NAME, new String[]{"COUNT(*)"}, "steps > ? AND date < ? AND date > 0",
                        new String[]{String.valueOf(0), String.valueOf((DateManager.getToday()))}, null,
                        null, null);
        c.moveToFirst();
        int re = c.getInt(0);
        c.close();
        return re < 0 ? 0 : re;
    }

    // Get the number of 'valid' days (= days with a step value > 0).
    public int getDays() {
        // todays is not counted yet
        return this.getDaysWithoutToday() + 1;
    }

    // Saves the current 'steps since boot' sensor value in the database.
    public void saveCurrentSteps(int steps) {
        ContentValues values = new ContentValues();
        values.put("steps", steps);
        if (getWritableDatabase().update(DB_NAME, values, "date = -1", null) == 0) {
            values.put("date", -1);
            getWritableDatabase().insert(DB_NAME, null, values);
        }
        if (BuildConfig.DEBUG) {
        //    Logger.log("saving steps in db: " + steps);
        }
    }

    // Reads the latest saved value for the 'steps since boot' sensor value.
    public int getCurrentSteps() {
        int re = getSteps(-1);
        return re == Integer.MIN_VALUE ? 0 : re;
    }
}
