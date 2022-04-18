package com.tiptap.speedlauncher;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViews.RemoteResponse;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link SpeedLaunchWidgetConfigureActivity SpeedLaunchWidgetConfigureActivity}
 */
public class SpeedLaunchWidget extends AppWidgetProvider {

    private static final String BUTTON_PRESS_ACTION = "buttonPressAction";
    private static final String BUTTON_ID = "buttonId";
    private static final String BUTTON_LETTER = "buttonLetter";

    private RemoteViews remoteViews;

    public SpeedLaunchWidget() {

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        if (remoteViews == null)
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.speed_launch_widget);
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //Todo CharSequence widgetText = SpeedLaunchWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object

        for (Map.Entry<Integer, String> letter : Letters.getIterable()) {
            Integer buttonId = letter.getKey();
            PendingIntent pendingIntent = getButtonPendingIntent(context, buttonId, letter.getValue(), appWidgetId);
            remoteViews.setOnClickPendingIntent(buttonId, pendingIntent);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }



    private PendingIntent getButtonPendingIntent(Context context, int buttonId, String buttonLetter, int widgetId) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(BUTTON_PRESS_ACTION);
        intent.putExtra(BUTTON_ID, buttonId);
        intent.putExtra(BUTTON_LETTER, buttonLetter);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        return PendingIntent.getBroadcast(context, buttonId, intent, PendingIntent.FLAG_ONE_SHOT);
    }



    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(BUTTON_PRESS_ACTION)) {

            String letter = intent.getStringExtra(BUTTON_LETTER);
            int buttonId = intent.getIntExtra(BUTTON_ID, 0);

            //update
            Intent sendUpdateIntent = new Intent(context, SpeedLaunchWidget.class);
            sendUpdateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            int[] intArray = {intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0)};
            sendUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArray);
            context.sendBroadcast(sendUpdateIntent);

            List<LauncherActivityInfo> activityList = new ArrayList<>();
            LauncherApps launcherApps = (LauncherApps) context.getSystemService(Context.LAUNCHER_APPS_SERVICE);
            UserManager userManager = (UserManager) context.getSystemService(Context.USER_SERVICE);
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

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            SpeedLaunchWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}