package com.chenliuliu.takephoto.views;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;


/**
 * Created by liuliuchen on 15/8/5.
 */
public class BaseController {

    protected Activity activity;

    public BaseController(Activity activity) {
        this.activity = activity;
    }

    public OnClickListener getImgBackLinstener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        };
    }
}
