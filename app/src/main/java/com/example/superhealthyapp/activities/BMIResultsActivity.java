package com.example.superhealthyapp.activities;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;

import com.example.superhealthyapp.R;
import com.example.superhealthyapp.activities.MainActivity;
import com.example.superhealthyapp.adapters.BMIViewModel;


import java.text.DecimalFormat;

public class BMIResultsActivity extends BaseActivity implements View.OnClickListener {

    private TextView textViewBMIIndex, textViewBMIResult, textViewRequiredAmount, textViewBMITips;
    private NumberPicker numberPickerWeight, numberPickerHeight;
    private Button buttonProceed;
    private ViewGroup transition;
    String bmiResult;
    String[] bmiTipsArray;
    BMICategory bmiCategory = new BMICategory();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        initializeBMI();
    }

    private void initializeBMI() {
        initializeParameters();
        initializeNumberPickers();
        initializeButtons();
        transition = findViewById(R.id.transition);
    }

    private void initializeButtons() {
        Button buttonCalculate = findViewById(R.id.btnBMISubmit);
        buttonCalculate.setOnClickListener(this);
        buttonProceed = findViewById(R.id.btnBMIProceed);
        buttonProceed.setVisibility(View.GONE);
        buttonProceed.setOnClickListener(this);
    }

    private void initializeParameters() {
        //   textViewBMITips = findViewById(R.id.bmiValuesLinear);
        textViewBMIIndex = findViewById(R.id.txtBMIValue);
        textViewBMIResult = findViewById(R.id.txtResultBMI);
        textViewRequiredAmount = findViewById(R.id.txtRequiredAmount);
    }

    private void initializeNumberPickers() {
        numberPickerWeight = findViewById(R.id.numberPickerWeight);
        numberPickerHeight = findViewById(R.id.numberPickerHeight);
        numberPickerWeight.setMinValue(20);
        numberPickerWeight.setMaxValue(150);
        numberPickerWeight.setValue(75);
        numberPickerHeight.setMinValue(100);
        numberPickerHeight.setMaxValue(200);
        numberPickerHeight.setValue(180);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnBMISubmit:
                initializeBMICalculator();
                initializeAnimation();
                break;
            case R.id.btnBMIProceed:
                switchActivity();
                this.finish();
                break;
        }
    }

    private void initializeAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(transition);
        }
        buttonProceed.setVisibility(View.VISIBLE);
    }



    private void categoryType() {
        String bmiResult;
        String[] bmiTipsArray;
        TextView textViewBMITips = null;

        bmiTipsArray = getResources().getStringArray(R.array.tips_array);
        bmiResult = "bmiResult";
        double result = Double.parseDouble(bmiResult);
        if (result < 15) {
            textViewBMITips.setText(bmiTipsArray[0]);
        } else if (result >= 15 && result <= 16) {
            textViewBMITips.setText(bmiTipsArray[0]);
        } else if (result >= 16 && result <= 18.5) {
            textViewBMITips.setText(bmiTipsArray[1]);
        } else if (result >= 18.5 && result <= 25) {
            textViewBMITips.setText(bmiTipsArray[2]);
        } else if (result >= 25 && result <= 30) {
            textViewBMITips.setText(bmiTipsArray[3]);
        } else if (result >= 30 && result <= 35) {
            textViewBMITips.setText(bmiTipsArray[4]);
        } else if (result >= 35 && result <= 50) {
            textViewBMITips.setText(bmiTipsArray[4]);
        } else
            textViewBMITips.setText(bmiTipsArray[4]);
    }





    private void switchActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }





    private void initializeBMICalculator() {
        int weight, height;

        weight =(numberPickerWeight.getValue());
        height =(numberPickerHeight.getValue());

        BMIViewModel bmiCalc;
        bmiCalc = new BMIViewModel(weight, height);
        double bmi = bmiCalc.calculateBMI(bmiCalc.getCurrentWeight(), bmiCalc.getCurrentHeight());
        StringBuilder bmiIndex = new StringBuilder("BMI : " + String.format("%.2f", bmi));

        StringBuilder bmiResult = new StringBuilder(bmiCategory.findCategory(bmiCalc.calculateBMI(
                bmiCalc.getCurrentWeight(), bmiCalc.getCurrentHeight()),getApplicationContext()));

        double requiredAmount =  bmiCalc.calculcateRequiredAmountWater(bmiCalc.getCurrentWeight());
        StringBuilder requiredAmountText = new StringBuilder().append(getString(R.string.required_amount_of_water))
                .append(String.format(" %.2f",requiredAmount)).append(getString(R.string.liter));

        float amountWater = (float) bmiCalc.calculcateRequiredAmountWater(bmiCalc.getCurrentWeight());
        SharedPreferences.Editor editor = getSharedPreferences("requiredAmount", MODE_PRIVATE).edit();
        editor.putFloat("amountWater",amountWater);
        editor.apply();

        textViewBMIResult.setText(bmiResult);
        textViewBMIIndex.setText(bmiIndex);
        textViewRequiredAmount.setText(requiredAmountText);
    }
}