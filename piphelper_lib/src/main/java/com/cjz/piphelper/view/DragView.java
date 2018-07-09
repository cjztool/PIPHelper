package com.cjz.piphelper.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.cjz.piphelper.PIPHelper;
import com.cjz.piphelper.Utils;

/**
 * Created by cjz on 2018/7/3.
 */
public class DragView extends FrameLayout {


    Context mContext;
    WindowManager windowManager;
    WindowManager.LayoutParams layoutParams;
    int originX, originY;
    int vWidth, vHeight;
    int sWidth, sHeight;

    float currentX, currentY;

    public DragView(@NonNull Context context) {
        super(context);
        this.mContext = context;
        this.setFocusableInTouchMode(true);
        this.setClickable(true);
    }

    public void init(WindowManager windowManager, WindowManager.LayoutParams layoutParams, int width, int height) {

        this.windowManager = windowManager;
        this.layoutParams = layoutParams;
        vWidth = width;
        vHeight = height;
        sWidth = Utils.getScreenWidth(mContext);
        sHeight = Utils.getScreenHeight(mContext, false);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!PIPHelper.get().isDraggable) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("cjz", "currentX:" +
                currentX +
                "," +
                "currentY:" +
                currentY +
                "," +
                "originX:" +
                originX +
                "," +
                "originY:" +
                originY);


        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                currentX = event.getRawX();
                currentY = event.getRawY();

                originX = layoutParams.x;
                originY = layoutParams.y;

                break;
            case MotionEvent.ACTION_MOVE:

                int dx = (int) (event.getRawX() - currentX);
                int dy = (int) (event.getRawY() - currentY);

                int newX = originX + dx;
                int newY = originY + dy;
                if (newX <= 0) {
                    newX = 0;
                } else if (newX + vWidth > sWidth) {
                    newX = sWidth - vWidth;
                }
                if (newY <= 0) {
                    newY = 0;
                } else if (newY + vHeight > sHeight) {
                    newY = sHeight - vHeight;
                }

                layoutParams.x = newX;
                layoutParams.y = newY;
                windowManager.updateViewLayout(DragView.this, layoutParams);

                break;
            case MotionEvent.ACTION_UP:
                performClick();
                break;
        }

        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
