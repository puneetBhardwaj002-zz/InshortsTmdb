package com.example.inshortsmovie.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.inshortsmovie.R;
import com.example.inshortsmovie.models.Movies;
import com.example.inshortsmovie.models.RecyclerViewItemClickListener;
import com.example.inshortsmovie.utils.AppUtils;
import com.example.inshortsmovie.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movies> dataList = new ArrayList<>();
    private final RequestManager manager;
    private final RecyclerViewItemClickListener recyclerViewItemClickListener;

    @Inject
    public MovieAdapter(RequestManager manager, RecyclerViewItemClickListener listener){
        this.manager = manager;
        this.recyclerViewItemClickListener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_single_movie_item,parent,false);
        return new MovieViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movies currentMovie = dataList.get(position);
        holder.mTvMovieTitle.setText(currentMovie.getTitle());
        if(!AppUtils.isNullOrEmptyString(currentMovie.getReleaseDate())){
            try {
                String date = currentMovie.getReleaseDate();
                SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date newDate = spf.parse(date);
                spf= new SimpleDateFormat("dd MMM yyyy",Locale.getDefault());
                date = spf.format(newDate);
                holder.mTvMovieReleaseDate.setText(date);
            } catch (ParseException e) {
                e.printStackTrace();
                holder.mTvMovieReleaseDate.setText(currentMovie.getReleaseDate());
            }
        }
        String popularity = currentMovie.getVoteAverage()+"";
        holder.mTvPopularity.setText(popularity);
        manager.load(Constants.BASE_URL_ICON +currentMovie.getPosterPath()).into(holder.mIvMovieIcon);
        holder.mClRootLayout.setOnClickListener(view -> {
            recyclerViewItemClickListener.onItemClick(currentMovie.getId());
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder{
        private ImageView mIvMovieIcon;
        private TextView mTvMovieTitle;
        private TextView mTvMovieReleaseDate;
        private TextView mTvPopularity;
        private ConstraintLayout mClRootLayout;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mClRootLayout = itemView.findViewById(R.id.cl_root_view);
            mIvMovieIcon = itemView.findViewById(R.id.iv_movie_icon);
            mTvMovieTitle = itemView.findViewById(R.id.tv_movie_name);
            mTvMovieReleaseDate = itemView.findViewById(R.id.tv_release_date);
            mTvPopularity = itemView.findViewById(R.id.tv_popularity);
        }
    }

    public void setUpData(List<Movies> response){
        dataList.clear();
        dataList.addAll(response);
        notifyDataSetChanged();
    }
}

