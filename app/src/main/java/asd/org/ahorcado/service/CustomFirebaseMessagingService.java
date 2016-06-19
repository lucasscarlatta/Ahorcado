package asd.org.ahorcado.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import asd.org.ahorcado.R;
import asd.org.ahorcado.activities.VersusActivity;
import asd.org.ahorcado.helpers.UserAdapter;
import asd.org.ahorcado.utils.MySharedPreference;

public class CustomFirebaseMessagingService extends FirebaseMessagingService{

    private static final String TAG = CustomFirebaseMessagingService.class.getSimpleName();

    private RequestQueue mRequestQueue;

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        final Context appContext = this.getApplicationContext();
        mRequestQueue = CustomVolleyRequestQueue.getInstance(appContext)
                .getRequestQueue();
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        final ProgressDialog progressDialog = new ProgressDialog(appContext);
        progressDialog.setMessage(getString(R.string.processing));
        progressDialog.setCancelable(false);
        progressDialog.show();
        String url = MySharedPreference.PREFIX_URL + "installations/findByToken";
        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method
                .GET, url,
                new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    sendNotification(response.getString("userFrom"), response.getInt("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(appContext, e.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
                progressDialog.dismiss();
                Toast.makeText(appContext, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        jsonRequest.setTag(MySharedPreference.REQUEST_GET_TAG);
        mRequestQueue.add(jsonRequest);
    }

    private void sendNotification(String opponentName, int opponentId ) {
        Intent intent = new Intent(this, VersusActivity.class);
        intent.putExtra(UserAdapter.COLUMN_NAME, opponentName);
        intent.putExtra(UserAdapter.COLUMN_ID, opponentId);
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