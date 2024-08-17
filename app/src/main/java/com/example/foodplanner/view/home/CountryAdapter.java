package com.example.foodplanner.view.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.model.dto.Country;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {

    private  List<Country> countryList;
    OnItemClickListener onItemClickListener;

    public CountryAdapter(List<Country> countryList) {
        this.countryList = countryList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setData(List<Country> countries) {
        countryList.clear();
        countryList.addAll(countries);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_item, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        holder.bind(countryList.get(position));
        holder.itemView.setOnClickListener(v ->{
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(countryList.get(position));
        }
        });
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public static class CountryViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageViewCountry;
        private final TextView textViewCountryName;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCountry = itemView.findViewById(R.id.country_img);
            textViewCountryName = itemView.findViewById(R.id.country_name);
        }

        public void bind(Country country) {
            imageViewCountry.setImageResource(country.getImageResourceId());
            textViewCountryName.setText(country.getName());
        }
    }
    interface OnItemClickListener{
        void onItemClick(Country country);
    }
}

