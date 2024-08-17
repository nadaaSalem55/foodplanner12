package com.example.foodplanner.view.bottom_sheet;

import com.example.foodplanner.model.dto.MealsItem;

import java.util.List;

public interface BottomSheetView {
    void showMeals(List<MealsItem> meals);

    void showError(String message);

    void onGetMealByIdSuccess(Object meal);

    void onGetMealByIdFail(String localizedMessage);

    void onPlanMealSuccess();

    void onPlanMealFail(String localizedMessage);
}
