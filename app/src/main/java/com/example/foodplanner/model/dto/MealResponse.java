package com.example.foodplanner.model.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MealResponse{

	@SerializedName("meals")
	private List<MealsItem> meals;

	public List<MealsItem> getMeals(){
		return meals;
	}
}