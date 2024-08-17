package com.example.foodplanner.view.fav_meal;

import com.example.foodplanner.model.dto.MealsItem;

import java.util.List;

public interface FavoriteView {
    public void onSuccessDeleteFromFav();
    public void onFailDeleteFromFav(String error);
    void onGetAllFavoriteMeals(List<MealsItem> favoriteMeals);
    void onGetAllFavoriteMealsError(String errorMessage);
    void onGetAllFavoriteFireStoreMeals(List<MealsItem> favoriteMeals);
    void onGetAllFavoriteFireStoreMealsError(String errorMessage);

}
