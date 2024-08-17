package com.example.foodplanner.presenter;

import com.example.foodplanner.model.dto.MealsItem;
import com.example.foodplanner.model.repo.MealRepo;
import com.example.foodplanner.view.fav_meal.FavoriteView;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public interface FavMealPresenter {
     void getMeals();
     void deleteFavMeals(MealsItem mealItem);

     class FavMealPresenterImp implements FavMealPresenter{
        MealRepo mealRepo;
        FavoriteView favoriteView;

        public FavMealPresenterImp(MealRepo mealRepo, FavoriteView favoriteView) {
            this.mealRepo = mealRepo;
            this.favoriteView = favoriteView;
        }

        @Override
        public void getMeals() {
            mealRepo.getAllFavMeals().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(favMeals->favoriteView.onGetAllFavoriteFireStoreMeals((List<MealsItem>)  favMeals),
                            error-> favoriteView.onGetAllFavoriteMealsError(error.getLocalizedMessage()));

        }

        @Override
        public void deleteFavMeals(MealsItem mealItem) {
            mealRepo.deleteFromRemoteAndLocal(mealItem)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> favoriteView.onSuccessDeleteFromFav(),
                            error -> favoriteView.onFailDeleteFromFav(error.getLocalizedMessage())
                    );
        }
    }
}
