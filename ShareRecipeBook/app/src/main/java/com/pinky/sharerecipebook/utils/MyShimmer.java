package com.pinky.sharerecipebook.utils;/* Created by Shay Mualem 09/10/2021 */

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;

public class MyShimmer {

    public static ShimmerDrawable getShimmer() {

        //Shimmer https://stackoverflow.com/questions/61076174/how-to-use-a-view-shimmer-as-a-placeholder-for-an-imageview-glide

        // The attributes for a ShimmerDrawable is set by this builder
        // how long the shimmering animation takes to do one full sweep
        //the alpha of the underlying children
        // the shimmer alpha amount
        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setDuration(1500) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.9f) //the alpha of the underlying children
                .setHighlightAlpha(0.4f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();

        // This is the placeholder for the imageView
        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        return shimmerDrawable;

    }

}
