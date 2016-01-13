package com.chenliuliu.takephoto.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.chenliuliu.takephoto.app.MyApplication;


public class ToastUtils {

    public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public static final int LENGTH_LONG = Toast.LENGTH_LONG;

    private static Toast toast;
    private static Handler handler = new Handler();

    private static Runnable run = new Runnable() {
        public void run() {
            toast.cancel();
        }
    };

    /**
     * <p/>
     *
     * @param ctx
     * @param msg
     * @param duration
     * @author chenliuliu, 2014-9-15
     */
    public static void toast(Context ctx, CharSequence msg, int duration) {
        handler.removeCallbacks(run);
        // handler的duration不能直接对应Toast的常量时长，在此针对Toast的常量相应定义时长
        switch (duration) {
            case LENGTH_SHORT:// Toast.LENGTH_SHORT值为0，对应的持续时间大概为1s
                duration = 1000;
                break;
            case LENGTH_LONG:// Toast.LENGTH_LONG值为1，对应的持续时间大概为3s
                duration = 3000;
                break;
            default:
                break;
        }
        if (null != toast) {
            toast.setText(msg);
        } else {
            toast = Toast.makeText(MyApplication.getInstance(), msg, duration);
        }
        handler.postDelayed(run, duration);
        toast.show();
    }
}
