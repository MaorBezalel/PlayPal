package com.hit.playpal.paginatedsearch.rooms.adapters;

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
import com.hit.playpal.paginatedsearch.rooms.enums.RoomFilterType;
import com.hit.playpal.paginatedsearch.rooms.utils.IBindableRoom;
import com.squareup.picasso.Picasso;

public class RoomAdapter<T> extends FirestorePagingAdapter<T, RoomAdapter.RoomViewHolder> {
    private static final int PAGE_SIZE = 20;
    private static final int PAGE_PREFETCH_DISTANCE = 5;
    private static final PagingConfig PAGING_CONFIG = new PagingConfig(PAGE_SIZE, PAGE_PREFETCH_DISTANCE, false);

    private final IRoomAdapter<T> mRoomAdapter;
    private final LifecycleOwner mCurrentLifecycleOwner;
    private final Query mBaseQuery;
    private final Class<T> mItemClass;
    private final IBindableRoom<T> mBindableRoom;

    public RoomAdapter(
            IRoomAdapter<T> iRoomAdapter,
            IBindableRoom<T> iBindable,
            LifecycleOwner iOwner,
            Class<T> itemClass,
            Query iBaseQuery) {
        super(new FirestorePagingOptions.Builder<T>()
                .setLifecycleOwner(iOwner)
                .setQuery(
                        iBaseQuery,
                        PAGING_CONFIG,
                        itemClass)
                .build());

        mRoomAdapter = iRoomAdapter;
        mBindableRoom = iBindable;
        mItemClass = itemClass;
        mCurrentLifecycleOwner = iOwner;
        mBaseQuery = iBaseQuery;
    }


    @Override
    protected void onBindViewHolder(@NonNull RoomViewHolder iRoomViewHolder, int i, @NonNull T iItem) {
            iRoomViewHolder.ROOM_NAME.setText(mBindableRoom.getRoomName(iItem));
            iRoomViewHolder.MEMBERS_COUNT.setText(mBindableRoom.getMembersCount(iItem) + " Members");

            if(mBindableRoom.getGameImage(iItem) != null && !mBindableRoom.getGameImage(iItem).isEmpty())
        {
            Picasso.get().load(mBindableRoom.getGameImage(iItem)).into(iRoomViewHolder.GAME_IMAGE);
        }
        else
        {
            Picasso.get().load(R.drawable.ic_home_nav_game).into(iRoomViewHolder.GAME_IMAGE);
        }


            if(mBindableRoom.getRoomImage(iItem) != null && !mBindableRoom.getRoomImage(iItem).isEmpty())
            {
                Picasso.get().load(mBindableRoom.getRoomImage(iItem)).into(iRoomViewHolder.ROOM_IMAGE);
            }
            else
            {
                Picasso.get().load(R.drawable.ic_home_nav_search_groupchats).into(iRoomViewHolder.ROOM_IMAGE);
            }


            iRoomViewHolder.ROOM_CARD.setOnClickListener(v -> {
                mRoomAdapter.onRoomClick(mBindableRoom.getRoomId(iItem));
            });
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup iParent, int iViewType) {
        View view = LayoutInflater.from(iParent.getContext()).inflate(R.layout.fragment_search_room_card, iParent, false);
        return new RoomAdapter.RoomViewHolder(view);
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        public final CardView ROOM_CARD;
        public final ImageView ROOM_IMAGE;
        public final ImageView GAME_IMAGE;
        public final TextView ROOM_NAME;
        public final TextView MEMBERS_COUNT;

        public RoomViewHolder(View iView) {
            super(iView);
            ROOM_CARD = iView.findViewById(R.id.roomCard);
            ROOM_IMAGE = iView.findViewById(R.id.fragment_search_room_card_roomImage);
            GAME_IMAGE =  iView.findViewById(R.id.fragment_search_room_card_gameImage);
            ROOM_NAME = iView.findViewById(R.id.fragment_search_room_card_name);
            MEMBERS_COUNT = iView.findViewById(R.id.fragment_search_room_card_members);
        }
    }

    public void applyFilters(String iQuery, RoomFilterType iRoomFilterType)
    {
        Query filteredQuery = mBaseQuery;

        switch(iRoomFilterType)
        {
            case BY_ROOM_NAME:
                filteredQuery = mBaseQuery
                        .orderBy("name")
                        .startAt(iQuery)
                        .endAt(iQuery+ "\uf8ff");
                break;
            case BY_GAME_NAME:
                filteredQuery = mBaseQuery
                        .orderBy("game.name")
                        .startAt(iQuery)
                        .endAt(iQuery + "\uf8ff");
                break;
            case NONE:
                break;
        }

        super.updateOptions(new FirestorePagingOptions.Builder<T>()
                .setLifecycleOwner(mCurrentLifecycleOwner)
                .setQuery(
                        filteredQuery,
                        PAGING_CONFIG,
                        mItemClass)
                .build());
    }

}
