package com.multipz.AdvoHub.util;


import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Admin on 28-06-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("", "From: " + remoteMessage.getFrom());

    }

   /* private void sendNotification(String ContactNo, String UserName, String BloodRequestId, String RequestId, String Address, String body, String title, String UserImage, String UserId, String BloodType) {

        Intent intent = new Intent(this, ReceiveNotificationActivity.class);
        intent.putExtra("ContactNo", ContactNo);
        intent.putExtra("UserName", UserName);
        intent.putExtra("RequestId", BloodRequestId);
        intent.putExtra("Address", Address);
        intent.putExtra("Message", body);
        intent.putExtra("UserImage", UserImage);
        intent.putExtra("UserId", UserId);
        intent.putExtra("BloodType", BloodType);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new
                NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setSmallIcon(R.mipmap.ic_icon);
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_icon);
        }

        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendNotificationBirthday(String body, String title, String image) {

        Intent intent = new Intent(this, BirthdayActivity.class);
        intent.putExtra("body", body);
        intent.putExtra("title", title);
        intent.putExtra("img", image);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new
                NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setSmallIcon(R.mipmap.ic_icon);
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_icon);
        }

        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }*/

}
