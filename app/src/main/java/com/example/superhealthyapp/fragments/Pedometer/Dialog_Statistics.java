package com.example.superhealthyapp.fragments.Pedometer;

/* Source: https://github.com/j4velin/Pedometer/blob/master/src/main/java/de/j4velin/pedometer/ui/Dialog_Statistics.java
 */


import android.app.Dialog;
import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;


import com.example.superhealthyapp.databases.StepCounterDatabase;
import com.example.superhealthyapp.managers.DateManager;
import com.example.superhealthyapp.R;


public class Dialog_Statistics {

    public static Dialog getDialog(final Context c, int since_boot) {
        final Dialog d = new Dialog(c);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.statistics);
        d.findViewById(R.id.close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        StepCounterDatabase db = StepCounterDatabase.getInstance(c);

        Pair<Date, Integer> record = db.getRecordData();

        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(DateManager.getToday());
        int daysThisMonth = date.get(Calendar.DAY_OF_MONTH);

        date.add(Calendar.DATE, -6);

        int thisWeek = db.getSteps(date.getTimeInMillis(), System.currentTimeMillis()) + since_boot;

        date.setTimeInMillis(DateManager.getToday());
        date.set(Calendar.DAY_OF_MONTH, 1);
        int thisMonth = db.getSteps(date.getTimeInMillis(), System.currentTimeMillis()) + since_boot;

        ((TextView) d.findViewById(R.id.record)).setText(
                PedometerFragment.formatter.format(record.second) + " @ "
                        + java.text.DateFormat.getDateInstance().format(record.first));

        ((TextView) d.findViewById(R.id.totalthisweek)).setText(PedometerFragment.formatter.format(thisWeek));

        ((TextView) d.findViewById(R.id.averagethisweek)).setText(PedometerFragment.formatter.format(thisWeek / 7));
        // ((TextView) d.findViewById(R.id.averagethismonth)).setText(PedometerFragment.formatter.format(thisMonth / daysThisMonth));

        db.close();

        return d;
    }

}

