package com.adminlive.preetyadminpanel;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.adminlive.preetyadminpanel.global.Sharedpref;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ApplicationClass extends Application {
    private static final String TAG = "ApplicationClass";
    FirebaseFirestore db;
    private Context context;
    private static Sharedpref sharedpref;
    private SharedPreferences mPref;
    private static ApplicationClass instance;

    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseFirestore.getInstance();
        instance = this;
        context = getApplicationContext();
        sharedpref = new Sharedpref(getApplicationContext());
        createChannel();

        initZEGOSDK();


    }
    private void createChannel() {
        // Create channel to show notifications.
        String channelId  = getString(R.string.default_notification_channel_id);
        String channelName = getString(R.string.default_notification_channel_name);
        NotificationManager notificationManager =
                getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_HIGH));
        }
    }

    private void initZEGOSDK() {

//        ZEGOSDKManager.getInstance().initSDK(instance, ZEGOSDKKeyCenter.appID, ZEGOSDKKeyCenter.appSign);
//        ZEGOSDKManager.getInstance().enableZEGOEffects(true);
    }

    public static ApplicationClass getInstance() {
        return instance;
    }

    public static Sharedpref getSharedpref() {
        return sharedpref;
    }


    public static ApplicationClass getAppContext() {
        return instance;
    }
    public SharedPreferences preferences() {
        return mPref;
    }




    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            System.out.println("Call onTerminate456 TRIM_MEMORY_UI_HIDDEN");
            // The application's UI is no longer visible
            // Perform cleanup tasks here
//            saveData();
        }

        if (level == ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
            System.out.println("Call onTerminate456 TRIM_MEMORY_BACKGROUND");
            // The application's UI is no longer visible
            // Perform cleanup tasks here
        }

        if (level == ComponentCallbacks2.TRIM_MEMORY_COMPLETE) {
            System.out.println("Call onTerminate456 TRIM_MEMORY_COMPLETE");
            // The application's UI is no longer visible
            // Perform cleanup tasks here
        }if (level == ComponentCallbacks2.TRIM_MEMORY_MODERATE) {
            System.out.println("Call onTerminate456 TRIM_MEMORY_MODERATE");
            // The application's UI is no longer visible
            // Perform cleanup tasks here
        }

        if (level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL) {
            System.out.println("Call onTerminate456 TRIM_MEMORY_RUNNING_CRITICAL");
            // The application's UI is no longer visible
            // Perform cleanup tasks here
        }if (level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW) {
            System.out.println("Call onTerminate456 TRIM_MEMORY_RUNNING_LOW");
            // The application's UI is no longer visible
            // Perform cleanup tasks here
        }if (level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE) {
            System.out.println("Call onTerminate456 TRIM_MEMORY_RUNNING_MODERATE");
            // The application's UI is no longer visible
            // Perform cleanup tasks here
        }
    }

    private void saveData() {
        Log.i("checkmethod", "onDestroy:======123== ");
        // Create a new user data map
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Dave");
        user.put("email", "dave@example.com");

// Add a new document with a generated ID
        db.collection("Test145")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    Log.i("checkmethod", "DocumentSnapshot added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.i("checkmethod", "Error adding document", e);
                });

        Log.i("checkmethod", "onDestroy:======1234== ");

    }


}

