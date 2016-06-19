package asd.org.ahorcado.service;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import asd.org.ahorcado.models.TokenObject;
import asd.org.ahorcado.utils.MySharedPreference;

public class CustomFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = CustomFirebaseInstanceIDService.class.getSimpleName();
    private static String PREFIX_URL = MySharedPreference.HOST + MySharedPreference.PORT + MySharedPreference.API_NAME;

    private RequestQueue queue;
    private TokenObject tokenObject;
    private MySharedPreference mySharedPreference;

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Token Value: " + refreshedToken);
        sendTheRegisteredTokenToWebServer(refreshedToken);
    }

    private void sendTheRegisteredTokenToWebServer(final String token) {
        queue = Volley.newRequestQueue(getApplicationContext());
        mySharedPreference = new MySharedPreference(getApplicationContext());
        String url = PREFIX_URL + "installations";
        StringRequest stringPostRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                tokenObject = gson.fromJson(response, TokenObject.class);
                if (null == tokenObject) {
                    Toast.makeText(getApplicationContext(), "Can't send a token to the server", Toast.LENGTH_LONG).show();
                    mySharedPreference.saveNotificationSubscription(false);
                } else {
                    Toast.makeText(getApplicationContext(), "Token successfully send to server", Toast.LENGTH_LONG).show();
                    mySharedPreference.saveNotificationSubscription(true);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(MySharedPreference.APP_ID, "loopback-component-push-app");
                params.put(MySharedPreference.TOKEN_TO_SERVER, token);
                params.put(MySharedPreference.DEVICE_TYPE, "android");
                return params;
            }
        };
        queue.add(stringPostRequest);
    }
}
