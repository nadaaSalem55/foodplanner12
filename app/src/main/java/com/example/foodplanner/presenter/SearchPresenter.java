package com.example.foodplanner.presenter;

import com.example.foodplanner.model.dto.MealsItem;
import com.example.foodplanner.model.repo.MealRepo;
import com.example.foodplanner.model.repo.remote.CategoryRepo;
import com.example.foodplanner.view.serach_meal.SearchMealView;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public interface SearchPresenter {
    void searchMealByName(String name);
     void getMealById(String idMeal);

    void getCategories();

    void getMealByCategory(String strCategory);

    class SearchPresenterImp implements SearchPresenter  {
        SearchMealView searchMealView;
        CompositeDisposable compositeDisposable;
        CategoryRepo categoryRepo;
        MealRepo mealRepo;

        public SearchPresenterImp(SearchMealView searchMealView,CategoryRepo categoryRepo,MealRepo mealRepo) {
            this.searchMealView = searchMealView;
            this.compositeDisposable = new CompositeDisposable();
            this.categoryRepo=categoryRepo;
            this.mealRepo=mealRepo;
        }

        @Override
        public void searchMealByName(String name) {
            compositeDisposable.add(mealRepo.searchMeals(name)
                    .subscribeWith(new DisposableObserver<List<MealsItem>>() {
                        @Override
                        public void onNext(List<MealsItem> meals) {
                            searchMealView.showMeal(meals);
                        }

                        @Override
                        public void onError(Throwable e) {
                            searchMealView.showError(e.getMessage());
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
                            .subscribe(
                                    this::handleMealByIdSuccess,
                                    this::handleError
                            )
            );
        }
        private void handleMealByIdSuccess(Object o) {
            searchMealView.showMealById((MealsItem) o);
        }
        private void handleError(Throwable throwable) {
            searchMealView.showError("Error loading meals");
        }

        public void onDestroy() {
            compositeDisposable.clear();
        }

         @Override
         public void getCategories() {
             compositeDisposable.add(categoryRepo.getCategories().subscribeOn(Schedulers.io())
                     .subscribeOn(Schedulers.io()).
                     observeOn(AndroidSchedulers.mainThread()).
                     subscribe(categoriesItems ->  searchMealView.showCategorySuccessMessage(categoriesItems),
                             error->searchMealView.showCategoryErrorMessage(error.getLocalizedMessage())));
         }

        @Override
        public void getMealByCategory(String strCategory) {

            compositeDisposable.add(
                    mealRepo.getMealByCategory(strCategory)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(mealsItems -> searchMealView.onMealByCategorySuccess(mealsItems),
                                    error->searchMealView.onMealByCategoryFail(error.getLocalizedMessage())));

        }

    }
}
