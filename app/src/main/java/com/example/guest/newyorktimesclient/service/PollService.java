    package com.example.guest.newyorktimesclient.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.guest.newyorktimesclient.BuildConfig;
import com.example.guest.newyorktimesclient.R;
import com.example.guest.newyorktimesclient.api.NytApi;
import com.example.guest.newyorktimesclient.app.NewsApp;
import com.example.guest.newyorktimesclient.di.components.DaggerServiceComponent;
import com.example.guest.newyorktimesclient.di.modules.ServiceModule;
import com.example.guest.newyorktimesclient.mvp.model.LatestModel.Response;
import com.example.guest.newyorktimesclient.ui.activities.MainActivity;
import com.example.guest.newyorktimesclient.utils.QueryPreferences;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class PollService extends IntentService { //todo UNSUBSCRIBE
    private static final String TAG = "PollService";

    @Inject
    protected NytApi apiService;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String title;
    private SharedPreferences prefs;

    public PollService() {
        super(TAG);
    }

    public static final long POLL_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = new Intent(context, PollService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), POLL_INTERVAL, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i = new Intent(context, PollService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.d(TAG, "onHandleIntent: ");
            resolveDaggerDependencies();
            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            boolean f = isLastMappedResultMatchingActual();
            Resources r = getResources();
            PendingIntent pi = PendingIntent.getActivity(this, 0,
                    new Intent(this, MainActivity.class), 0);

            Notification notification = new NotificationCompat.Builder(this)
                    .setTicker(r.getString(R.string.got_news)) //todo: actual
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(r.getString(R.string.got_news))
                    .setContentText(r.getString(R.string.got_news))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();

            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(0, notification);
        }

        prefs.edit()
                .putString(QueryPreferences.PREF_LAST_RESULT_ID, title)
                .apply();
    }


    protected void resolveDaggerDependencies() {
        DaggerServiceComponent.builder()
                .applicationComponent(((NewsApp) getApplication()).getApplicationComponent())
                .serviceModule(new ServiceModule())
                .build()
                .inject(this);
    }

    private boolean isLastMappedResultMatchingActual() {
        compositeDisposable.add(apiService.getNews(20, BuildConfig.API_KEY, 0)
                .map(Response::getNews)
                .subscribe(news -> title = news.get(0).getTitle()));
        return title.equals(prefs.getString(QueryPreferences.PREF_LAST_RESULT_ID, null)); //todo в каком потоке слушать?
    }
}
