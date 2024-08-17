package com.example.foodplanner;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GlideImage {
     public static void downloadImageToImageView(Context context, String url, ImageView imageView){
         Glide.with(context)
                 .load(url)
                 .into(imageView);
     }
}
