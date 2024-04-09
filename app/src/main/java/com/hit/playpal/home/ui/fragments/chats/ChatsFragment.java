package com.hit.playpal.home.ui.fragments.chats;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.material.tabs.TabLayout;
import com.hit.playpal.R;
import com.hit.playpal.home.adapters.recentchats.ChatsViewPagerAdapter;

public class ChatsFragment extends Fragment {
    private static final String TAG = "ChatsFragment";
    private SearchView mSearchView;
    private ViewPager2 mViewPager2;
    private TabLayout mTabLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater iInflater, ViewGroup iContainer, Bundle iSavedInstanceState) {
        return iInflater.inflate(R.layout.fragment_recent_chats, iContainer, false);
    }

    @Override
    public void onViewCreated(View iView, Bundle iSavedInstanceState) {
        super.onViewCreated(iView, iSavedInstanceState);

        mSearchView = iView.findViewById(R.id.searchview_chats); // WORK IN PROCESS
        mViewPager2 = iView.findViewById(R.id.viewpager2_chats);
        mTabLayout = iView.findViewById(R.id.tablayout_chats);

        mViewPager2.setAdapter(new ChatsViewPagerAdapter(this));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab iTab) {
                mViewPager2.setCurrentItem(iTab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab iTab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab iTab) {
            }
        });

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int iPosition) {
                mTabLayout.selectTab(mTabLayout.getTabAt(iPosition));
            }
        });

        Log.d(TAG, "onViewCreated: ChatsFragment created");
    }
}