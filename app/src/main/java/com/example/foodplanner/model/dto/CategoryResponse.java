package com.example.foodplanner.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CategoryResponse{

	@SerializedName("categories")
	private List<CategoriesItem> categories;

	public List<CategoriesItem> getCategories(){
		return categories;
	}

	public class CategoriesItem implements Serializable {

		@SerializedName("strCategory")
		private String strCategory;

		@SerializedName("strCategoryDescription")
		private String strCategoryDescription;

		@SerializedName("idCategory")
		private String idCategory;

		@SerializedName("strCategoryThumb")
		private String strCategoryThumb;

		public String getStrCategory(){
			return strCategory;
		}

		public String getStrCategoryDescription(){
			return strCategoryDescription;
		}

		public String getIdCategory(){
			return idCategory;
		}

		public String getStrCategoryThumb(){
			return strCategoryThumb;
		}
	}
}