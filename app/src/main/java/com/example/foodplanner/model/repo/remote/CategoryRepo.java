package com.example.foodplanner.model.repo.remote;

import com.example.foodplanner.model.dto.CategoryResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface CategoryRepo {
    Observable<List<CategoryResponse.CategoriesItem>> getCategories();

    public class CategoryRepoImp implements CategoryRepo {

        public CategoryRepoImp(CategoryRemoteDataSource categoryRemoteDataSource) {
            this.categoryRemoteDataSource = categoryRemoteDataSource;
        }

        CategoryRemoteDataSource categoryRemoteDataSource;

        @Override
        public Observable<List<CategoryResponse.CategoriesItem>> getCategories() {
            return categoryRemoteDataSource.getCategories();
        }
    }

}
