package com.example.foodplanner.model.repo.remote;

import com.example.foodplanner.api.MealCallBack;
import com.example.foodplanner.model.dto.MealsItem;

public interface RandomMealRemoteDataSource {
    MealsItem getMeal(MealCallBack mealCallBack);
}
