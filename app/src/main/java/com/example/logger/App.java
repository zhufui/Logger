package com.example.logger;

import android.app.Application;

import com.lib_logger.AndroidLogAdapter;
import com.lib_logger.DiskLogAdapter;
import com.lib_logger.LogSwitch;
import com.lib_logger.Logger;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LogSwitch.getClog(this)) {
            Logger.addLogAdapter(new AndroidLogAdapter());
        }
        if (LogSwitch.getDlog(this)) {
            Logger.addLogAdapter(new DiskLogAdapter());
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
