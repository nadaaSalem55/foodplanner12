package com.example.foodplanner.view.meal_details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.GlideImage;
import com.example.foodplanner.R;
import com.example.foodplanner.model.dto.Ingredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    public IngredientAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    List<Ingredient> ingredients;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.ingredient_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient=ingredients.get(position);
        holder.ingredientName.setText(ingredient.getIngredientName());
        holder.ingredientMeasure.setText(ingredient.getIngredientMeasure());
        GlideImage.downloadImageToImageView(holder.ingredientImg.getContext(),
                ingredient.getThumbnailUrl(),holder.ingredientImg);

    }

    public void changeData(List<Ingredient> ingredients){
        this.ingredients=ingredients;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView ingredientName,ingredientMeasure;
        ImageView ingredientImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName=itemView.findViewById(R.id.ingredient_name_tv);
            ingredientMeasure=itemView.findViewById(R.id.ingredient_measure_tv);
            ingredientImg=itemView.findViewById(R.id.ingredient_img);
        }
    }
}
