package com.pinky.sharerecipebook.repositories;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pinky.sharerecipebook.R;
import com.pinky.sharerecipebook.view.MainActivity;


public class MyFirebaseMessagingService extends FirebaseMessagingService{

    static MyFirebaseMessagingService INSTANCE;

    public static MyFirebaseMessagingService getInstance() {
        if (INSTANCE == null)
            INSTANCE = new MyFirebaseMessagingService();

        return INSTANCE;
    }

    final String TAG = "NotificationAdapter";
    final String CHANNEL_ID_1 = "id_1";
    final String CHANNEL_ID_2 = "id_2";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String title = "";
        String text = "";

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            // Pending intent to bring user to the app
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID_1);
            NotificationChannel channel = new NotificationChannel("id_1", "name_1",NotificationManager.IMPORTANCE_HIGH);

            if(remoteMessage.getData().get("type").equals("comment"))
            {
                title = getString(R.string.comment_notif_title);
                text = remoteMessage.getData().get("senderName") + " " + getString(R.string.comment_notif_has_commented) + " " + remoteMessage.getData().get("recipeName");
            }
            else if(remoteMessage.getData().get("type").equals("like"))
            {
                title = getString(R.string.like_notif_title);
                text = remoteMessage.getData().get("senderName") + " " + getString(R.string.like_notif_has_liked) + " " + remoteMessage.getData().get("recipeName");
                builder.setChannelId(CHANNEL_ID_2);
                channel = new NotificationChannel(CHANNEL_ID_2, "name_2",NotificationManager.IMPORTANCE_HIGH);
            }



            manager.createNotificationChannel(channel);

            builder.setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setSmallIcon(R.drawable.recipe_notif_icon)
                    .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(text));

            manager.notify(1, builder.build());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }
}