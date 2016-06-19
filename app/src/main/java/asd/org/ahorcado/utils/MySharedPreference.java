package asd.org.ahorcado.utils;

import android.content.Context;
import android.content.SharedPreferences;
public class MySharedPreference {

    private SharedPreferences prefs;
    private Context context;
    public static final String FIREBASE_CLOUD_MESSAGING = "fcm";
    public static final String SET_NOTIFY = "set_notify";

    public static String TOKEN_TO_SERVER = "deviceToken";
    public static String DEVICE_TYPE = "deviceType";
    public static String APP_ID = "appId";
    public static String HOST = "http://192.168.0.14:";
    public static String PORT = "3000";
    public static String API_NAME = "/api/";
    public static String PREFIX_URL = HOST + PORT + API_NAME;
    public static final String REQUEST_GET_TAG = "GetMainVolleyActivity";
    public static final String REQUEST_POST_TAG = "PostMainVolleyActivity";

    public MySharedPreference(Context context){
        this.context = context;
        prefs = context.getSharedPreferences(FIREBASE_CLOUD_MESSAGING, Context.MODE_PRIVATE);
    }

    public void saveNotificationSubscription(boolean value){
        SharedPreferences.Editor edits = prefs.edit();
        edits.putBoolean(SET_NOTIFY, value);
        edits.apply();
    }

    public boolean hasUserSubscribeToNotification(){
        return prefs.getBoolean(SET_NOTIFY, false);
    }

    public void clearAllSubscriptions(){
        prefs.edit().clear().apply();
    }

}