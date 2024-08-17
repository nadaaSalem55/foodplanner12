package com.example.foodplanner.view.home;

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
    OnItemClickListener onItemClickListener;
    public IngredientAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    List<Ingredient> ingredients;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.ingredient_view_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient=ingredients.get(position);
        holder.ingredientName.setText(ingredient.getIngredientName());
        GlideImage.downloadImageToImageView(holder.ingredientImg.getContext(),ingredient.getThumbnailUrl(),holder.ingredientImg);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(ingredient));
    }

    public void changeData(List<Ingredient> ingredients){
        this.ingredients=ingredients;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView ingredientName;
        ImageView ingredientImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName=itemView.findViewById(R.id.ingredient_name);
            ingredientImg=itemView.findViewById(R.id.ingredient_img);

        }
    }
    interface OnItemClickListener{
        void onItemClick(Ingredient ingredient);
    }
}
