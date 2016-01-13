package com.chenliuliu.takephoto.views;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.chenliuliu.takephoto.R;
import com.chenliuliu.takephoto.activitys.TakePhotoActivity;
import com.chenliuliu.takephoto.utils.Constants;

import java.io.File;

/**
 * Created by liuliuchen on 15/11/18.
 */
public class TakePhotoController extends BaseController {
    private TakePhotoActivity mContext;

    public TakePhotoController(TakePhotoActivity activity) {
        super(activity);
        this.mContext = activity;
    }

    public View.OnClickListener getTake() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PhotoDialog dialog = new PhotoDialog(mContext, R.style.shareDialog, new PhotoDialog.PhotoDialogCallBack() {
                    @Override
                    public void choooseFromFile() {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        mContext.startActivityForResult(intent, Constants.RequestCode.code_file);
                    }

                    @Override
                    public void choooseFromCamera() {
                        startCameraForResult();
                    }
                });
                Window w = dialog.getWindow();
                WindowManager.LayoutParams lp = w.getAttributes();
                lp.x = 0;
                final int cMakeBottom = -1000;
                lp.y = cMakeBottom;
                lp.gravity = Gravity.BOTTOM;
                dialog.onWindowAttributesChanged(lp);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();

            }
        };
    }

    protected void startCameraForResult() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(mContext.temp);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        mContext.startActivityForResult(takePictureIntent, Constants.RequestCode.code_camera);
    }

    public View.OnClickListener getUpload() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        };
    }
}
