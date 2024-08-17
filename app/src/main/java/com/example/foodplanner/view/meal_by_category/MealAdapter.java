package com.example.foodplanner.view.meal_by_category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.GlideImage;
import com.example.foodplanner.R;
import com.example.foodplanner.model.dto.MealsItem;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> {
   List<MealsItem> mealsItems;
  public OnItemClickListener onItemClickListener;

    public MealAdapter(List<MealsItem> mealsItems) {
        this.mealsItems = mealsItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.meal_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MealsItem mealsItem=mealsItems.get(position);
        holder.mealNameTv.setText(mealsItem.getStrMeal());
        GlideImage.
                downloadImageToImageView(holder.mealImg.getContext(),mealsItem.getStrMealThumb(),holder.mealImg);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(mealsItem));

    }

    @Override
    public int getItemCount() {
        return mealsItems.size();
    }
    public void changeData(List<MealsItem> mealsItems){
        this.mealsItems=mealsItems;
        notifyDataSetChanged();
    }
    public void clearData() {
        mealsItems.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mealImg;
        TextView mealNameTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View view) {
            mealImg=view.findViewById(R.id.meal_img);
            mealNameTv=view.findViewById(R.id.meal_title_tv);
        }
    }
    public interface OnItemClickListener{
        void onItemClick(MealsItem mealsItem);
    }
}
