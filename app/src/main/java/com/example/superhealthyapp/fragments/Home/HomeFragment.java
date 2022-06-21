package com.example.superhealthyapp.fragments.Home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.superhealthyapp.R;
import com.example.superhealthyapp.fragments.BaseFragment;
import com.example.superhealthyapp.activities.MainActivity;
import com.ncapdevi.fragnav.FragNavController;

import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends BaseFragment implements View.OnClickListener,View.OnLongClickListener {

    private TextView textViewRequiredAmount,textViewPedometerSummary,textViewFoodSummary,textViewReminderSummary;
    private ProgressBar progressBar;
    private RadioButton radioButtonRomanian,radioButtonEnglish;
    private SharedPreferences dailySteps,preferences,sharedPreferences,currentAmountWater;
    private ImageView imageViewWaterSummary;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeLocale();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        initializeSharedPreferences();
        initializePedometerProgressCalculator();
        initializeWaterFragmentProgress();
        initializeFoodCalorySummary();
        initializeReminderSummary();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.imageViewLanguagePreference:
                initializeLanguagePicker();
                break;
            case R.id.imageViewInfo:
                initializeInfoDialog();
                break;
            case R.id.imageViewCaloriesSummary:
                switchToCaloriesFragment();
                break;
            case R.id.imageViewWaterSummary:
                switchToWaterFragment();
                break;
            case R.id.imageViewReminderSummary:
                switchToReminderFragment();
                break;
            case R.id.imageViewPedometerSummary:
                switchToPedometerFragment();
                break;
                default:
                    break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.imageViewWaterSummary:
                waterCleanerDialog();
                break;
            case R.id.imageViewCaloriesSummary:
                caloriesCleanerDialog();
                break;
        }
        return false;
    }

    private void initializeSharedPreferences(){
        if(getActivity() != null){
             preferences = getActivity().getSharedPreferences("requiredAmount", Context.MODE_PRIVATE);
             sharedPreferences = getActivity().getSharedPreferences("pedometer",Context.MODE_PRIVATE);
             dailySteps = getActivity().getSharedPreferences("dailySteps",Context.MODE_PRIVATE);
             currentAmountWater = getActivity().getSharedPreferences("waterDailyGoal",Context.MODE_PRIVATE);
        }
    }

    private void initializeViews(View view){
        if(getActivity() != null) {
            ((MainActivity) getActivity()).updateToolbarTitle(getString(R.string.title_home));
            ImageView imageViewLanguagePreference = view.findViewById(R.id.imageViewLanguagePreference);
            ImageView imageViewInfo = view.findViewById(R.id.imageViewInfo);
            ImageView imageViewCaloriesSummary = view.findViewById(R.id.imageViewCaloriesSummary);
            imageViewWaterSummary = view.findViewById(R.id.imageViewWaterSummary);
            ImageView imageViewReminderSummary = view.findViewById(R.id.imageViewReminderSummary);
            ImageView imageViewPedometerSummary = view.findViewById(R.id.imageViewPedometerSummary);
            textViewRequiredAmount = view.findViewById(R.id.textViewRequiredAmount);
            textViewFoodSummary = view.findViewById(R.id.textViewFoodSummary);
            textViewPedometerSummary = view.findViewById(R.id.textViewPedometerSummary);
            textViewReminderSummary = view.findViewById(R.id.textViewReminderSummary);
            progressBar = view.findViewById(R.id.determinateBar);
            imageViewCaloriesSummary.setOnLongClickListener(this);
            imageViewWaterSummary.setOnLongClickListener(this);
            imageViewLanguagePreference.setOnClickListener(this);
            imageViewInfo.setOnClickListener(this);
            imageViewCaloriesSummary.setOnClickListener(this);
            imageViewWaterSummary.setOnClickListener(this);
            imageViewReminderSummary.setOnClickListener(this);
            imageViewPedometerSummary.setOnClickListener(this);
        }
    }

    private void initializePedometerProgressCalculator(){
        int totalSteps = dailySteps.getInt("dailySteps", 0);
        int value = sharedPreferences.getInt("goal", 1000);
        int percentage = (int) (((double) totalSteps / (double) value) * 100);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressBar.setProgress(percentage,true);
        } else {
            progressBar.setProgress(percentage);
        }
        StringBuilder pedometerSummary = new StringBuilder().append(totalSteps).append(" / ").append(value);

        textViewPedometerSummary.setText(pedometerSummary);
    }

    private void initializeLanguagePicker(){
        if(getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater layoutInflater = getActivity().getLayoutInflater();
            @SuppressLint("InflateParams")
            View languagePickerView = layoutInflater.inflate(R.layout.language_picker,null,false);
            builder.setView(languagePickerView);
            builder.setTitle(getString(R.string.pick_language));
            radioButtonEnglish = languagePickerView.findViewById(R.id.imageViewEnglish);
            radioButtonRomanian = languagePickerView.findViewById(R.id.imageViewRomanian);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Objects.equals(getActivity().getSharedPreferences("languagePreferences", Context.MODE_PRIVATE)
                        .getString("selectedLanguage", "ro"), "ro"))
                            radioButtonRomanian.setChecked(true);
                else
                    radioButtonEnglish.setChecked(true);
            }
            builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(getActivity() != null) {
                            if (radioButtonRomanian.isChecked()) {
                                setLocale("ro");
                                Toast.makeText(getContext(), getString(R.string.restart_device), Toast.LENGTH_LONG).show();
                                } else if (radioButtonEnglish.isChecked()) {
                                setLocale("en");
                                Toast.makeText(getContext(), getString(R.string.restart_device), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        }

    private void initializeInfoDialog(){
        if (getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            @SuppressLint("InflateParams") final View preferencesView = inflater.inflate(R.layout.information,null,false);
            builder.setView(preferencesView);
            TextView textViewAbout = preferencesView.findViewById(R.id.textViewInfo);
            StringBuilder builder1 = new StringBuilder("Thanks for using Super Healthy! This Info box is considered to be replaced by further RFE's");

            textViewAbout.setText(builder1);
            builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }
    private void initializeWaterFragmentProgress(){

      //  DecimalFormat twoDecimalPlaces = new DecimalFormat(".##");
        float amount = preferences.getFloat("amountWater", 0);
        float current = currentAmountWater.getFloat("waterDailyGoal",0);
        if (current < 0 )
            current = 0;


        StringBuilder waterSummary = new StringBuilder().append(String.format(" %.2f", current)).append(" / ").append(String.format(" %.2f",amount)).append(" L");

        textViewRequiredAmount.setText(waterSummary);
            if ((double)current / (double)amount >= 0 && (double)current / (double)amount <= 0.25){
                imageViewWaterSummary.setImageResource(R.drawable.water_icon);
            }else if ((double)current / (double)amount >= 0.26 && (double)current / (double)amount <= 0.50){
                imageViewWaterSummary.setImageResource(R.drawable.water_icon);
            }else if ((double)current / (double)amount >= 0.51 && (double)current / (double)amount <= 0.75){
                imageViewWaterSummary.setImageResource(R.drawable.water_icon);
            }else if ((double)current / (double)amount >= 0.76 && (double)current / (double)amount <= 1){
                imageViewWaterSummary.setImageResource(R.drawable.water_icon);
            }else {
                imageViewWaterSummary.setImageResource(R.drawable.water_icon);
            }

    }

    private void initializeFoodCalorySummary() {
        if (getActivity() != null){
            SharedPreferences preferences = getActivity().getSharedPreferences("foodCalory",Context.MODE_PRIVATE);
            int preferencesTotal = preferences.getInt("totalCalory",0);
            StringBuilder builder = new StringBuilder(getString(R.string.calories)).append(" : ").append(preferencesTotal);
            textViewFoodSummary.setText(builder);
        }
    }
    private void initializeReminderSummary(){
        if (getActivity() != null){
            SharedPreferences preferences = getActivity().getSharedPreferences("reminderCount",Context.MODE_PRIVATE);
            int reminderCount = preferences.getInt("reminderCount",0);
            StringBuilder builder = new StringBuilder(getString(R.string.reminder)).append(" : ").append(reminderCount);
            textViewReminderSummary.setText(builder);
        }
    }

    private void switchToCaloriesFragment(){
        if (getActivity() != null){
            ((MainActivity) getActivity()).switchTab(FragNavController.TAB2);
        }
    }
    private void switchToWaterFragment(){
        if (getActivity() != null){
            ((MainActivity) getActivity()).switchTab(FragNavController.TAB3);
        }
    }
    private void switchToPedometerFragment(){
        if (getActivity() != null){
            ((MainActivity) getActivity()).switchTab(FragNavController.TAB4);
        }
    }
    private void switchToReminderFragment(){
        if (getActivity() != null){
            ((MainActivity) getActivity()).switchTab(FragNavController.TAB5);
        }
    }
    private void initializeLocale(){
            if (getActivity() != null) {
                SharedPreferences preferences = getActivity().getSharedPreferences("languagePreferences", Context.MODE_PRIVATE);
                String language = preferences.getString("selectedLanguage","ro");
                setLocale(language);
        }
    }
    private void setLocale(String lang){
        if (getContext() != null) {
            Locale locale = new Locale(lang);
            Configuration configuration = getResources().getConfiguration();
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            configuration.locale = locale;
            getResources().updateConfiguration(configuration, displayMetrics);
            SharedPreferences.Editor editor = getContext().getSharedPreferences("languagePreferences", Context.MODE_PRIVATE).edit();
            editor.putString("selectedLanguage", lang);
            editor.apply();
        }
    }
    private void waterCleanerDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.clean));
        builder.setMessage(getString(R.string.sure_to_clean));
        builder.setNegativeButton(getString(R.string.negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (getContext() != null) {
                    if (getActivity() != null)
                            getContext().getSharedPreferences("dailyGoal", Context.MODE_PRIVATE)
                                .edit().clear().apply();
                    }
                }
        });
        builder.show();
    }
    private void caloriesCleanerDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.clean));
        builder.setMessage(getString(R.string.sure_to_clean));
        builder.setNegativeButton(getString(R.string.negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (getContext() != null) {
                    if (getActivity() != null)
                            getContext().getSharedPreferences("foodCalory", Context.MODE_PRIVATE)
                                    .edit().clear().apply();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}