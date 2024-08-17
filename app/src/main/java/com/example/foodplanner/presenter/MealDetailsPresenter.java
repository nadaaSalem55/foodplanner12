package com.example.foodplanner.presenter;

import android.util.Log;

import com.example.foodplanner.model.dto.MealsItem;
import com.example.foodplanner.model.repo.MealRepo;
import com.example.foodplanner.view.meal_details.MealDetailsView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public interface MealDetailsPresenter {
    void addMealToFavorite(MealsItem mealsItem);

    void deleteFavMeals(MealsItem mealItem);

    void addMealToWeeklyPlan(MealsItem mealsItem);
    public void getMealById(String mealId);



    class MealDetailsPresenterImp implements MealDetailsPresenter {
        private final CompositeDisposable compositeDisposable;
        MealRepo mealRepo;
        MealDetailsView mealDetailsView;

        public MealDetailsPresenterImp(MealRepo mealRepo, MealDetailsView mealDetailsView) {
            this.mealRepo = mealRepo;
            this.mealDetailsView = mealDetailsView;
            compositeDisposable=new CompositeDisposable();
        }

        @Override
        public void addMealToFavorite(MealsItem mealsItem) {

            mealRepo.insertMealToFavRemoteAndLocal(mealsItem).
                    subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> mealDetailsView.onInsertFavSuccess(),
                            error -> mealDetailsView.onInsertFavError(error.getLocalizedMessage()));
        }

        @Override
        public void deleteFavMeals(MealsItem mealItem) {
            mealRepo.deleteFromRemoteAndLocal(mealItem)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> mealDetailsView.onSuccessDeleteFromFav(),
                            error -> mealDetailsView.onFailDeleteFromFav(error.getLocalizedMessage())
                    );
        }

        @Override
        public void addMealToWeeklyPlan(MealsItem mealsItem) {
            mealRepo.insertMealToWeeklyPlanRemoteAndLocal(mealsItem).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> mealDetailsView.onPlanMealSuccess(),
                            error -> mealDetailsView.onPlanMealFail(error.getLocalizedMessage()));
        }

        @Override
        public void getMealById(String mealId) {
            compositeDisposable.add(
                    mealRepo.getMealById(mealId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(meal->handleSuccess((MealsItem) meal),
                                    error->handleError(error)));

        }


        private void handleSuccess(MealsItem mealsItems) {
            mealDetailsView.showMeal(mealsItems);
        }

        private void handleError(Throwable throwable) {
            Log.e("MealPresenter", "Error: " + throwable.getLocalizedMessage());
            mealDetailsView.showError("Error loading meals");
        }


    }
}
