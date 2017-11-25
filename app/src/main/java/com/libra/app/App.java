package com.libra.app;

import android.app.Application;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by libra on 2017/7/21.
 */

public class App extends Application {
    private static App app;

    public static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        try {
            JSONObject json = new JSONObject("{\"name\":\"1\"}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
