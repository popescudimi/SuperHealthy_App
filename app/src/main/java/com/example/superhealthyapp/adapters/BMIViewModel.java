package com.example.superhealthyapp.adapters;



public class BMIViewModel {

        private double currentWeight;
        private double currentHeight;

        public BMIViewModel(double currentWeight, double currentHeight){
            this.currentWeight = currentWeight;
            this.currentHeight = currentHeight;
        }

        public double getCurrentWeight() {
            return currentWeight;
        }

        public double getCurrentHeight() {
            return currentHeight;
        }

        public double calculateBMI(double currentWeight,double currentHeight){
            return (currentWeight/(Math.pow(currentHeight,2)/10000));

        }

        public double calculcateRequiredAmountWater (double currentWeight) {
            Double requiredAmount = 0.033 * currentWeight;
                return requiredAmount;
        }

    }


