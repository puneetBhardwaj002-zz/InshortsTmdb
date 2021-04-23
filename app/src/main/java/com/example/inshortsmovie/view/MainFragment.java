package com.example.inshortsmovie.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inshortsmovie.R;
import com.example.inshortsmovie.models.MoviesResponse;
import com.example.inshortsmovie.models.Resource;
import com.example.inshortsmovie.utils.Constants;
import com.example.inshortsmovie.view.adapter.MovieAdapter;
import com.example.inshortsmovie.viewmodel.MainFragmentViewModel;
import com.example.inshortsmovie.viewmodel.ViewModelProvidersFactory;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class MainFragment extends DaggerFragment {

    @Inject
    ViewModelProvidersFactory viewModelProvidersFactory;

    @Inject
    MovieAdapter movieAdapter;
    private RecyclerView mRvMoviesNowPlaying;
    private ProgressBar mPbLoader;
    private TextView mTvErrorMessage;
    private ImageView mIvErrorIcon;
    private Group mGErrorContent;
    private TextInputLayout mTILSearchBanner;
    private TextInputEditText mTIETSearchBanner;
    private MainFragmentViewModel viewModel;
    private TextWatcher mTWSearchText;
    private int type;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRvMoviesNowPlaying = view.findViewById(R.id.rv_movies_list);
        mPbLoader = view.findViewById(R.id.pb_loader);
        mTvErrorMessage = view.findViewById(R.id.tv_error_text);
        mIvErrorIcon = view.findViewById(R.id.iv_no_content_found);
        mGErrorContent = view.findViewById(R.id.g_info_content);
        mTILSearchBanner = view.findViewById(R.id.til_search_banner);
        mTIETSearchBanner = view.findViewById(R.id.tiet_search);
        viewModel = new ViewModelProvider(this,viewModelProvidersFactory).get(MainFragmentViewModel.class);
        if (getArguments() != null) {
            type = getArguments().getInt(Constants.FRAGMENT_TYPE);
        }
        initRecyclerView();
        setUpTextWatcher();
    }

    private void setUpTextWatcher() {
        mTWSearchText = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().isEmpty()){
                    subscribeObservers();
                }else {
                    viewModel.getSearchMoviesResponse().removeObservers(getViewLifecycleOwner());
                    viewModel.getSearchMoviesResponse().observe(getViewLifecycleOwner(),MainFragment.this::handleMoviesResponse);
                    viewModel.searchMovies(editable.toString());
                }
            }
        };
        mTIETSearchBanner.addTextChangedListener(mTWSearchText);
    }

    private void subscribeObservers(){
        switch (type){
            case 0:
                //Removing previous lifecycle owner as fragment has its own lifecycle
                viewModel.getNowPlayingMoviesResponse().removeObservers(getViewLifecycleOwner());
                viewModel.getNowPlayingMoviesResponse().observe(getViewLifecycleOwner(),this::handleMoviesResponse);
                viewModel.fetchNowPlayingMovies();
                break;
            case 1:
                viewModel.getTrendingMoviesResponse().removeObservers(getViewLifecycleOwner());
                viewModel.getTrendingMoviesResponse().observe(getViewLifecycleOwner(), this::handleMoviesResponse);
                viewModel.fetchTrendingMovies();
                break;
            case 2:
                viewModel.getFavouriteMoviesResponse().removeObservers(getViewLifecycleOwner());
                viewModel.getFavouriteMoviesResponse().observe(getViewLifecycleOwner(), responseResource -> {
                    if(responseResource!=null){
                        switch (responseResource.status){
                            case SUCCESS:
                                if(responseResource.data!=null && responseResource.data.getResults()!=null && responseResource.data.getResults().size() > 0){
                                    toggleProgressBar(false);
                                    showHideErrorContent("",false);
                                    movieAdapter.setUpData(responseResource.data.getResults());
                                }else{
                                    showNoBookmarksFound();
                                }
                                break;
                            case LOADING:
                                toggleProgressBar(true);
                                break;
                            case ERROR:
                                break;
                        }
                    }else{
                        showNoBookmarksFound();
                    }
                });
                viewModel.fetchFavouriteMovies();
                break;
        }

    }

    private void handleMoviesResponse(Resource<MoviesResponse> moviesResponseResource) {
        if(moviesResponseResource!=null){
            switch (moviesResponseResource.status){
                case SUCCESS:
                    toggleProgressBar(false);
                    showHideErrorContent("",false);
                    if(moviesResponseResource.data!=null){
                        if(moviesResponseResource.data.getResults()!=null && moviesResponseResource.data.getResults().size() > 0){
                            movieAdapter.setUpData(moviesResponseResource.data.getResults());
                        }else{
                            showHideErrorContent(getString(R.string.no_movies_found),true);
                            setErrorIcon(R.drawable.ic_no_content_found);
                        }
                    }
                    break;
                case LOADING:
                    toggleProgressBar(true);
                    break;
                case ERROR:
                    toggleProgressBar(false);
                    showHideErrorContent(moviesResponseResource.message,true);
                    setErrorIcon(R.drawable.ic_no_content_found);
                    break;
            }
        }
    }

    private void showNoBookmarksFound() {
        toggleProgressBar(false);
        showHideErrorContent(getString(R.string.no_bookmarks_found),true);
        setErrorIcon(R.drawable.ic_bookmark);
    }

    private void setErrorIcon(int ic_bookmark) {
        mIvErrorIcon.setImageResource(ic_bookmark);
    }

    private void showHideErrorContent(String message,boolean shouldShow) {
        if(shouldShow){
            mRvMoviesNowPlaying.setVisibility(View.GONE);
            mTILSearchBanner.setVisibility(View.GONE);
            mGErrorContent.setVisibility(View.VISIBLE);
            mTvErrorMessage.setText(message);
        }else{
            mRvMoviesNowPlaying.setVisibility(View.VISIBLE);
            mTILSearchBanner.setVisibility(View.VISIBLE);
            mGErrorContent.setVisibility(View.GONE);
        }
    }

    private void initRecyclerView(){
        mRvMoviesNowPlaying.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvMoviesNowPlaying.setAdapter(movieAdapter);
    }

    private void toggleProgressBar(boolean isVisible){
        if(isVisible){
            mPbLoader.setVisibility(View.VISIBLE);
        }else{
            mPbLoader.setVisibility(View.GONE);
        }
    }

    public static MainFragment newInstance(int type){
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.FRAGMENT_TYPE,type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        subscribeObservers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTIETSearchBanner.removeTextChangedListener(mTWSearchText);
    }
}
