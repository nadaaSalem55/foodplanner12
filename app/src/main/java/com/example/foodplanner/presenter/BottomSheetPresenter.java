package com.example.foodplanner.presenter;

import com.example.foodplanner.model.dto.MealsItem;
import com.example.foodplanner.model.repo.MealRepo;
import com.example.foodplanner.view.bottom_sheet.BottomSheetView;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public interface BottomSheetPresenter {

    void searchMealByName(String name);

    void getMealById(String idMeal);

    void addMealToWeeklyPlan(MealsItem mealsItem);

    class BottomSheetPresenterImp implements BottomSheetPresenter{
        BottomSheetView bottomSheetView;
        CompositeDisposable compositeDisposable;
        MealRepo mealRepo;

        public BottomSheetPresenterImp(BottomSheetView bottomSheetView, MealRepo mealRepo) {
            this.bottomSheetView = bottomSheetView;
            this.mealRepo = mealRepo;
            this.compositeDisposable=new CompositeDisposable();
        }

        @Override
        public void searchMealByName(String name) {
            compositeDisposable.add(mealRepo.searchMeals(name)
                    .subscribeWith(new DisposableObserver<List<MealsItem>>() {
                        @Override
                        public void onNext(List<MealsItem> meals) {
                            bottomSheetView.showMeals(meals);
                        }

                        @Override
                        public void onError(Throwable e) {
                            bottomSheetView.showError(e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            // Handle completion if needed
                        }
                    }));
        }

        @Override
        public void getMealById(String idMeal) {
                compositeDisposable.add(
                        mealRepo.getMealById(idMeal)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(meal->bottomSheetView.onGetMealByIdSuccess(meal),
                                        error->bottomSheetView.onGetMealByIdFail(error.getLocalizedMessage())
                                )
                );
            }

        @Override
        public void addMealToWeeklyPlan(MealsItem mealsItem) {
            mealRepo.insertMealToWeeklyPlanRemoteAndLocal(mealsItem).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> bottomSheetView.onPlanMealSuccess(),
                            error -> bottomSheetView.onPlanMealFail(error.getLocalizedMessage()));
        }

    }
}

