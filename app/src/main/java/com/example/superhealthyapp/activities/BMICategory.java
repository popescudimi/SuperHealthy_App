package com.example.superhealthyapp.activities;

import android.content.Context;

public class BMICategory {


    public String findCategory(double result1, Context context) {


        String category;

        if (result1 < 15) {
            category = "Very Severely Underweight";
            //    textViewBMIResult.setText(category);
        } else if (result1 >= 15 && result1 <= 16) {
            category = "Severely Underweight";
            //   textViewBMIResult.setText(category);
        } else if (result1 >= 16 && result1 <= 18.5) {
            category = "Underweight";
            //   textViewBMIResult.setText(category);
        } else if (result1 >= 18.5 && result1 <= 25) {
            category = "Normal (Healthy weight)";
            //   textViewBMIResult.setText(category);
        } else if (result1 >= 25 && result1 <= 30) {
            category = "Overweight";
            //    textViewBMIResult.setText(category);
        } else if (result1 >= 30 && result1 <= 35) {
            category = "Moderately Obese";
            //   textViewBMIResult.setText(category);
        } else if (result1 >= 35 && result1 <= 50) {

            category = "Severely Obese";
            //   textViewBMIResult.setText(category);
        } else
            category = "Very Severely Obese";

        return category;

    }
}
