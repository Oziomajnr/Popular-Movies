package com.example.ogbeoziomajnr.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ogbeoziomajnr.popularmovies.Model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.ogbeoziomajnr.popularmovies.CONSTANTS.IMAGE_BASE_URL;

/**
 * Created by SQ-OGBE PC on 13/04/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {


    private List<Movie> movies;
    private final MovieAdapterOnClickHandler  mClickHandler;

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutForItem = R.layout.movie_list;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutForItem, viewGroup, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    public void setImageUrl(List<Movie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        if (movies == null) {
           return 0;
        }
        return  movies.size();
    }

    public interface  MovieAdapterOnClickHandler {
        void onClick(Movie movieToView);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        ImageView movieImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieImageView = (ImageView) itemView.findViewById(R.id.img_movie_thumbnail);
            itemView.setOnClickListener(this);
        }
        void bind(int position) {
            Picasso.with(itemView.getContext()).load(IMAGE_BASE_URL+movies.get(position).getPosterPath()).placeholder(R.drawable.loading_image).into(movieImageView);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movieToView = movies.get(adapterPosition);
            mClickHandler.onClick(movieToView);
        }
    }
}
