package com.example.foodplanner.view.meal_by_category;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.model.dto.MealsItem;

import java.util.ArrayList;
import java.util.Arrays;

public class MealFragment extends Fragment implements MealView {

    //private CategoryResponse.CategoriesItem categoryItem;
    String categoryItem;
    private RecyclerView recyclerView;
    private MealAdapter mealAdapter;
    //private MealPresenter mealPresenter;
    MealsItem [] mealsItem;

    public MealFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_meal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeDependencies();
        initViews(view);
        retrieveCategoryFromArguments();

    }

    private void initializeDependencies() {
        /*mealPresenter = new MealPresenter.MealPresenterImp(
                new MealRemoteDataSource.MealRemoteDataSourceImp(requireContext()),
                this
        );*/
    }

    private void retrieveCategoryFromArguments() {
        if(getArguments() !=null){
            mealsItem= MealFragmentArgs.fromBundle(getArguments()).getMealItem();
            assert mealsItem != null;
            Log.d("TAG", "retrieveCategoryFromArguments:"+Arrays.asList(mealsItem).get(0).getStrMeal());
            mealAdapter.changeData(Arrays.asList(mealsItem));
        }
        /*if (getArguments() != null) {
            categoryItem = CategoryMealFragmentArgs.fromBundle(getArguments()).getCategoryName();
            if (categoryItem != null) {
                mealPresenter.getMealByCategory(categoryItem);
            }
        }*/
    }

    private void initViews(View view) {
        mealAdapter = new MealAdapter(new ArrayList<>());
        recyclerView = view.findViewById(R.id.meal_recycler_view);
        mealAdapter.onItemClickListener= this::navigateToMealDetailsFragment;
        recyclerView.setAdapter(mealAdapter);

    }

   /* @Override
    public void showMeal(List<MealsItem> mealsItems) {
        mealAdapter.changeData(mealsItems);
    }

    @Override
    public void showError(String error) {
        AlertMessage.showToastMessage(error, requireContext());
    }*/

    private void navigateToMealDetailsFragment(MealsItem mealsItems) {
        MealFragmentDirections.ActionCategoryMealFragmentToMealDetailsFragment action=MealFragmentDirections
                .actionCategoryMealFragmentToMealDetailsFragment(mealsItems) ;
        Navigation.findNavController(requireView()).navigate(action);

    }

}
