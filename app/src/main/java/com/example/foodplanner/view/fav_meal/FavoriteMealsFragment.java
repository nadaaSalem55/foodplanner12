package com.example.foodplanner.view.fav_meal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.db.SharedPreferencesManager;
import com.example.foodplanner.model.dto.MealsItem;
import com.example.foodplanner.model.repo.MealRepoImp;
import com.example.foodplanner.model.repo.local.MealLocalDatasource;
import com.example.foodplanner.model.repo.remote.MealRemoteDataSource;
import com.example.foodplanner.model.repo.remote.RandomMealRemoteDataSourceImp;
import com.example.foodplanner.presenter.FavMealPresenter;
import com.example.foodplanner.view.AlertMessage;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMealsFragment extends Fragment implements FavoriteView{
    RecyclerView recyclerView;
    FavMealAdapter favMealAdapter;
    FavMealPresenter favMealPresenter;


    public FavoriteMealsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_meals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        favMealPresenter=new FavMealPresenter.FavMealPresenterImp(new MealRepoImp(new RandomMealRemoteDataSourceImp(),
                new MealLocalDatasource.MealLocalDataSourceImp(this.requireContext()),new MealRemoteDataSource.MealRemoteDataSourceImp(requireContext())),this);
        favMealPresenter.getMeals();
        initViews(view);
    }

    private void initViews(View view) {
        favMealAdapter=new FavMealAdapter(new ArrayList<>());
        recyclerView=view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this.requireContext(),RecyclerView.VERTICAL,false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        favMealAdapter.onDeleteTextClickListener= this::deleteMealFromFav;
        favMealAdapter.onItemClickListener= this::navigateToMealDetailsFragment;
        recyclerView.setAdapter(favMealAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void deleteMealFromFav(MealsItem mealsItem) {
        String title="Are you sure you want to delete the meal from your favorites?";
        AlertMessage.showCustomAlertDialog(getContext(),title,"Yes", (dialog, which) -> favMealPresenter.deleteFavMeals(mealsItem));
        SharedPreferencesManager.saveFavoriteStatus(this.requireContext(),false,mealsItem.getIdMeal());
    }

    @Override
    public void onSuccessDeleteFromFav() {

    }

    @Override
    public void onFailDeleteFromFav(String error) {

    }

    @Override
    public void onGetAllFavoriteMeals(List<MealsItem> favoriteMeals) {
        favMealAdapter.changeData(favoriteMeals);
    }

    @Override
    public void onGetAllFavoriteMealsError(String errorMessage) {
        showErrorMsg(errorMessage);
    }

    @Override
    public void onGetAllFavoriteFireStoreMeals(List<MealsItem> favoriteMeals) {
        favMealAdapter.changeData(favoriteMeals);
    }

    @Override
    public void onGetAllFavoriteFireStoreMealsError(String errorMessage) {
        showErrorMsg(errorMessage);

    }


    private void navigateToMealDetailsFragment(MealsItem mealsItems) {
        FavoriteMealsFragmentDirections.ActionFavoriteMealsFragmentToMealDetailsFragment action=FavoriteMealsFragmentDirections
                .actionFavoriteMealsFragmentToMealDetailsFragment(mealsItems) ;
        Navigation.findNavController(requireView()).navigate(action);

    }

    private void showErrorMsg(String error){
        AlertMessage.showToastMessage(error,requireContext());
    }
}