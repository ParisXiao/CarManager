package com.pda.carmanager.observer.cameraobserver;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class VisitorObserver implements ObserverInterface {
    private List<ObservableInterface> list = new ArrayList<ObservableInterface>();

    private VisitorObserver() {
    }

    private static VisitorObserver visitorObserver;

    public static VisitorObserver init() {
        if (visitorObserver == null) {
            visitorObserver = new VisitorObserver();
        }
        return visitorObserver;
    }

    @Override
    public void addObservered(ObservableInterface observable) {
        list.add(observable);
    }

    @Override
    public void removeObservered() {
        list.clear();
    }

    @Override
    public void startSetPhoto(Bitmap bitmap) {
        for (ObservableInterface observableInterface : list) {
            observableInterface.setPhoto(bitmap);
        }
        removeObservered();
    }
}
