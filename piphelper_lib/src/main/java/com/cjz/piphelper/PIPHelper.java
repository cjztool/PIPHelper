package com.cjz.piphelper;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.cjz.piphelper.permission.FloatingPermissionCompat;
import com.cjz.piphelper.view.DragView;

import java.lang.ref.WeakReference;

/**
 * Created by cjz on 2018/6/27.
 */
public class PIPHelper {


    enum ReMeeTAction {
        none,
        replaceNewByFloatOne,
        dismissFloatOne
    }

    private ReMeeTAction reMeeTAction = ReMeeTAction.none;

    private volatile static PIPHelper pipHelper;

    private WeakReference<View> targetRef;

    private int targetPos[];
    private int targetWidth, targetHeight;

    private boolean isFloating = false;
    public boolean isDraggable = true;

    private WindowManager windowManager;
//    private WindowManager.LayoutParams windowManagerLP;


    private View.OnKeyListener autoFloatListener;

    private PIPHelper() {
    }

    public static PIPHelper get() {
        if (pipHelper == null) {
            synchronized (PIPHelper.class) {
                if (pipHelper == null) {
                    pipHelper = new PIPHelper();
                    pipHelper.targetPos = new int[2];
                }
            }
        }
        return pipHelper;
    }


    public PIPHelper reMeetDoNothing() {
        reMeeTAction = ReMeeTAction.none;
        return pipHelper;
    }

    public PIPHelper reMeetDismissFloatOne() {
        reMeeTAction = ReMeeTAction.dismissFloatOne;
        return pipHelper;
    }

    public PIPHelper reMeetReplaceNewByFloatOne() {
        reMeeTAction = ReMeeTAction.replaceNewByFloatOne;
        return pipHelper;
    }

    public void initTarget(View target) {


        if (targetRef != null) {

            View oldTarget = targetRef.get();
            if (oldTarget != null) {

                if (target.getId() == oldTarget.getId()) { //same one?? how about list item


                    switch (reMeeTAction) {
                        case replaceNewByFloatOne:

                            if (isFloating) {
                                target.setVisibility(View.INVISIBLE);
                                ViewGroup.LayoutParams layoutParams = target.getLayoutParams();
                                ViewGroup newTargetParent = ((ViewGroup) target.getParent());
                                newTargetParent.removeView(target);
                                windowManager.removeViewImmediate((View) oldTarget.getParent());
                                ((ViewGroup) oldTarget.getParent()).removeView(oldTarget);
                                newTargetParent.addView(oldTarget, layoutParams);
                                target = oldTarget;
                            }
                            isFloating = false;

                            break;
                        case dismissFloatOne:

                            if (isFloating) {
                                windowManager.removeViewImmediate((View) oldTarget.getParent());
                            }
                            isFloating = false;

                            break;
                        case none:
                        default:
                            break;
                    }
                }
            }
        }

        targetRef = new WeakReference<>(target);
        //target.setTag(R.id.pip, "a");

        if (autoFloatListener != null) {
            target.setOnKeyListener(autoFloatListener);
            target.setFocusableInTouchMode(true);//响应按键事件
//            target.requestFocus();
        }
    }


    public PIPHelper floatView() {
        View target;

        if (!isFloating && targetRef != null && (target = targetRef.get()) != null) {

            final Context appContext = target.getContext().getApplicationContext();

            if (!FloatingPermissionCompat.get().check(appContext)) {

                new AlertDialog.Builder(target.getContext(), R.style.AlertDialogCustom).setTitle("悬浮窗权限未开启")
                        .setMessage("您没有授予" + appContext.getString(R.string.app_name) + "悬浮窗权限，悬浮功能将无法正常使用!")
                        .setPositiveButton("开启", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FloatingPermissionCompat.get().apply(appContext);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

            } else {

                target.getLocationOnScreen(targetPos);
                targetWidth = target.getWidth();
                targetHeight = target.getHeight();
                DragView container = initFloatContainer(appContext);
                ((ViewGroup) target.getParent()).removeView(target);
                container.addView(target, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                if (enterPIPListener != null) {
                    enterPIPListener.onEnter(container);
                }

                isFloating = true;
            }
        }
        return pipHelper;
    }


    private DragView initFloatContainer(Context context) {

        final WindowManager.LayoutParams windowManagerLP = new WindowManager.LayoutParams();
        windowManagerLP.packageName = context.getPackageName();
        windowManagerLP.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_SCALED
                | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        if (Build.VERSION.SDK_INT >= 26) {
            windowManagerLP.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            windowManagerLP.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        windowManagerLP.format = PixelFormat.RGBA_8888;
        windowManagerLP.gravity = Gravity.START | Gravity.TOP;
        windowManagerLP.x = targetPos[0];
        windowManagerLP.y = targetPos[1];// - Utils.getStatusBarHeight(context); //FLAG_LAYOUT_IN_SCREEN
        windowManagerLP.width = targetWidth; // if wrap content, xiaomi devices can handle onTouchEvent ,but others can't
        windowManagerLP.height = targetHeight;

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DragView container = new DragView(context);
        container.init(windowManager, windowManagerLP, targetWidth, targetHeight);
        windowManager.addView(container, windowManagerLP);

//        mSurfaceView = new SurfaceView(context);
        return container;
    }

    public PIPHelper onBackKeyUpAutoFloat() {

        autoFloatListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_BACK == keyCode && event.getAction() == KeyEvent.ACTION_UP) {
                    floatView();

                }
                return false;
            }
        };

        return pipHelper;
    }

    public PIPHelper floatViewDisableDrag() {
        isDraggable = false;
        return pipHelper;
    }


    public PIPHelper onEnterPIPListener(EnterPIPListener listener) {
        enterPIPListener = listener;
        return pipHelper;
    }

    EnterPIPListener enterPIPListener;

//    SurfaceView mSurfaceView;


    public interface EnterPIPListener {
        void onEnter(View floatRootView);
    }
}
