package com.example.inshortsmovie.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.inshortsmovie.view.MainFragment;

public class MyFragmentPagerAdapter extends FragmentStateAdapter {

    public MyFragmentPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return MainFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
