package com.example.superhealthyapp.fragments.Pedometer;

/*
Source: https://github.com/j4velin/Pedometer/blob/master/src/main/java/de/j4velin/pedometer/widget/WidgetUpdateService.java
 */

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.example.superhealthyapp.databases.StepCounterDatabase;
import com.example.superhealthyapp.managers.DateManager;

public class WidgetUpdateService extends JobIntentService {
    // **
    //     * Unique job ID for this service.
    //     */
    private static final int JOB_ID = 42;

    public static void enqueueUpdate(Context context) {
        enqueueWork(context, WidgetUpdateService.class, JOB_ID, new Intent());
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        StepCounterDatabase db = StepCounterDatabase.getInstance(this);
        int steps = Math.max(db.getCurrentSteps() + db.getSteps(DateManager.getToday()), 0);
        db.close();
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds =
                appWidgetManager.getAppWidgetIds(new ComponentName(this, Widget.class));
        for (int appWidgetId : appWidgetIds) {
            appWidgetManager
                    .updateAppWidget(appWidgetId, Widget.updateWidget(appWidgetId, this, steps));
        }
    }
}