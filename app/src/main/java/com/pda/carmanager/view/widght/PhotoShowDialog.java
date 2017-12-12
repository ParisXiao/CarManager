package com.pda.carmanager.view.widght;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pda.carmanager.R;

import uk.co.senab.photoview.PhotoView;


/**
 * Created by Administrator on 2016/10/28.
 */
public class PhotoShowDialog {
    public static void showPhotoDialog(Context context, Bitmap bitmap) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        lp.alpha = 1f;
        window.setAttributes(lp);
        window.setContentView(R.layout.layout_photoview);
        PhotoView photoView = (PhotoView) window.findViewById(R.id.photoview);
        photoView.setImageBitmap(bitmap);
    }

    public static void showPhotoDialogForUrl(Context context, String url) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        lp.alpha = 1f;
        window.setAttributes(lp);
        window.setContentView(R.layout.layout_photoview);
        PhotoView photoView = (PhotoView) window.findViewById(R.id.photoview);
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(url, photoView);

    }

}
