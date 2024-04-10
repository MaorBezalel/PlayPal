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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.hit.playpal.R;
import com.hit.playpal.home.ui.adapters.recentchats.ChatsViewPagerAdapter;

public class ChatsFragment extends Fragment {
    private static final String TAG = "ChatsFragment";
    private SearchView mChatsSearchView;
    private ViewPager2 mChatsViewPager2;
    private TabLayout mChatsTabLayout;
    private FloatingActionButton mAddChatGroupButton;


    @Override
    public View onCreateView(@NonNull LayoutInflater iInflater, ViewGroup iContainer, Bundle iSavedInstanceState) {
        return iInflater.inflate(R.layout.fragment_recent_chats, iContainer, false);
    }

    @Override
    public void onViewCreated(@NonNull View iView, Bundle iSavedInstanceState) {
        super.onViewCreated(iView, iSavedInstanceState);

        initChatsSearchView(iView);
        initChatsViewPager2(iView);
        initChatsTabLayout(iView);
        initAddChatGroupButton(iView);
    }

    private void initChatsSearchView(@NonNull View iView) {
        mChatsSearchView = iView.findViewById(R.id.searchview_chats);

        mChatsSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String iQuery) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String iNewText) {
                return false;
            }
        });
    }

    private void initChatsViewPager2(@NonNull View iView) {
        mChatsViewPager2 = iView.findViewById(R.id.viewpager2_chats);
        mChatsViewPager2.setAdapter(new ChatsViewPagerAdapter(this));
    }

    private void initChatsTabLayout(@NonNull View iView) {
        mChatsTabLayout = iView.findViewById(R.id.tablayout_chats);

        mChatsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab iTab) {
                mChatsViewPager2.setCurrentItem(iTab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab iTab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab iTab) {
            }
        });

        mChatsViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int iPosition) {
                mChatsTabLayout.selectTab(mChatsTabLayout.getTabAt(iPosition));
            }
        });
    }

    private void initAddChatGroupButton(@NonNull View iView) {
        mAddChatGroupButton = iView.findViewById(R.id.fab_chats_add_group_chat);
        mAddChatGroupButton.setOnClickListener(this::handleAddChatGroupButtonClick);
    }

    private void handleAddChatGroupButtonClick(View iView) {
        Log.i(TAG, "handleAddChatGroupButtonClick: Add chat group button clicked");
        CreateGroupChatRoomDialogFragment dialog = new CreateGroupChatRoomDialogFragment();
        dialog.show(getChildFragmentManager(), "CreateGroupChatRoomDialogFragment");
    }

    private void handleSuccessfulGroupChatRoomCreation() {
        // I need to refresh the recycler views in the AllChatsFragment and the GroupChatsFragment that are displayed by the ViewPager2
    }
}