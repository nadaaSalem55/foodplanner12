package com.example.foodplanner.model.repo.local;

import android.content.Context;

import com.example.foodplanner.db.MealDao;
import com.example.foodplanner.db.MyDatabase;
import com.example.foodplanner.model.dto.MealsItem;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public interface MealLocalDatasource {
    Completable insertProductToFavorite(MealsItem mealsItem);
    Completable deleteFavoriteProduct(MealsItem mealsItem);
    Flowable<List<MealsItem>> getFavMeals();
    Flowable<List<MealsItem>> getPlannedMealByDate(String date);

    Completable addMealToWeekPlan(MealsItem mealsItem);




     class MealLocalDataSourceImp implements MealLocalDatasource {
        private MealDao mealDao;
        private Flowable<List<MealsItem>> favProducts;
        private static MealLocalDataSourceImp productRepoImp =null;

        public MealLocalDataSourceImp(Context ctx) {
            MyDatabase database= MyDatabase.getInstance(ctx);
            mealDao =database.getproductDao();
            favProducts = mealDao.getAllFavMeals();

        }
        public static MealLocalDataSourceImp getInstance(Context context){
            if(productRepoImp ==null) {
                productRepoImp = new MealLocalDataSourceImp(context);
            }
            return productRepoImp;
        }

        public Flowable<List<MealsItem>> getFavMeals() {
            return mealDao.getAllFavMeals();
        }

        @Override
        public Flowable<List<MealsItem>> getPlannedMealByDate(String date) {
            return mealDao.getPlannedMealsByDate(date);
        }

        @Override
        public Completable addMealToWeekPlan(MealsItem mealsItem) {
         //   mealsItem.setFavorite(false);
            mealsItem.setPlanned(true);
            return mealDao.addMealToPlan(mealsItem).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }

        @Override
        public Completable deleteFavoriteProduct(MealsItem mealsItem){
            return mealDao.deleteMealFromFavorite(mealsItem).
                    subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }


        @Override
        public Completable insertProductToFavorite(MealsItem mealItem){
            mealItem.setFavorite(true);
            return mealDao.insertMealToFavorite(mealItem)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }

    }
}
