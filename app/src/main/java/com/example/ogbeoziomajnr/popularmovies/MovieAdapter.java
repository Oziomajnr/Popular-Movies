package com.example.ogbeoziomajnr.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by SQ-OGBE PC on 13/04/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static int viewHolderCount;

    private int mNumberItems;

    public MovieAdapter(int numberItems) {
        viewHolderCount = 0;
        mNumberItems = numberItems;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutForItem = R.layout.movie_list;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutForItem, viewGroup, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        viewHolderCount++;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView movieImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieImageView = (ImageView) itemView.findViewById(R.id.img_movie_thumbnail);


        }

        void bind() {
            Toast.makeText(itemView.getContext(),"Cake Life", Toast.LENGTH_LONG).show();
            Picasso.with(itemView.getContext()).load("https://avatars3.githubusercontent.com/u/25436529?v=3").into(movieImageView);
        }

    }
}
