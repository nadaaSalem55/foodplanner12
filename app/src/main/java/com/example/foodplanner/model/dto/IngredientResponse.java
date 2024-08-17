package com.example.foodplanner.model.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IngredientResponse{

	@SerializedName("meals")
	private List<Ingredient> ingredients;

	public List<Ingredient> getMeals(){
		return ingredients;
	}
}