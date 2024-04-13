package com.hit.playpal.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * An abstract scroll listener for RecyclerView to load more items as the user scrolls down.
 */
public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    /**
     * The minimum amount of items to have below your current scroll position before loading more.
     */
    private int mVisibleThreshold = 10;

    /**
     * The current offset index of data you have loaded.
     */
    private int mCurrentPage = 0;

    /**
     * The total number of items in the dataset after the last load.
     */
    private int mPreviousTotalItemCount = 0;

    /**
     * True if we are still waiting for the last set of data to load.
     */
    private boolean mLoading = true;

    /**
     * Sets the starting page index.
     */
    private int mStartingPageIndex = 0;

    /**
     * Sets the starting page index.
     */
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * Constructor for EndlessRecyclerViewScrollListener that supports LinearLayoutManager.
     *
     * @param iLayoutManager The layout manager of the current RecyclerView.
     */
    public EndlessRecyclerViewScrollListener(LinearLayoutManager iLayoutManager) {
        mLayoutManager = iLayoutManager;
    }

    /**
     * Constructor for EndlessRecyclerViewScrollListener that supports StaggeredGridLayoutManager.
     *
     * @param iLayoutManager The layout manager of the current RecyclerView.
     */
    public EndlessRecyclerViewScrollListener(@NonNull StaggeredGridLayoutManager iLayoutManager) {
        mLayoutManager = iLayoutManager;
        mVisibleThreshold = mVisibleThreshold * iLayoutManager.getSpanCount();
    }

    /**
     * Returns the last visible item position from an array of positions.
     *
     * @param iLastVisibleItemPositions The array of last visible item positions.
     * @return The last visible item position.
     */
    public int getLastVisibleItem(@NonNull int[] iLastVisibleItemPositions) {
        int maxSize = 0;

        for (int i = 0; i < iLastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = iLastVisibleItemPositions[i];
            }
            else if (iLastVisibleItemPositions[i] > maxSize) {
                maxSize = iLastVisibleItemPositions[i];
            }
        }

        return maxSize;
    }

    /**
     * Called when the RecyclerView has been scrolled. This will be called after the scroll has completed.
     *
     * @param iView The RecyclerView which scrolled.
     * @param iDx   The amount of horizontal scroll.
     * @param iDy   The amount of vertical scroll.
     */
    @Override
    public void onScrolled(@NonNull RecyclerView iView, int iDx, int iDy) {
        int lastVisibleItemPosition = getLastVisibleItemPosition();
        int totalItemCount = mLayoutManager.getItemCount();

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (isListInvalidated(totalItemCount)) {
            resetToInitialState();
            if (totalItemCount == 0) {
                mLoading = true;
            }
        }

        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (isLoadingFinished(totalItemCount)) {
            mLoading = false;
            mPreviousTotalItemCount = totalItemCount;
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (shouldLoadMoreData(lastVisibleItemPosition, totalItemCount)) {
            mCurrentPage++;
            onLoadMore(mCurrentPage, totalItemCount, iView);
            mLoading = true;
        }
    }

    /**
     * Determines the last visible item position based on the layout manager type.
     *
     * @return The last visible item position.
     */
    private int getLastVisibleItemPosition() {
        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            return getLastVisibleItem(lastVisibleItemPositions); // get maximum element within the list
        } else if (mLayoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }
        return 0;
    }

    /**
     * Checks if the list is invalidated, which is determined by the total item count being less than the previous total item count.
     *
     * @param iTotalItemCount The current total item count.
     * @return True if the list is invalidated, false otherwise.
     */
    private boolean isListInvalidated(int iTotalItemCount) {
        return iTotalItemCount < mPreviousTotalItemCount;
    }

    /**
     * Resets the state of the listener to its initial values.
     */
    private void resetToInitialState() {
        mCurrentPage = mStartingPageIndex;
        mPreviousTotalItemCount = 0;
    }

    /**
     * Checks if the loading of data has finished, which is determined by the total item count being greater than the previous total item count.
     *
     * @param iTotalItemCount The current total item count.
     * @return True if the loading has finished, false otherwise.
     */
    private boolean isLoadingFinished(int iTotalItemCount) {
        return mLoading && (iTotalItemCount > mPreviousTotalItemCount);
    }

    /**
     * Checks if more data should be loaded, which is determined by the last visible item position and the visible threshold.
     *
     * @param iLastVisibleItemPosition The last visible item position.
     * @param iTotalItemCount The current total item count.
     * @return True if more data should be loaded, false otherwise.
     */
    private boolean shouldLoadMoreData(int iLastVisibleItemPosition, int iTotalItemCount) {
        return !mLoading && (iLastVisibleItemPosition + mVisibleThreshold) > iTotalItemCount;
    }

    /**
     * Resets the state of the listener to its initial values.
     * This should be called whenever performing new searches.
     */
    public void resetState() {
        mCurrentPage = mStartingPageIndex;
        mPreviousTotalItemCount = 0;
        mLoading = true;
    }

    /**
     * Defines the process for actually loading more data based on page.
     *
     * @param iPage          The page to load.
     * @param iTotalItemsCount The total number of items in the dataset after the last load.
     * @param iView          The RecyclerView instance.
     */
    public abstract void onLoadMore(int iPage, int iTotalItemsCount, RecyclerView iView);
}
