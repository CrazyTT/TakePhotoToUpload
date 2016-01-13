package com.chenliuliu.takephoto.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chenliuliu.takephoto.R;


public class PhotoDialog extends Dialog implements OnClickListener {
    private Button buttonFile, buttonCamera, buttonCancle;
    private PhotoDialogCallBack dialogCallBack;

    public PhotoDialog(Context context, int theme, PhotoDialogCallBack dialogCallBack) {
        super(context, theme);
        this.dialogCallBack = dialogCallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_take_choose);
        buttonFile = (Button) findViewById(R.id.btn_from_file);
        buttonFile.setOnClickListener(this);
        buttonCamera = (Button) findViewById(R.id.btn_from_camera);
        buttonCamera.setOnClickListener(this);
        buttonCancle = (Button) findViewById(R.id.btn_photo_cancle);
        buttonCancle.setOnClickListener(this);
    }

    public interface PhotoDialogCallBack {
        void choooseFromFile();

        void choooseFromCamera();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_from_file:
                dialogCallBack.choooseFromFile();
                this.dismiss();
                break;
            case R.id.btn_from_camera:
                dialogCallBack.choooseFromCamera();
                this.dismiss();
                break;
            case R.id.btn_photo_cancle:
                this.dismiss();
                break;
            default:
                break;
        }

    }
}