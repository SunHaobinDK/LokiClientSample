package com.example.loki;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.loki.sdk.LokiListener;

/**
 * Created by zhangyong on 15-6-4.
 */
public class SampleListener extends LokiListener {

    private Context context;
    private Handler handler;

    private class ToastAction implements Runnable {
        private String toast;

        public ToastAction(String toast) {
            this.toast = toast;
        }

        @Override
        public void run() {
            Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
        }
    }

    public SampleListener(Context context) {
        this.context = context;
        this.handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public boolean onReferralBroadcast(Intent intent) {
        handler.post(new ToastAction("Received referral: " + intent.toString()));
        return false;
    }

    @Override
    public void onApplicationSwitch(String packageName, boolean isLauncher) {
        handler.post(new ToastAction("Application switch: " + packageName + (isLauncher ? " is launcher" : " not launcher")));
    }

    @Override
    public void onCleanNotification() {
        handler.post(new ToastAction("Clean all notifications"));
    }

    @Override
    public void onGooglePlayDownload(String packageName, int versionCode) {
        handler.post(new ToastAction("Start download " + packageName + " version " + versionCode));
    }
}
