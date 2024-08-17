package com.example.foodplanner.view.serach_meal;

import com.example.foodplanner.model.dto.CategoryResponse;
import com.example.foodplanner.model.dto.MealsItem;

import java.util.List;

public interface SearchMealView {

    void showCategorySuccessMessage(List<CategoryResponse.CategoriesItem> categoriesItems);

    void showCategoryErrorMessage(String error);

    void onMealByCategorySuccess(List<MealsItem> mealsItems);

    void onMealByCategoryFail(String localizedMessage);

    void showMeal(List<MealsItem> meals);

    void showError(String message);

    void showMealById(MealsItem o);
    void showMealsByCountryError(String localizedMessage);
}
