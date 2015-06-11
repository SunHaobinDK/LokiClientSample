package com.example.loki;

import android.app.Application;

import com.loki.sdk.LokiService;

/**
 * Created by zhangyong on 15-6-4.
 */
public class SampleApplication extends Application {
    @Override
    public void onCreate() {
        LokiService service = LokiService.getInstance(this);
        if (service != null) {
            service.registerListener(new SampleListener(this));
        }
    }
}
