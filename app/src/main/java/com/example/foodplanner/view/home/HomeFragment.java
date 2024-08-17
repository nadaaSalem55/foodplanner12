package com.example.foodplanner.view.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.GlideImage;
import com.example.foodplanner.R;
import com.example.foodplanner.model.dto.CategoryResponse;
import com.example.foodplanner.model.dto.Country;
import com.example.foodplanner.model.dto.Ingredient;
import com.example.foodplanner.model.dto.MealsItem;
import com.example.foodplanner.model.repo.MealRepoImp;
import com.example.foodplanner.model.repo.local.MealLocalDatasource;
import com.example.foodplanner.model.repo.remote.CategoryRemoteDataSourceImp;
import com.example.foodplanner.model.repo.remote.CategoryRepo;
import com.example.foodplanner.model.repo.remote.MealRemoteDataSource;
import com.example.foodplanner.model.repo.remote.RandomMealRemoteDataSourceImp;
import com.example.foodplanner.presenter.HomePresenter;
import com.example.foodplanner.view.AlertMessage;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeView {
    ImageView mealImg;
    TextView mealTitle;
    HomePresenter homePresenter;
    MaterialCardView cardView;
    CategoryAdapter categoryAdapter;
    CountryAdapter countryAdapter;
    RecyclerView categoryRecyclerView, ingredRecyclerView,countryRecyclerView;
    ProgressBar ingredientProgressBar;
    MealsItem mealItem;
    OnMealByCountryClick onMealByCountryClick;
    OnMealByIngredientClick onMealByIngredientClick;
    OnMealByCategoryClick onMealByCategoryClick;
    IngredientAdapter ingredientAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intiViews(view);
        homePresenter = new HomePresenter.HomePresenterImp(this,
                new MealRepoImp(new RandomMealRemoteDataSourceImp(), new MealLocalDatasource.MealLocalDataSourceImp(this.getContext()), new MealRemoteDataSource.MealRemoteDataSourceImp(requireContext())),
                new CategoryRepo.CategoryRepoImp(new CategoryRemoteDataSourceImp()));
        homePresenter.getRandomMeal();
        homePresenter.getCategories();
        homePresenter.getIngredients();
        homePresenter.getCountries();
        Log.e("TAG", "onViewCreated: ");


    }

    private void intiViews(View view) {
        initIngredientRecyclerView(view);
        initCategoryRecyclerView(view);
        initCountryRecyclerView(view);
        mealImg = view.findViewById(R.id.meal_img);
        mealTitle = view.findViewById(R.id.meal_title_tv);
        ingredientProgressBar=view.findViewById(R.id.ingredient_progress_bar);
        cardView = view.findViewById(R.id.meal_card_view);
        cardView.setOnClickListener(v -> navigateToMealDetailsFragment());
    }

    private void initCountryRecyclerView(View view) {
        countryRecyclerView=view.findViewById(R.id.countries_recycler_view);
        countryAdapter=new CountryAdapter(new ArrayList<>());
        countryAdapter.setOnItemClickListener(country -> {
            homePresenter.getMealsByCountry(country.getName());
            onMealByCountryClick = mealsItems -> {
                MealsItem[] meals = mealsItems.toArray(new MealsItem[mealsItems.size()]);
                navigateToyMealFragment(meals);
            };
        });
        countryRecyclerView.setAdapter(countryAdapter);
    }

    private void initCategoryRecyclerView(View view) {
        categoryAdapter = new CategoryAdapter(new ArrayList<>());
        categoryRecyclerView = view.findViewById(R.id.category_recycler_view);
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryAdapter.onItemClickListener = categoriesItem -> {
            homePresenter.getMealByCategory(categoriesItem.getStrCategory());
            onMealByCategoryClick= mealsItems -> {
                MealsItem[] mealsItem = mealsItems.toArray(new MealsItem[mealsItems.size()]);
                hideProgressBar(ingredientProgressBar);
                navigateToyMealFragment(mealsItem);
            };

        };
    }

    private void initIngredientRecyclerView(View view) {
        ingredRecyclerView = view.findViewById(R.id.ingredients_recycler_view);
        ingredientAdapter = new IngredientAdapter(new ArrayList<>());
        ingredRecyclerView.setAdapter(ingredientAdapter);
        ingredientAdapter.onItemClickListener = ingredient -> {
            homePresenter.getMealsByIngredient(ingredient.getIngredientName());
            onMealByIngredientClick= mealsItems -> {
                MealsItem[] mealsItem = mealsItems.toArray(new MealsItem[mealsItems.size()]);
                navigateToyMealFragment(mealsItem);
            };
        };
    }

    private void navigateToyMealFragment(MealsItem[] mealsItems) {
        HomeFragmentDirections.ActionHomeFragmentToCategoryMealFragment action = HomeFragmentDirections
                .actionHomeFragmentToCategoryMealFragment(mealsItems);
        Navigation.findNavController(requireView()).navigate(action);

    }

    private void navigateToMealDetailsFragment() {
        HomeFragmentDirections.ActionHomeFragmentToMealDetailsFragment action =
                HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(mealItem);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void showSuccessMessage(MealsItem mealsItem) {
        this.mealItem = mealsItem;
        mealTitle.setText(mealsItem.getStrMeal());
        GlideImage.downloadImageToImageView(mealImg.getContext(), mealsItem.getStrMealThumb(), mealImg);

    }

    @Override
    public void showErrorMessage(String error) {
        AlertMessage.showToastMessage(error, this.getContext());
    }

    @Override
    public void showCategorySuccessMessage(List<CategoryResponse.CategoriesItem> categoriesItems) {
        categoryAdapter.changeData(categoriesItems);
    }

    @Override
    public void showCategoryErrorMessage(String error) {
        showMessage(error);
    }

    @Override
    public void showIngredientSuccessMessage(List<Ingredient> ingredients) {
        ingredientAdapter.changeData(ingredients);
        hideProgressBar(ingredientProgressBar);
    }

    @Override
    public void showIngredientsErrorMessage(String localizedMessage) {
        showMessage(localizedMessage);
    }

    @Override
    public void showMealsByIngredientSuccess(List<MealsItem> mealsItems) {
        onMealByIngredientClick.onItemClick(mealsItems);
        hideProgressBar(ingredientProgressBar);
    }

    @Override
    public void showMealsByIngredientError(String localizedMessage) {
        showMessage(localizedMessage);
        hideProgressBar(ingredientProgressBar);
    }

    @Override
    public void onMealByCategorySuccess(List<MealsItem> mealsItems) {
        onMealByCategoryClick.onItemClick(mealsItems);
    }

    @Override
    public void onMealByCategoryFail(String localizedMessage) {
        showMessage(localizedMessage);
    }

    @Override
    public void showCountriesSuccessMessage(List<Country> countries) {
        countryAdapter.setData(countries);
    }

    @Override
    public void showCountriesErrorMessage(String localizedMessage) {
        showMessage(localizedMessage);
    }

    @Override
    public void showMealsByCountryError(String localizedMessage) {
        showMessage(localizedMessage);
    }
    @Override
    public void showMealsByCountrySuccess(List<MealsItem> mealsItems) {
        onMealByCountryClick.onItemClick(mealsItems);

    }

    void hideProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void showMessage(String message) {
        AlertMessage.showToastMessage(message, this.getContext());
    }
    interface OnMealByCountryClick {
        void onItemClick(List<MealsItem> mealsItems);
    }
    interface OnMealByCategoryClick {
        void onItemClick(List<MealsItem> mealsItems);
    }
    interface OnMealByIngredientClick {
        void onItemClick(List<MealsItem> mealsItems);
    }

}