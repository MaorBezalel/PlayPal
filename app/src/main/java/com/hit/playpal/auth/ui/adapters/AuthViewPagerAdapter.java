package com.hit.playpal.auth.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.hit.playpal.auth.ui.fragments.LoginTabFragment;
import com.hit.playpal.auth.ui.fragments.SignupTabFragment;

public class AuthViewPagerAdapter extends FragmentStateAdapter {
    public final int FRAGMENT_COUNT = 2;

    public AuthViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int iPosition) {
        switch (iPosition) {
            case 0: return new LoginTabFragment();
            case 1: return new SignupTabFragment();
            default: return new LoginTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return FRAGMENT_COUNT;
    }
}
