package com.example.aihub.dataobjects;

public class FoodVisionResponse {
    String model_prediction;
    String model_prediction_confidence_score;

    public String getModel_prediction() {
        return model_prediction;
    }

    public void setModel_prediction(String model_prediction) {
        this.model_prediction = model_prediction;
    }

    public String getModel_prediction_confidence_score() {
        return model_prediction_confidence_score;
    }

    public void setModel_prediction_confidence_score(String model_prediction_confidence_score) {
        this.model_prediction_confidence_score = model_prediction_confidence_score;
    }
}
