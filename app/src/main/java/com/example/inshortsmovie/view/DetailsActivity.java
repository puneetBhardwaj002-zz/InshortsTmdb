package com.example.inshortsmovie.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.RequestManager;
import com.example.inshortsmovie.BaseActivity;
import com.example.inshortsmovie.R;
import com.example.inshortsmovie.models.MovieDetails;
import com.example.inshortsmovie.utils.AppUtils;
import com.example.inshortsmovie.utils.Constants;
import com.example.inshortsmovie.viewmodel.DetailsViewModel;
import com.example.inshortsmovie.viewmodel.ViewModelProvidersFactory;

import javax.inject.Inject;

public class DetailsActivity extends BaseActivity implements View.OnClickListener {

    @Inject
    ViewModelProvidersFactory viewModelProvidersFactory;
    @Inject
    RequestManager manager;

    private DetailsViewModel detailsViewModel;
    int movieId;
    private ProgressBar mPbLoader;
    private TextView mTvMovieReleasedYear;
    private TextView mTvMovieTitle;
    private TextView mTvMovieGenre;
    private TextView mTvMovieRating;
    private TextView mTvMovieShare;
    private TextView mTvMovieFavourite;
    private TextView mTvDescriptionText;
    private TextView mTvMovieRevenue;
    private TextView mTvMovieBudget;
    private TextView mTvMovieReleaseState;
    private TextView mTvMovieRuntime;
    private ImageView mIvMoviePoster;
    private ImageView mIvMovieBackdrop;
    private boolean isFavourite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initViews();
        setOnClickListeners();
        detailsViewModel = new ViewModelProvider(this,viewModelProvidersFactory).get(DetailsViewModel.class);
        if(getIntent()!=null){
            if(getIntent().getData()!=null){
                if(getIntent().getData().getQueryParameter("movieId")!=null){
                    movieId = Integer.parseInt(getIntent().getData().getQueryParameter("movieId"));
                }else{
                    finish();
                }
            }else{
                movieId = getIntent().getIntExtra(Constants.MOVIE_ID,0);
            }
        }
        subscribeObservers();
    }

    private void setOnClickListeners() {
        mTvMovieShare.setOnClickListener(this);
        mTvMovieFavourite.setOnClickListener(this);
    }

    private void initViews() {
        mPbLoader = findViewById(R.id.pb_movie_details);
        mTvMovieTitle = findViewById(R.id.tv_movie_detail_title);
        mTvMovieReleasedYear = findViewById(R.id.tv_movie_details_year);
        mTvMovieGenre = findViewById(R.id.tv_movie_detail_genre);
        mTvMovieRating = findViewById(R.id.tv_movie_detail_rating);
        mTvMovieShare = findViewById(R.id.tv_movie_detail_share);
        mTvMovieFavourite = findViewById(R.id.tv_movie_detail_bookmark);
        mTvDescriptionText = findViewById(R.id.tv_movie_detail_description_text);
        mTvMovieRevenue = findViewById(R.id.tv_movie_detail_revenue);
        mTvMovieBudget = findViewById(R.id.tv_movie_detail_budget);
        mTvMovieReleaseState = findViewById(R.id.tv_movie_detail_status);
        mTvMovieRuntime = findViewById(R.id.tv_movie_detail_runtime);
        mIvMoviePoster = findViewById(R.id.iv_movie_poster);
        mIvMovieBackdrop = findViewById(R.id.iv_movie_backdrop);
    }

    private void subscribeObservers() {
        detailsViewModel.getDetailedMovieResponse().observe(this, movieDetailsResource -> {
            if(movieDetailsResource!=null){
                switch (movieDetailsResource.status){
                    case SUCCESS:
                        toggleProgressBar(false);
                        if(movieDetailsResource.data!=null){
                            if(movieDetailsResource.data.getId()!=-1){
                                setUpData(movieDetailsResource.data);
                            }else{
                                showAlertDialog();
                            }
                        }
                        break;
                    case LOADING:
                        toggleProgressBar(true);
                        break;
                    case ERROR:
                        toggleProgressBar(false);
                        showAlertDialog();
                        break;
                }
            }
        });
        detailsViewModel.fetchMovieDetails(movieId);
        detailsViewModel.getIsFavouriteMovieOrNot(movieId).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isFavourite) {
                DetailsActivity.this.isFavourite = isFavourite;
                updateFavouriteText();
            }
        });
        detailsViewModel.getFavouriteMovieDetails(movieId);
    }

    private void setUpData(MovieDetails data) {
        setTitleAndTextColor(data);
        setPosterAndBackdropImages(data);
        mTvMovieReleasedYear.setText(data.getReleaseDate().substring(0,4));
        if(data.getGenres().size() > 0 && !AppUtils.isNullOrEmptyString(data.getGenres().get(0).getName())){
            mTvMovieGenre.setText(data.getGenres().get(0).getName());
        }
        mTvMovieRating.setText(AppUtils.parseDouble(data.getVoteAverage()));
        mTvDescriptionText.setText(data.getOverview());
        mTvMovieRevenue.setText(getString(R.string.revenue,AppUtils.parseAmount(data.getRevenue())));
        mTvMovieBudget.setText(getString(R.string.budget,AppUtils.parseAmount(data.getBudget())));
        mTvMovieReleaseState.setText(getString(R.string.status,data.getStatus()));
        mTvMovieRuntime.setText(getString(R.string.runtime,AppUtils.parseRuntime(data.getRuntime())));
    }

    private void setPosterAndBackdropImages(MovieDetails data) {
        manager.load(Constants.BASE_URL_ICON+data.getBackdropPath()).into(mIvMovieBackdrop);
        manager.load(Constants.BASE_URL_ICON+data.getPosterPath()).into(mIvMoviePoster);
    }

    private void setTitleAndTextColor(MovieDetails details) {
        try {
            Bitmap drawableBitmap = manager.asBitmap().load(Constants.BASE_URL_ICON+details.getBackdropPath()).submit().get();
            Palette.from(drawableBitmap).generate(palette -> {
                if(palette!=null){
                    Palette.Swatch vibrant = palette.getVibrantSwatch();
                    if (vibrant != null) {
                        // Update the title TextView with the proper text color
                        mTvMovieTitle.setTextColor(vibrant.getTitleTextColor());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTvMovieTitle.setText(details.getOriginalTitle());
    }



    private void toggleProgressBar(boolean isVisible){
        if(isVisible){
            mPbLoader.setVisibility(View.VISIBLE);
        }else{
            mPbLoader.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_movie_detail_share:
                createDeeplinkAndShare();
                break;
            case R.id.tv_movie_detail_bookmark:
                markRemoveMovieAsFavourite();
                break;
        }
    }

    private void markRemoveMovieAsFavourite() {
        if(isFavourite){
            detailsViewModel.removeMovieFromFavourite(movieId);
        }else{
            detailsViewModel.markMovieAsFavourite(movieId);
        }
        isFavourite = !isFavourite;
        updateFavouriteText();
    }

    private void updateFavouriteText() {
        if(isFavourite){
            mTvMovieFavourite.setText(getString(R.string.favourite));
            mTvMovieFavourite.setTextColor(ContextCompat.getColor(this,R.color.color_f54842));
        }else{
            mTvMovieFavourite.setText(getString(R.string.add_to_favourites));
            mTvMovieFavourite.setTextColor(ContextCompat.getColor(this,R.color.color_ffffff));
        }
    }

    private void createDeeplinkAndShare() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String shareUrl = Constants.BASE_URL_SHARE+"?movieId="+movieId;
        sendIntent.putExtra(Intent.EXTRA_TEXT,shareUrl );
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Share to...");
        startActivity(shareIntent);
    }

    private void showAlertDialog(){
        if(!this.isFinishing()){
            try {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Error");
                alertDialogBuilder.setMessage(getString(R.string.something_went_wrong_n_no_details_found));
                alertDialogBuilder.setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> finish());
                alertDialogBuilder.show();
            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), "error showing dialog");
            }
        }
    }
}
