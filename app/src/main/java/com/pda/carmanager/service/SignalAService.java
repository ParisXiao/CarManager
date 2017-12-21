package com.pda.carmanager.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Admin on 2017/12/21.
 */

public class SignalAService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new SignalABinder(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
