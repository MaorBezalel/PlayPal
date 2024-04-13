package com.hit.playpal.home.ui.adapters.search;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.hit.playpal.paginatedsearch.users.fragments.UserSearchFragment;
import com.hit.playpal.paginatedsearch.rooms.fragments.RoomSearchFragment;

public class SearchViewPagerAdapter extends FragmentStateAdapter {
    private static final String TAG = "SearchViewPagerAdapter";
    private static final int NUM_PAGES = 2;
    private static final int SEARCH_USERS_PAGE_INDEX = 0;
    private static final int SEARCH_GROUPS_PAGE_INDEX = 1;

    public SearchViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case SEARCH_USERS_PAGE_INDEX:
                return new UserSearchFragment();
            case SEARCH_GROUPS_PAGE_INDEX:
                return new RoomSearchFragment();
            default:
                return new UserSearchFragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}