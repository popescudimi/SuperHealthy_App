package com.example.superhealthyapp.fragments.Pedometer;

/*
Source : https://github.com/j4velin/Pedometer/blob/master/src/main/java/de/j4velin/pedometer/widget/Widget.java
 */

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.RemoteViews;

import com.example.superhealthyapp.R;
import com.example.superhealthyapp.activities.MainActivity;



public class Widget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetUpdateService.enqueueUpdate(context);
    }

    static RemoteViews updateWidget(final int appWidgetId, final Context context, final int steps) {
        final SharedPreferences prefs =
                context.getSharedPreferences("Widgets", Context.MODE_PRIVATE);
        final PendingIntent pendingIntent = PendingIntent
                .getActivity(context, appWidgetId, new Intent(context, MainActivity.class), 0);

        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        views.setOnClickPendingIntent(R.id.widget, pendingIntent);
        views.setTextColor(R.id.widgetsteps, prefs.getInt("color_" + appWidgetId, Color.WHITE));
        views.setTextViewText(R.id.widgetsteps, String.valueOf(steps));
        views.setTextColor(R.id.widgettext, prefs.getInt("color_" + appWidgetId, Color.WHITE));
        views.setInt(R.id.widget, "setBackgroundColor",
                prefs.getInt("background_" + appWidgetId, Color.TRANSPARENT));
        return views;
    }

}