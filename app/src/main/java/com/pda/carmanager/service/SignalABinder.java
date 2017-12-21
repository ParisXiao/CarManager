package com.pda.carmanager.service;

import android.os.Binder;

/**
 * Created by Admin on 2017/12/21.
 */

public class SignalABinder extends Binder {
    private SignalAService service;

    public SignalABinder(SignalAService service) {
        this.service = service;
    }

    public SignalAService getService() {
        return this.service;
    }
}
