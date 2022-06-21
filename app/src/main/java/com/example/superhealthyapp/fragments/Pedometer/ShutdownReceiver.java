package com.example.superhealthyapp.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.superhealthyapp.fragments.Pedometer.SensorListener;
import com.example.superhealthyapp.managers.DateManager;
import com.example.superhealthyapp.databases.StepCounterDatabase;

public class ShutdownReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {

        context.startService(new Intent(context, SensorListener.class));
        context.getSharedPreferences("pedometer", Context.MODE_PRIVATE).edit()
                .putBoolean("correctShutdown", true).apply();

        StepCounterDatabase db = StepCounterDatabase.getInstance(context);
        if (db.getSteps(DateManager.getToday()) == Integer.MIN_VALUE) {
            int steps = db.getCurrentSteps();
            db.insertNewDay(DateManager.getToday(), steps);
        } else {
            db.addToLastEntry(db.getCurrentSteps());
        }
        db.close();
    }
}
