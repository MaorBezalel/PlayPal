package com.hit.playpal.home.adapters.recentchats;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.hit.playpal.entities.chats.enums.ChatRoomType;
import com.hit.playpal.home.ui.fragments.chats.ChatsTabFragment;

public class ChatsViewPagerAdapter extends FragmentStateAdapter {
    public final int FRAGMENT_COUNT = 3;

    public ChatsViewPagerAdapter(@NonNull Fragment iFragment) {
        super(iFragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int iPosition) {
        switch (iPosition) {
            case 0: return new ChatsTabFragment(null); // All
            case 1: return new ChatsTabFragment(ChatRoomType.ONE_TO_ONE); // One to One
            case 2: return new ChatsTabFragment(ChatRoomType.GROUP); // Group
            default: return new ChatsTabFragment(null);
        }
    }

    @Override
    public int getItemCount() {
        return FRAGMENT_COUNT;
    }
}
