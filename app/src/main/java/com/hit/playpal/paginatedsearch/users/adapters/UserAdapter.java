package com.hit.playpal.paginatedsearch.users.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.Query;
import com.hit.playpal.R;
import com.hit.playpal.paginatedsearch.users.utils.IBindableUser;
import com.squareup.picasso.Picasso;

public class UserAdapter<T> extends FirestorePagingAdapter<T, UserAdapter.UserViewHolder> {
    private static final int PAGE_SIZE = 20;
    private static final int PAGE_PREFETCH_DISTANCE = 5;
    private Query mBaseQuery;
    protected String mUserPrefixPath;
    private static final PagingConfig PAGING_CONFIG = new PagingConfig(PAGE_SIZE, PAGE_PREFETCH_DISTANCE, false);
    private final LifecycleOwner mCurrentLifecycleOwner;
    private final IBindableUser<T> mBindableUser;
    private final IUserAdapter mUserAdapter;
    private final Class<T> mItemClass;

    public UserAdapter(
            IUserAdapter iUserAdapter,
            IBindableUser<T> iBindableUser,
            LifecycleOwner iOwner,
            Query iBaseQuery,
            Class<T> iItemClass) {
        super(new FirestorePagingOptions.Builder<T>()
                .setLifecycleOwner(iOwner)
                .setQuery(
                        iBaseQuery,
                        PAGING_CONFIG,
                        iItemClass)
                .build());

        mCurrentLifecycleOwner = iOwner;
        mBaseQuery = iBaseQuery;
        mBindableUser = iBindableUser;
        mUserAdapter = iUserAdapter;
        mItemClass = iItemClass;
    }

    @NonNull
    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder iUserViewHolder, int i, @NonNull T iItem) {
        String userImageUrl = mBindableUser.getUserImage(iItem);
        if (userImageUrl == null || userImageUrl.isEmpty()) {
            iUserViewHolder.userImage.setImageResource(R.drawable.ic_home_nav_myprofile);
        } else {
            Picasso.get().load(userImageUrl).into(iUserViewHolder.userImage);
        }
        iUserViewHolder.userName.setText(mBindableUser.getUsername(iItem));

        iUserViewHolder.userCard.setOnClickListener(v -> {
            mUserAdapter.onUserClick(mBindableUser.getUserId(iItem));
        });
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup iParent, int iViewType) {
        View view = LayoutInflater.from(iParent.getContext()).inflate(R.layout.fragment_search_user_card, iParent, false);
        return new UserViewHolder(view);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public final CardView userCard;
        public final ImageView userImage;
        public final TextView userName;

        public UserViewHolder(View view) {
            super(view);
            userCard = view.findViewById(R.id.userCard);
            userImage = view.findViewById(R.id.fragment_search_users_image);
            userName = view.findViewById(R.id.fragment_search_users_displayname);
        }
    }


    public void applyFilters(String iUserName)
    {
        if(iUserName == null || iUserName.isEmpty())
        {
            super.updateOptions(new FirestorePagingOptions.Builder<T>()
                    .setLifecycleOwner(mCurrentLifecycleOwner)
                    .setQuery(
                            mBaseQuery,
                            PAGING_CONFIG,
                            mItemClass)
                    .build());
        }
        else
        {
            Query filteredQuery = mBaseQuery
                    .orderBy(mUserPrefixPath + "username")
                    .startAt(iUserName)
                    .endAt(iUserName + "\uf8ff");

            super.updateOptions(new FirestorePagingOptions.Builder<T>()
                    .setLifecycleOwner(mCurrentLifecycleOwner)
                    .setQuery(
                            filteredQuery,
                            PAGING_CONFIG,
                            mItemClass)
                    .build());
        }
    }
}
