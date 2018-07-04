package com.cjz.piphelper.permission.impl;


import android.content.Context;
import android.os.Build;

import com.cjz.piphelper.permission.FloatingPermissionCompat;

/**
 * Android 6.0 以下的通用实现基类
 *
 * Created by linchaolong on 2016/12/26.
 */
public abstract class BelowApi23CompatImpl implements FloatingPermissionCompat.CompatImpl {

    @Override public boolean check(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            return FloatingPermissionCompat.checkOp(context, 24); // 悬浮窗权限的 op 值是 OP_SYSTEM_ALERT_WINDOW = 24;
        }
        return true;
    }

}
