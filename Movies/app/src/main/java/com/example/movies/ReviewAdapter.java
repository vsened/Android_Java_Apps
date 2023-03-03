package com.example.movies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private static final String TYPE_POSITIVE = "Позитивный";
    private static final String TYPE_NEGATIVE = "Негативный";

    private List<Review> reviews = new ArrayList<>();

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.review_item,
                parent,
                false
        );
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.textViewAuthorName.setText(review.getAuthor());
        holder.textViewReview.setText(review.getReview());
        int colorResId;
        switch (review.getType()) {
            case TYPE_POSITIVE:
                colorResId = android.R.color.holo_green_light;
                break;
            case TYPE_NEGATIVE:
                colorResId = android.R.color.holo_red_light;
                break;
            default:
                colorResId = android.R.color.holo_orange_light;
                break;
        }
        int color = ContextCompat.getColor(holder.itemView.getContext(), colorResId);
        holder.linearLayout.setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout linearLayout;
        private final TextView textViewAuthorName;
        private final TextView textViewReview;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearLayoutReviews);
            textViewAuthorName = itemView.findViewById(R.id.textViewAuthorName);
            textViewReview = itemView.findViewById(R.id.textViewReview);
        }

    }
}
