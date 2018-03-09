package com.example.guest.newyorktimesclient.service.impl1;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.guest.newyorktimesclient.R;
import com.example.guest.newyorktimesclient.mvp.model.Article;
import com.example.guest.newyorktimesclient.utils.NetworkChecker;
import com.example.guest.newyorktimesclient.utils.QueryPreferences;

import java.util.List;

/**
 * Created by l1maginaire on 3/9/18.
 */

public class PollService {
    private static final String TAG = "PollService";
    public static final long POLL_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;

    public static void setServiceAlarm(Context context, boolean isOn) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            PollJobService.setServiceAlarm(context, isOn);
        } else {
            PollAlarmService.setServiceAlarm(context, isOn);
        }
    }

    public static boolean isServiceAlarmOn(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return PollJobService.isServiceAlarmOn(context);
        } else {
            return PollAlarmService.isServiceAlarmOn(context);
        }
    }

    public static void doWork(Context context) {
        if (!(NetworkChecker.isNetAvailable(context))) {
            return;
        }

        String query = QueryPreferences.getStoredQuery(context);
        String lastResultTitle = QueryPreferences.getLastResultTitle(context);
//        List<Article> items = (query == null) ? new FlickrFetchr().fetchRecentPhotos() : new FlickrFetchr().searchPhotos(query); //TODO
//        if (items.size() == 0) {
//            return;
//        }

        String resultTitle = items.get(0).getTitle();
        if (resultTitle.equals(lastResultTitle)) {
            Log.i(TAG, "Got an old result: " + resultTitle);
        } else {
            Log.i(TAG, "Got a new result: " + resultTitle);

            Resources resources = context.getResources();
            Intent i = PhotoGalleryActivity.newIntent(context);
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);

            Notification notification = new NotificationCompat.Builder(context)
                    .setTicker(resources.getString(R.string.new_pictures_title))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(resources.getString(R.string.new_pictures_title))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(0, notification);
        }

        QueryPreferences.setLastResultId(context, resultTitle);
    }
}