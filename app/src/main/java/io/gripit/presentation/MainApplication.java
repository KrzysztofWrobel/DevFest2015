package io.gripit.presentation;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by krzysztofwrobel on 27/11/15.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
        FirebaseApi.getFirebaseSurveys().keepSynced(true);
    }
}
