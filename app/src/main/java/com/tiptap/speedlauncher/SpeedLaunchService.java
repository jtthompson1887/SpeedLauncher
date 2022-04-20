package com.tiptap.speedlauncher;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.tiptap.speedlauncher.SpeedLaunchWidget.BUTTON_ID;
import static com.tiptap.speedlauncher.SpeedLaunchWidget.BUTTON_LETTER;
import static java.util.stream.Collectors.joining;

@SuppressWarnings("SimplifyStreamApiCallChains")
public class SpeedLaunchService extends Service {

    //actions
    public static String WIDGET_PRESS_ACTION = "widgetPressAction";
    public static String UI_UPDATE_ACTION = "uiUpdateAction";

    //parameters
    private static final String CURRENT_LETTERS = "currentLetters";

    public SpeedLaunchService() {
    }

    private CountDownTimer countDownTimer;
    private List<String> letterList = new ArrayList<>();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);


        setupTimer();

        int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            Log.e(getClass().getSimpleName(), "No widget ID");
            stopSelf();
        }

        if (WIDGET_PRESS_ACTION.equals(intent.getAction())) {
            widgetPressAction(intent);
        }

        updateUI(widgetId);

        return START_STICKY;
    }

    private void updateUI(int... widgetId) {
        Intent intent = new Intent(this, SpeedLaunchWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetId);
        intent.putExtra(CURRENT_LETTERS, letterList.stream().collect(joining()));
    }

    private void updateAllUI() {
        AppWidgetManager appWidgetManager = SpeedLaunchService.this.getSystemService(AppWidgetManager.class);
        ComponentName componentName = new ComponentName(this, SpeedLaunchWidget.class);
        updateUI(appWidgetManager.getAppWidgetIds(componentName));
    }

    private void widgetPressAction(Intent intent) {
            int buttonId = intent.getIntExtra(BUTTON_ID, 0);
            String buttonLetter = intent.getStringExtra(BUTTON_LETTER);
            letterList.add(buttonLetter);
    }

    private void fetchAppList() {
        //get applications todo move
        List<LauncherActivityInfo> activityList = new ArrayList<>();
        LauncherApps launcherApps = (LauncherApps) this.getSystemService(Context.LAUNCHER_APPS_SERVICE);
        UserManager userManager = (UserManager) this.getSystemService(Context.USER_SERVICE);
        List<UserHandle> profiles = userManager.getUserProfiles();
        for (UserHandle profile : profiles) {
            //if (userManager.isUserRunning(profile)) {
            //activityList = launcherApps.getActivityList(null, profile);
//                    Log.d("username",userManager.getUserName());
            activityList.addAll(launcherApps.getActivityList(null, profile));
            //}
        }

        activityList.forEach((info) -> Log.d("app",info.getLabel().toString()));
    }

    private void setupTimer() {
        if (countDownTimer != null)
            return;
        countDownTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                countDownTimer = null;
                letterList.clear();
                updateAllUI();
                stopSelf();
            }
        };
    }
}