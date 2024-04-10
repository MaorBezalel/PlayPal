package com.hit.playpal.home.ui.adapters.notifications;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public interface IFriendRequestsNotificationsAdapter {
    void onFriendRequestAccept(String iNotifierUid, ProgressBar iProgressBar, TextView iAcceptedText, TextView iRejectedText);
    void onFriendRequestReject(String iNotifierUid, ProgressBar iProgressBar, TextView iAcceptedText, TextView iRejectedText);
}
