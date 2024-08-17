package com.example.foodplanner.presenter;

import com.example.foodplanner.api.MealCallBack;
import com.example.foodplanner.model.dto.MealsItem;
import com.example.foodplanner.model.repo.MealRepo;
import com.example.foodplanner.model.repo.remote.CategoryRepo;
import com.example.foodplanner.view.home.HomeView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public interface HomePresenter {
    void getRandomMeal();
    void getCategories();
     void getIngredients();
     void getMealsByIngredient(String ingredient);
    void getMealByCategory(String category);
    void getCountries();
    void getMealsByCountry(String country);


     class HomePresenterImp implements HomePresenter, MealCallBack {
        HomeView homeView;
        MealRepo mealRepo;
        CategoryRepo categoryRepo;
        private final CompositeDisposable compositeDisposable;


        public HomePresenterImp(HomeView homeView, MealRepo mealRepo,CategoryRepo categoryRepo) {
            this.homeView = homeView;
            this.mealRepo = mealRepo;
            this.categoryRepo=categoryRepo;
            compositeDisposable=new CompositeDisposable();
        }

        @Override
        public void getRandomMeal() {
            mealRepo.getRandomMeal(this);
        }

        @Override
        public void getCategories() {
            compositeDisposable.add(categoryRepo.getCategories().subscribeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread()).
                    subscribe(categoriesItems ->  homeView.showCategorySuccessMessage(categoriesItems),
                            error->homeView.showCategoryErrorMessage(error.getLocalizedMessage())));
        }

        @Override
        public void getIngredients() {
            compositeDisposable.add(mealRepo.getIngredients().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(ingredients->homeView.showIngredientSuccessMessage(ingredients),
                            error->homeView.showIngredientsErrorMessage(error.getLocalizedMessage())));
        }

        @Override
        public void getMealsByIngredient(String ingredient) {
            compositeDisposable.add(mealRepo.getMealsByIngredient(ingredient)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mealsItems->homeView.showMealsByIngredientSuccess(mealsItems),
                            error->homeView.showMealsByIngredientError(error.getLocalizedMessage())));
        }

        @Override
        public void getMealByCategory(String category) {
            compositeDisposable.add(
                    mealRepo.getMealByCategory(category)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(mealsItems -> homeView.onMealByCategorySuccess(mealsItems),
                                    error->homeView.onMealByCategoryFail(error.getLocalizedMessage())));
        }

         @Override
         public void getCountries() {
            compositeDisposable.add(mealRepo.getCountries().subscribeOn(
                    Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                            countries -> homeView.showCountriesSuccessMessage(countries),
                    error->homeView.showCountriesErrorMessage(error.getLocalizedMessage())
            ));

         }

         @Override
         public void getMealsByCountry(String country) {
             compositeDisposable.add(mealRepo.getMealsByCountry(country)
                     .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                     .subscribe(mealsItems -> homeView.showMealsByCountrySuccess(mealsItems),errpr->homeView.showMealsByCountryError(errpr.getLocalizedMessage())));
         }

         @Override
        public void onSuccess(MealsItem mealsItem) {
            homeView.showSuccessMessage(mealsItem);
        }

        @Override
        public void onFailure(String errorMessage) {
            homeView.showErrorMessage(errorMessage);
        }
    }

}
