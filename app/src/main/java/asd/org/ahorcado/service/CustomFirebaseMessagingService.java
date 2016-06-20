package asd.org.ahorcado.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import asd.org.ahorcado.R;
import asd.org.ahorcado.activities.VersusActivity;
import asd.org.ahorcado.helpers.UserAdapter;

public class CustomFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = CustomFirebaseMessagingService.class.getSimpleName();

    private RequestQueue mRequestQueue;

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Map<String, String> data = remoteMessage.getData();
        sendNotification(data.get("messageFrom"), Integer.parseInt(data.get("message")));
    }

    private void sendNotification(String opponentName, int opponentId) {
        Intent intent = new Intent(this, VersusActivity.class);
        intent.putExtra(UserAdapter.COLUMN_NAME, opponentName);
        intent.putExtra(UserAdapter.COLUMN_ID, opponentId);
        intent.putExtra(UserAdapter.COLUMN_MY_ID, opponentId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(getString(R.string.challenge_title))
                .setContentText(String.format(getString(R.string.challenger), opponentName))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}