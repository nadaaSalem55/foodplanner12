package com.example.foodplanner.api;

import com.example.foodplanner.model.dto.MealsItem;

import java.util.List;

public interface CategoryMealCallBack {
    void onSuccessCategoryMeal(List<MealsItem> mealsItems);
    void onFailureCategoryMeal(String errorMessage);

    void onSuccessByMealId(MealsItem mealsItem);
    void onFailureByMealId(String errorMessage);
}
