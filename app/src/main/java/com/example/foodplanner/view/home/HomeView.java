package com.example.foodplanner.view.home;

import com.example.foodplanner.model.dto.CategoryResponse;
import com.example.foodplanner.model.dto.Country;
import com.example.foodplanner.model.dto.Ingredient;
import com.example.foodplanner.model.dto.MealsItem;

import java.util.List;

public interface HomeView {
    void showSuccessMessage(MealsItem mealsItem);
    void showErrorMessage(String error);
    void showCategorySuccessMessage(List<CategoryResponse.CategoriesItem> categoriesItems);
    void showCategoryErrorMessage(String error);

    void showIngredientSuccessMessage(List<Ingredient> ingredients);

    void showIngredientsErrorMessage(String localizedMessage);

    void showMealsByIngredientSuccess(List<MealsItem> mealsItems);

    void showMealsByIngredientError(String localizedMessage);

    void onMealByCategorySuccess(List<MealsItem> mealsItems);

    void onMealByCategoryFail(String localizedMessage);

    void showCountriesSuccessMessage(List<Country> countries);

    void showCountriesErrorMessage(String localizedMessage);

    void showMealsByCountryError(String localizedMessage);

    void showMealsByCountrySuccess(List<MealsItem> mealsItems);
}
