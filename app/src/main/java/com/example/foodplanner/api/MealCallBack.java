package com.example.foodplanner.api;

import com.example.foodplanner.model.dto.MealsItem;

public interface MealCallBack {
    void onSuccess(MealsItem mealsItem);
    void onFailure(String errorMessage);
}
