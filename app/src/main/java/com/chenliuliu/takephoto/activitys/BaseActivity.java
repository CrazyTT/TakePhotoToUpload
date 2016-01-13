package com.chenliuliu.takephoto.activitys;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chenliuliu.takephoto.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by liuliuchen on 15/11/2.
 */
public abstract class BaseActivity extends Activity {
    public ImageButton imgBack;

    protected TextView tvTitle;

    public TextView btnRight;

    protected abstract void initViews();

    /**
     * @param savedInstanceState 子类的super方法放到最后一行执行
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        initTitle();
        initViews();
    }

    private void applyKitKatTranslucency() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);
            mTintManager.setNavigationBarTintEnabled(true);
            // mTintManager.setTintColor(0x63AE0A);
            mTintManager.setStatusBarTintDrawable(getResources().getDrawable(R.drawable.backgroud_title_state));

        }

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    protected void initTitle() {
        imgBack = (ImageButton) this.findViewById(R.id.title_left_btn);
        tvTitle = (TextView) this.findViewById(R.id.title_tv);
        btnRight = (TextView) this.findViewById(R.id.title_right_btn);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 获取当前点击位置是否为et
     *
     * @param view  焦点所在View
     * @param event 触摸事件
     * @return
     */
    public boolean isClickEt(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            view.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            //此处根据输入框左上位置和宽高获得右下位置
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 點擊EditText以外的區域后鍵盤隱藏
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            //获取当前获得当前焦点所在View
            View view = getCurrentFocus();
            if (isClickEt(view, event)) {

                //如果不是edittext，则隐藏键盘

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    //隐藏键盘
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(event);
        }
        /**
         * 看源码可知superDispatchTouchEvent  是个抽象方法，用于自定义的Window
         * 此处目的是为了继续将事件由dispatchTouchEvent(MotionEvent event)传递到onTouchEvent(MotionEvent event)
         * 必不可少，否则所有组件都不能触发 onTouchEvent(MotionEvent event)
         */
        if (getWindow().superDispatchTouchEvent(event)) {
            return true;
        }
        return onTouchEvent(event);
    }
}
