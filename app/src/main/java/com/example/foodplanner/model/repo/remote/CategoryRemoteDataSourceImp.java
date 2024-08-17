package com.example.foodplanner.model.repo.remote;

import com.example.foodplanner.api.ApiManager;
import com.example.foodplanner.api.WebService;
import com.example.foodplanner.model.dto.CategoryResponse;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CategoryRemoteDataSourceImp implements CategoryRemoteDataSource{
    WebService webService;

    public CategoryRemoteDataSourceImp() {
        this.webService = ApiManager.getApi();
    }

    @Override
    public Observable<List<CategoryResponse.CategoriesItem>> getCategories() {
        return webService.getCategories().map(CategoryResponse::getCategories)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
