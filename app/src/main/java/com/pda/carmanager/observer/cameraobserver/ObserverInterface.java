package com.pda.carmanager.observer.cameraobserver;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public interface ObserverInterface {
    void addObservered(ObservableInterface observable);

    void removeObservered();

    void startSetPhoto(Bitmap bitmap);
}
