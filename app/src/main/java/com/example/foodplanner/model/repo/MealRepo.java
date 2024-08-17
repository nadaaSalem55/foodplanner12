package com.example.foodplanner.model.repo;

import com.example.foodplanner.api.MealCallBack;
import com.example.foodplanner.model.dto.Country;
import com.example.foodplanner.model.dto.Ingredient;
import com.example.foodplanner.model.dto.MealsItem;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface MealRepo {
   void getRandomMeal(MealCallBack mealCallBack);
    Flowable<List<MealsItem>> getAllFavMeals();
    Completable deleteFromRemoteAndLocal(MealsItem mealsItem);
    Completable deletePlannedMealRemoteAndLocal(MealsItem mealsItem);
    Completable insertMealToFavRemoteAndLocal(MealsItem mealsItem);
    Completable insertMealToWeeklyPlanRemoteAndLocal(MealsItem mealsItem);
    Flowable<List<MealsItem>> fetchAndSavePlannedMealsFromRemote(String date);
    Flowable<List<MealsItem>> getAllPlannedMeals(String date);
    Observable<List<Ingredient>> getIngredients();
    Observable<List<MealsItem>> getMealsByIngredient(String ingredient);
    @NonNull Observable<List<MealsItem>> getMealByCategory(String category);
    Observable<Object> getMealById(String mealId);
    Observable<List<Country>>getCountries();
    Observable<List<MealsItem>> getMealsByCountry(String country);
    Observable<List<MealsItem>> searchMeals(String name);
    Observable<AuthResult> signUpWithGoogle(String idToken);
    Single<FirebaseUser> signInWithGoogle(GoogleSignInAccount account);
 //   Observable<FirebaseUser> signInWithGoogle(Activity activity);
}
