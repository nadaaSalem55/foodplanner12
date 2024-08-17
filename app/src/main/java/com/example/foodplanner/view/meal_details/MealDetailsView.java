package com.example.foodplanner.view.meal_details;

import com.example.foodplanner.model.dto.MealsItem;

public interface MealDetailsView {
    void onInsertFavSuccess();
    void onInsertFavError(String error);
    void onPlanMealSuccess();
    void onPlanMealFail(String error);
     void onSuccessDeleteFromFav();
     void onFailDeleteFromFav(String error);

    void showMeal(MealsItem mealsItem);

    void showError(String errorLoadingMeals);
}
