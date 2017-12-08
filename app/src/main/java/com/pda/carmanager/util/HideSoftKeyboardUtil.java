package com.pda.carmanager.util;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

/**软键盘隐藏
 * Created by Administrator on 2017/12/7 0007.
 */

public class HideSoftKeyboardUtil {
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
