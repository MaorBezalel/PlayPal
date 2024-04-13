package com.hit.playpal.home.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hit.playpal.R;
import com.hit.playpal.home.ui.adapters.search.SearchViewPagerAdapter;

public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";

    private ViewPager2 mSearchViewPager2;
    private TabLayout mSearchTabLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater iInflater, ViewGroup iContainer, Bundle iSavedInstanceState) {
        return iInflater.inflate(R.layout.fragment_search, iContainer, false);
    }

    @Override
    public void onViewCreated(@NonNull View iView, Bundle iSavedInstanceState) {
        super.onViewCreated(iView, iSavedInstanceState);

        initSearchViewPager2(iView);
        initSearchTabLayout(iView);
    }

    private void initSearchViewPager2(@NonNull View iView) {
        mSearchViewPager2 = iView.findViewById(R.id.viewpager2_search);
        mSearchViewPager2.setAdapter(new SearchViewPagerAdapter(this));
    }

    private void initSearchTabLayout(@NonNull View iView) {
        mSearchTabLayout = iView.findViewById(R.id.tablayout_search);

        mSearchTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab iTab) {
                mSearchViewPager2.setCurrentItem(iTab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab iTab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab iTab) {
            }
        });

        mSearchViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int iPosition) {
                mSearchTabLayout.selectTab(mSearchTabLayout.getTabAt(iPosition));
            }
        });
    }
}