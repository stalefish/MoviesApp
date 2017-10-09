package com.example.android.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.movies.model.MovieDBResult;
import com.example.android.movies.utils.MovieDBUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieDBAdapter extends RecyclerView.Adapter<MovieDBAdapter.PosterViewHolder> {

    private static final String TAG = MovieDBAdapter.class.getSimpleName();
    private static final String THUMBNAIL_SIZE = "w342";
    private List<MovieDBResult> data = new ArrayList<>();
    private Context context;
    private MovieDBAdapterOnClickHandler onClickHandler;

    public MovieDBAdapter(Context context, List<MovieDBResult> data, MovieDBAdapterOnClickHandler onClickHandler) {
        this.context = context;
        this.data = data;
        this.onClickHandler = onClickHandler;
    }

    public interface MovieDBAdapterOnClickHandler {
        void onClick(MovieDBResult movieDBResult);
    }

    public void setData(List<MovieDBResult> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.poster_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        PosterViewHolder viewHolder = new PosterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {
        MovieDBResult movieDBResult = data.get(position);
        holder.setPosition(position);
        String url = MovieDBUtils.BASE_IMAGE_URL + "/" + THUMBNAIL_SIZE + "/" + movieDBResult.getPosterPath();
        Log.v(TAG,"loading " + url);
        Picasso.with(context).load(url).into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        int position;
        private PosterViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_poster);
            imageView.setOnClickListener(this);
        }

        private void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            MovieDBResult movieData = data.get(position);
            Log.v(TAG,"onClick " + movieData);
            onClickHandler.onClick(movieData);
        }
    }

}
