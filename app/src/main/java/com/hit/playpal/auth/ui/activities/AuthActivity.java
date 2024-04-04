package com.hit.playpal.auth.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.hit.playpal.R;
import com.hit.playpal.auth.ui.adapters.AuthViewPagerAdapter;
import com.hit.playpal.auth.ui.fragments.ForgotPasswordFragment;
import com.hit.playpal.auth.ui.viewmodels.AuthViewModel;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.home.ui.activities.HomeActivity;
import com.hit.playpal.auth.ui.fragments.LoginTabFragment;
import com.hit.playpal.auth.ui.fragments.SignupTabFragment;
import com.hit.playpal.utils.CurrentlyLoggedUser;

/**
 * <p>This activity is the entry point of the application. It is responsible for handling the authentication process of the user.</p>
 * <p>It contains a view pager that holds the login and sign up fragments and a tab layout that allows the user to switch between the two fragments.</p>
 * <p>It also contains a view model that is responsible for handling the authentication process of the user.</p>
 * <p>After the user is authenticated, the activity will navigate to the home activity.</p>
 *
 * @see AuthViewModel
 * @see AuthViewPagerAdapter
 * @see LoginTabFragment
 * @see SignupTabFragment
 * @see ForgotPasswordFragment
 * @see HomeActivity
 */
public class AuthActivity extends AppCompatActivity {
    private static final String TAG = "AuthActivity";
    private AuthViewModel mViewModel;
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager2;
    private ForgotPasswordFragment mForgotPasswordFragment;
    private AuthViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        mViewModel.getUser().observe(this, this::handleAuthenticatedUser);

        initViewPagerAndItsAdapter();
        initTabLayoutAndHandleTabSelection();
    }

    private void handleAuthenticatedUser(User user) {

        CurrentlyLoggedUser.setCurrentlyLoggedUser(user);

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }

    private void initViewPagerAndItsAdapter() {
        // Initialize the view pager and its adapter and set the adapter to the view pager
        mViewPager2 = findViewById(R.id.viewpager2_auth);
        mViewPagerAdapter = new AuthViewPagerAdapter(this);
        mViewPager2.setAdapter(mViewPagerAdapter);
    }

    private void initTabLayoutAndHandleTabSelection() {
        mTabLayout = findViewById(R.id.tablayout_auth);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager2.setCurrentItem(tab.getPosition());
            }

            // Do nothing
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                mTabLayout.selectTab(mTabLayout.getTabAt(position));
            }
        });
    }

    public void initForgotPasswordFragment() {
        mForgotPasswordFragment = new ForgotPasswordFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.linearlayout_auth_login_and_signup, mForgotPasswordFragment).commit();
    }
}