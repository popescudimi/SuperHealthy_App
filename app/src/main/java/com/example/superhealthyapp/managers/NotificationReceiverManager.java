package com.example.superhealthyapp.managers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.superhealthyapp.R;

public class NotificationReceiverManager extends BroadcastReceiver {
    private static final long[] VIBRATION_PATTERN = {200, 300, 200, 300};
    private static final int LIGHT_COLOR = Color.GREEN;
    private NotificationManager nM = null;
    private boolean notificationArea;
    private boolean vibration;
    private Uri soundUri;
    int notificationId = 30;


    @Override
    public void onReceive(Context context, Intent intent ) {
        if (nM == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return;
        else{
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("alert_content");
        String channelId = "channel_id";
        CharSequence channelName = "channel";
        String description = "AndStatus, " + channelName;
        NotificationChannel channel = new NotificationChannel(channelId, channelName,
                NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            channel.setDescription(description);
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
        // Configure the notification channel.
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        // Sets the notification light color for notifications posted to this
        // channel, if the device supports this feature.
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(
                new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notificationManager.createNotificationChannel(channel);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.splash_logo)
                .setContentTitle(title)
                .setContentText(content)
                .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" + context.getPackageName() + "/raw/notify"));
        notificationManager.notify(notificationId, builder.build());
    }

}}
