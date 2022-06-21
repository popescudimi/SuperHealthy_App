package com.example.superhealthyapp.fragments.Pedometer;



import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.superhealthyapp.BuildConfig;
import com.example.superhealthyapp.activities.MainActivity;
import com.example.superhealthyapp.fragments.BaseFragment;
import com.example.superhealthyapp.databases.StepCounterDatabase;
import com.example.superhealthyapp.managers.DateManager;
import com.example.superhealthyapp.R;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PedometerFragment extends BaseFragment
                            implements SensorEventListener{

    private TextView textViewStepsCount;
    private TextView textViewTotalSteps;
    private TextView textViewAverageSteps;
    private PieModel sliceGoal, sliceCurrent;
    private PieChart pieChart;
    private static final int MINIMUM_PICKER_VALUE = 1000;
    private static final int MAXIMUM_PICKER_VALUE = 1000;
    private static final int DEFAULT_GOAL = 500;
    private int todayOffset, total_start, goal, since_boot, total_days;
    @SuppressLint("ConstantLocale")
    public final static NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
    private boolean showSteps = true;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (Build.VERSION.SDK_INT >= 26) {
            getActivity().startService(new Intent(getActivity(), SensorListener.class));
        }
    }

    @Override
    public View onCreateView(final @NonNull LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_pedometer, null);
        if(getActivity() != null) {
          ((MainActivity) getActivity()).updateToolbarTitle(getString(R.string.title_pedometer));

            textViewStepsCount = (TextView) v.findViewById(R.id.steps);
            textViewTotalSteps = (TextView) v.findViewById(R.id.total);
            textViewAverageSteps = (TextView) v.findViewById(R.id.average);
            pieChart = v.findViewById(R.id.graph);

            // slice for the steps taken today
           sliceCurrent = new PieModel("", 0, Color.parseColor("#0080ff"));
           pieChart.addPieSlice(sliceCurrent);

           //   // slice for the "missing" steps until reaching the goal
           sliceGoal = new PieModel("", DEFAULT_GOAL, Color.parseColor("#0000ff"));
           pieChart.addPieSlice(sliceGoal);


           String target = String.valueOf(getActivity().getSharedPreferences("pedometer",Context.MODE_PRIVATE)
                   .getInt("goal",1000));
           TextView textViewtarget = v.findViewById(R.id.textViewTarget);
           textViewtarget.setText(target);

           pieChart.setDrawValueInPie(false);
           pieChart.setUsePieRotation(true);
           pieChart.startAnimation();

        }
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
            SensorManager sm =
                    (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            sm.unregisterListener(this);
        StepCounterDatabase db = StepCounterDatabase.getInstance(getActivity());
        db.saveCurrentSteps(since_boot);
        db.close();
    }


    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
        if(getActivity()!=null) {
            StepCounterDatabase db = StepCounterDatabase.getInstance(getActivity());

            if (BuildConfig.DEBUG) db.logState();
            todayOffset = db.getSteps(DateManager.getToday());
            // // read todays offset
            SharedPreferences prefs =
                    getActivity().getSharedPreferences("pedometer", Context.MODE_PRIVATE);

            goal = prefs.getInt("goal", DEFAULT_GOAL);
            since_boot = db.getCurrentSteps();
            int pauseDifference = since_boot - prefs.getInt("pauseCount", since_boot);

            // register a sensorlistener to live update the UI if a step is taken

            SensorManager sm = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

            if (sensor == null) {
                    new AlertDialog.Builder(getActivity()).setTitle(R.string.no_sensor)
                            .setMessage(R.string.no_sensor_explain)
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(final DialogInterface dialogInterface) {
                                    getActivity().finish();
                                }
                            }).setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();

            } else {
                sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI, 0);
            }
            since_boot -= pauseDifference;

            total_start = db.getTotalWithoutToday();
            total_days = db.getDays();

            db.close();
            updatePie();
            updateBars();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // won't happen
    }



    @Override
    public void onSensorChanged(final SensorEvent event) {

        if (event.values[0] > Integer.MAX_VALUE || event.values[0] == 0) {
            return;
        }
        if (todayOffset == Integer.MIN_VALUE) {
            // no values for today
            // we dont know when the reboot was, so set todays steps to 0 by
            // initializing them with -STEPS_SINCE_BOOT
            todayOffset = -(int) event.values[0];
            StepCounterDatabase db = StepCounterDatabase.getInstance(getActivity());
            db.insertNewDay(DateManager.getToday(), (int) event.values[0]);
            db.close();
        }
        since_boot = (int) event.values[0];
        updatePie();
    }

    private void updatePie() {
      //  if (BuildConfig.DEBUG) Logger.log("UI - update steps: " + since_boot);
        int steps_today = Math.max(todayOffset + since_boot, 0);
        sliceCurrent.setValue(steps_today);
      //      goal not reached yet
        if (goal - steps_today > 0) {
            if (pieChart.getData().size() == 1) {
                // can happen if the goal value was changed: old goal value was
                // reached but now there are some steps missing for the new goal
                pieChart.addPieSlice(sliceGoal);
            }
            sliceGoal.setValue(goal - steps_today);
        } else {
            pieChart.clearChart();
            pieChart.addPieSlice(sliceCurrent);
        }
        pieChart.update();
        if (showSteps) {
            textViewStepsCount.setText(formatter.format(steps_today));
            textViewTotalSteps.setText(formatter.format(total_start + steps_today));
            textViewAverageSteps.setText(formatter.format((total_start + steps_today) / total_days));
        }if(getActivity() != null){
            SharedPreferences.Editor sharedPreferences =
                    getActivity().getSharedPreferences("dailySteps",Context.MODE_PRIVATE).edit();
            sharedPreferences.putInt("dailySteps",steps_today);
            sharedPreferences.apply();

        }
    }

    private void updateBars() {
        if (getView() != null) {
            SimpleDateFormat df = new SimpleDateFormat("E", Locale.getDefault());
            BarChart barChart = getView().findViewById(R.id.bargraph);
            if (barChart.getData().size() > 0) barChart.clearChart();
            int steps;

            barChart.setShowDecimal(!showSteps); // show decimal in distance view only
            BarModel bm;
            StepCounterDatabase db = StepCounterDatabase.getInstance(getActivity());
            List<Pair<Long, Integer>> last = db.getLastEntries(8);
            db.close();
            for (int i = last.size() - 1; i > 0; i--) {
                Pair<Long, Integer> current = last.get(i);
                steps = current.second;
                if (steps > 0) {
                    bm = new BarModel(df.format(new Date(current.first)), 0,
                            steps > goal ? Color.parseColor("#0000ff") : Color.parseColor("#0080ff"));
                    if (showSteps) {
                        bm.setValue(steps);
                    }
                    barChart.addBar(bm);
                }
            }
            if (barChart.getData().size() > 0) {
                barChart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Dialog_Statistics.getDialog(getActivity(), since_boot).show();
                    }
                });
                barChart.startAnimation();
            } else {
                barChart.setVisibility(View.GONE);
            }
        }
    }






    }
