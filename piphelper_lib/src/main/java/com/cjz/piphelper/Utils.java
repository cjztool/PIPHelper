package com.cjz.piphelper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

/**
 * Created by cjz on 2018/6/29.
 */
public class Utils {

    private static int screenHeight = 0;
    private static int screenWidth = 0;
    private static int statusBarHeight = 0;

    public static int getStatusBarHeight(Context context) {
        if (statusBarHeight > 0) {
            return statusBarHeight;
        }
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }


    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        if (screenWidth > 0) {
            return screenWidth;
        }

        if (context == null) {
            return 0;
        }
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        return screenWidth;
    }

    /**
     * 获取屏幕高度,是否包含导航栏高度
     */
    public static int getScreenHeight(Context mContext, boolean isIncludeNav) {
        if (mContext == null) {
            return 0;
        }
        int screenHeight = getScreenHeight(mContext);
        if (isIncludeNav) {
            return screenHeight;
        } else {
            return screenHeight - getNavigationBarHeight(mContext);
        }
    }

    /**
     * 获取屏幕高(包括底部虚拟按键)
     *
     * @param mContext
     * @return
     */
    public static int getScreenHeight(Context mContext) {
        if (screenHeight > 0) {
            return screenHeight;
        }

        if (mContext == null) {
            return 0;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        //WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = getWindowManager(mContext).getDefaultDisplay();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealMetrics(displayMetrics);
            } else {
                display.getMetrics(displayMetrics);
            }
            screenHeight = displayMetrics.heightPixels;
        } catch (Exception e) {
            screenHeight = display.getHeight();
        }
        return screenHeight;
    }


    /**
     * 获取WindowManager。
     */
    public static WindowManager getWindowManager(Context mContext) {
        if (mContext == null) {
            return null;
        }
        return (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    }

    /**
     * 获取NavigationBar的高度
     */
    public static int getNavigationBarHeight(Context mContext) {
        if (!hasNavigationBar(mContext)) {
            return 0;
        }
        Resources resources = mContext.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 是否存在NavigationBar
     */
    public static boolean hasNavigationBar(Context mContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = getWindowManager(mContext).getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.x != size.x || realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(mContext).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return !(menu || back);
        }
    }

}
