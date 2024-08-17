package com.example.foodplanner.view.bottom_sheet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.model.dto.MealsItem;
import com.example.foodplanner.model.repo.MealRepoImp;
import com.example.foodplanner.model.repo.local.MealLocalDatasource;
import com.example.foodplanner.model.repo.remote.MealRemoteDataSource;
import com.example.foodplanner.model.repo.remote.RandomMealRemoteDataSourceImp;
import com.example.foodplanner.presenter.BottomSheetPresenter;
import com.example.foodplanner.view.AlertMessage;
import com.example.foodplanner.view.meal_by_category.MealAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetFragment extends BottomSheetDialogFragment implements BottomSheetView{
    SearchView searchView;
    BottomSheetPresenter bottomSheetPresenter;
    MealAdapter mealAdapter;
    RecyclerView mealRecyclerView;
    ProgressBar progressBar;
    String day;
    public BottomSheetDismissListener dismissListener;

    public BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getArgs();
        initDependencies();
        initViews(view);

    }

    private void getArgs() {
        Bundle args=getArguments();
        day=args.getString("day");
    }

    private void initViews(View view) {
        progressBar=view.findViewById(R.id.progress_bar);
        initMealRecyclerView(view);
        initSearchView(view);
    }
    private void initMealRecyclerView(View view) {
        mealAdapter=new MealAdapter(new ArrayList<>());
        mealRecyclerView=view.findViewById(R.id.recyclerView);
        mealAdapter.onItemClickListener= mealsItem -> {
            Log.e("TAG", "initMealRecyclerView: "+day);
            if(day!=null){
                mealsItem.setDateModified(day);
                bottomSheetPresenter.addMealToWeeklyPlan(mealsItem);
            }
            bottomSheetPresenter.getMealById(mealsItem.getIdMeal());
        };
        mealRecyclerView.setAdapter(mealAdapter);
    }

    private void initDependencies() {
        bottomSheetPresenter=new BottomSheetPresenter.BottomSheetPresenterImp(
                this,new MealRepoImp(new RandomMealRemoteDataSourceImp()
        ,new MealLocalDatasource.MealLocalDataSourceImp(requireContext()),
                new MealRemoteDataSource.MealRemoteDataSourceImp(requireContext())));
    }

    private void initSearchView(View view) {
        searchView=view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    bottomSheetPresenter.searchMealByName(newText);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    mealAdapter.clearData();
                }
                return true;
            }
        });
    }

    @Override
    public void showMeals(List<MealsItem> meals) {
        progressBar.setVisibility(View.INVISIBLE);
        mealAdapter.changeData(meals);
    }

    @Override
    public void showError(String message) {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onGetMealByIdSuccess(Object meal) {

    }

    @Override
    public void onGetMealByIdFail(String localizedMessage) {
        showMessage(localizedMessage);
    }

    @Override
    public void onPlanMealSuccess() {
        showMessage("Meal added to weekly plan");
    }

    @Override
    public void onPlanMealFail(String localizedMessage) {
        showMessage(localizedMessage);
    }

    private void showMessage(String msg){
        AlertMessage.showToastMessage(msg,requireContext());
    }


}