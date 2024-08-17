package com.example.foodplanner.api;


import com.example.foodplanner.model.dto.CategoryResponse;
import com.example.foodplanner.model.dto.IngredientResponse;
import com.example.foodplanner.model.dto.MealResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WebService {
    @GET("api/json/v1/1/random.php")
    Call<MealResponse> getRandomMeal();

    @GET("/api/json/v1/1/categories.php")
    Observable<CategoryResponse> getCategories();

   @GET("/api/json/v1/1/filter.php")
   Observable<MealResponse> getAllMealsByCategory(@Query("c") String category);

   @GET("/api/json/v1/1/lookup.php")
   Call<MealResponse> getMealById(@Query("i") String mealId);

    @GET("/api/json/v1/1/search.php")
    Observable<MealResponse> searchMealByName(@Query("s") String mealName);

    @GET("/api/json/v1/1/list.php?i=list")
    Observable<IngredientResponse> getIngredients();

    @GET("/api/json/v1/1/filter.php")
    Observable<MealResponse> getMealsBIngredient(@Query("i") String ingredient);

    @GET("/api/json/v1/1/list.php?a=list")
    Observable<MealResponse> getCountries();

    @GET("/api/json/v1/1/filter.php")
    Observable<MealResponse> getMealsByCountry(@Query("a") String country);

}
