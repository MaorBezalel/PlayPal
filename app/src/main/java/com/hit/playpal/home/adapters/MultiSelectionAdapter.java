package com.hit.playpal.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hit.playpal.R;

import java.util.Arrays;
import java.util.List;

public class MultiSelectionAdapter extends BaseAdapter {
    private Context mContext;
    private List<CharSequence> mItemList;
    private boolean[] mCheckedItems;
    private String mPlaceholder;

    public MultiSelectionAdapter(Context iContext, List<CharSequence> iItemList, String iPlaceholder) {

        this.mContext = iContext;
        this.mItemList = iItemList;
        this.mPlaceholder = iPlaceholder;
        this.mCheckedItems = new boolean[iItemList.size()];
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public CharSequence getItem(int iPosition) {
        return mItemList.get(iPosition);
    }

    @Override
    public long getItemId(int iPosition) {
        return iPosition;
    }

    @Override
    public boolean isEnabled(int iPosition) {
        return iPosition != 0;
    }


        @NonNull
        @Override
        public View getView(final int iPosition, View iConvertView, ViewGroup iParent) {
            ViewHolder viewHolder;

            if (iConvertView == null)
            {
                iConvertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_search_games_filter_item, iParent, false);

                viewHolder = new ViewHolder();
                viewHolder.textView = iConvertView.findViewById(R.id.textView);
                viewHolder.checkBox = iConvertView.findViewById(R.id.checkBox);

                iConvertView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) iConvertView.getTag();
            }

            if (iPosition == 0)
            {
                viewHolder.textView.setText(mPlaceholder);
                viewHolder.textView.setVisibility(View.VISIBLE);
                viewHolder.checkBox.setVisibility(View.GONE);
            }
            else
            {
                viewHolder.checkBox.setText(mItemList.get(iPosition - 1));
                viewHolder.checkBox.setChecked(mCheckedItems[iPosition - 1]);
                viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCheckedItems[iPosition - 1] = ((CheckBox) v).isChecked();
                    }
                });
                viewHolder.checkBox.setVisibility(View.VISIBLE);
                viewHolder.textView.setVisibility(View.GONE);
            }

            return iConvertView;
        }


    private static class ViewHolder {
        TextView textView;
        CheckBox checkBox;
    }



    public boolean[] getCheckedItems() {
        return mCheckedItems;
    }

    public void uncheckAllItems() {
        Arrays.fill(mCheckedItems, false);
        notifyDataSetChanged();
    }
}