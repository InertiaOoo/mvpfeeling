package com.dfzt.mvpfeeling.base;

import android.app.Application;
import android.content.Context;

public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }


}
