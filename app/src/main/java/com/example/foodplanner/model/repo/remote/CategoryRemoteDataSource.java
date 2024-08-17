package com.example.foodplanner.model.repo.remote;

import com.example.foodplanner.model.dto.CategoryResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface CategoryRemoteDataSource {
    Observable<List<CategoryResponse.CategoriesItem>> getCategories();

}
