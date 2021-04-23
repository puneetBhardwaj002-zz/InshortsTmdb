package com.example.inshortsmovie.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.viewpager2.widget.ViewPager2;

import com.example.inshortsmovie.BaseActivity;
import com.example.inshortsmovie.R;
import com.example.inshortsmovie.models.RecyclerViewItemClickListener;
import com.example.inshortsmovie.utils.Constants;
import com.example.inshortsmovie.view.adapter.MyFragmentPagerAdapter;
import com.example.inshortsmovie.utils.AppUtils;
import com.example.inshortsmovie.utils.SingleScrollDirectionEnforcer;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements RecyclerViewItemClickListener {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Inject
    MyFragmentPagerAdapter pagerAdapter;

    @Inject
    SingleScrollDirectionEnforcer enforceSingleScrollDirection;

    private ViewPager2.OnPageChangeCallback onPageChangeCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.pager);
        AppUtils.enforceSingleScrollDirection(AppUtils.getRecyclerView(viewPager),enforceSingleScrollDirection);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(tabLayout,viewPager, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText(getString(R.string.now_playing_title));
                    break;
                case 1:
                    tab.setText(getString(R.string.trending_title));
                    break;
                case 2:
                    tab.setText(getString(R.string.bookmark_title));
                    break;
            }
        }).attach();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        };
        viewPager.registerOnPageChangeCallback(onPageChangeCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewPager.unregisterOnPageChangeCallback(onPageChangeCallback);
    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem() == 0){
            super.onBackPressed();
        }else{
            viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
        }
    }

    @Override
    public void onItemClick(int movieId) {
        Intent detailsIntent = new Intent(this,DetailsActivity.class);
        detailsIntent.putExtra(Constants.MOVIE_ID,movieId);
        startActivity(detailsIntent);
    }
}