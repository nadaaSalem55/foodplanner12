package com.example.foodplanner.view.serach_meal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.model.dto.CategoryResponse;
import com.example.foodplanner.model.dto.MealsItem;
import com.example.foodplanner.model.repo.MealRepoImp;
import com.example.foodplanner.model.repo.local.MealLocalDatasource;
import com.example.foodplanner.model.repo.remote.CategoryRemoteDataSourceImp;
import com.example.foodplanner.model.repo.remote.CategoryRepo;
import com.example.foodplanner.model.repo.remote.MealRemoteDataSource;
import com.example.foodplanner.model.repo.remote.RandomMealRemoteDataSourceImp;
import com.example.foodplanner.presenter.SearchPresenter;
import com.example.foodplanner.view.AlertMessage;
import com.example.foodplanner.view.home.CategoryAdapter;
import com.example.foodplanner.view.meal_by_category.MealAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchMealFragment extends Fragment implements  SearchMealView {

    CategoryAdapter categoryAdapter;
    RecyclerView categoryRecyclerView,mealRecyclerView;
    SearchView searchBar;
    SearchPresenter searchPresenter;
    MealAdapter mealAdapter;
    ProgressBar categoryProgressBar;
    OnMealByCategoryClick onMealByCategoryClick;

    public SearchMealFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDependencies();
        intiViews(view);

    }



    private void initDependencies() {
        searchPresenter=new SearchPresenter.
                SearchPresenterImp(
                this,new CategoryRepo.CategoryRepoImp(new CategoryRemoteDataSourceImp()),new MealRepoImp(
                        new RandomMealRemoteDataSourceImp(),
                new MealLocalDatasource.MealLocalDataSourceImp(requireContext()),
                new MealRemoteDataSource.MealRemoteDataSourceImp(requireContext())));
        searchPresenter.getCategories();
    }


    private void intiViews(View view) {
        initCategoryRecyclerView(view);
        categoryProgressBar=view.findViewById(R.id.category_progress_bar);
        initMealRecyclerView(view);
        initSearchView(view);
    }

    private void initMealRecyclerView(View view) {
        mealAdapter=new MealAdapter(new ArrayList<>());
        mealRecyclerView=view.findViewById(R.id.recycler_view_meal);
        mealAdapter.onItemClickListener= mealsItem -> searchPresenter.getMealById(mealsItem.getIdMeal());
        mealRecyclerView.setAdapter(mealAdapter);
    }

    private void initSearchView(View view) {
        searchBar=view.findViewById(R.id.search_view);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    searchPresenter.searchMealByName(newText);
                } else {
                    mealAdapter.clearData();
                }
                return true;
            }
        });
    }

    private void initCategoryRecyclerView(View view) {
        categoryAdapter = new CategoryAdapter(new ArrayList<>());
        categoryRecyclerView = view.findViewById(R.id.recyclerview_meals);
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryAdapter.onItemClickListener = categoriesItem -> {
            searchPresenter.getMealByCategory(categoriesItem.getStrCategory());
            onMealByCategoryClick= mealsItems -> {
                MealsItem[] mealsItem = mealsItems.toArray(new MealsItem[mealsItems.size()]);
                navigateToMealFragment(mealsItem);
            };

        };
    }





    private void navigateToMealFragment(MealsItem[] mealsItems) {
        SearchMealFragmentDirections.ActionSearchFragment2ToCategoryMealFragment action=SearchMealFragmentDirections
                .actionSearchFragment2ToCategoryMealFragment(mealsItems);
        Navigation.findNavController(requireView()).navigate(action);

    }


    @Override
    public void showCategorySuccessMessage(List<CategoryResponse.CategoriesItem> categoriesItems) {
        categoryAdapter.changeData(categoriesItems);
        hideProgressBar(categoryProgressBar);
    }

    @Override
    public void showCategoryErrorMessage(String error) {
        AlertMessage.showToastMessage(error,this.getContext());
        hideProgressBar(categoryProgressBar);
    }


    @Override
    public void onMealByCategorySuccess(List<MealsItem> mealsItems) {
        onMealByCategoryClick.onItemClick(mealsItems);
    }

    @Override
    public void onMealByCategoryFail(String localizedMessage) {
        showMessage(localizedMessage);
    }
    void hideProgressBar(ProgressBar progressBar){
        progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public void showMeal(List<MealsItem> meals) {
        mealAdapter.changeData(meals);
    }

    @Override
    public void showError(String message) {
        showMessage(message);
    }

    @Override
    public void showMealById(MealsItem mealsItem) {
        navigateToMealDetailsFragment(mealsItem);
    }

    @Override
    public void showMealsByCountryError(String localizedMessage) {
        showMessage(localizedMessage);
    }

    private void navigateToMealDetailsFragment(MealsItem mealsItems) {
        SearchMealFragmentDirections.ActionSearchFragment2ToMealDetailsFragment action=SearchMealFragmentDirections
                .actionSearchFragment2ToMealDetailsFragment(mealsItems) ;
        Navigation.findNavController(requireView()).navigate(action);

    }

    interface OnMealByCategoryClick {
        void onItemClick(List<MealsItem> mealsItems);
    }

    void showMessage(String msg){
        AlertMessage.showToastMessage(msg,requireContext());
    }
}