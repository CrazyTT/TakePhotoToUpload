package com.chenliuliu.takephoto.activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chenliuliu.takephoto.R;
import com.chenliuliu.takephoto.utils.Constants;
import com.chenliuliu.takephoto.utils.PictureUtil;
import com.chenliuliu.takephoto.views.TakePhotoController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TakePhotoActivity extends BaseActivity {
    TakePhotoController mController;
    public String ImgPath;
    @Bind(R.id.img_photo)
    ImageView mImageView;
    @Bind(R.id.btn_upload)
    Button btnUpload;
    public String temp = PictureUtil.getAlbumDir().getAbsolutePath() + File.separator + "temp.jpg";
    @Bind(R.id.img_photo_demo)
    ImageView mImgPhotoDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mController = new TakePhotoController(this);
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        ImgPath = PictureUtil.getAlbumDir().getAbsolutePath() + File.separator + System.currentTimeMillis() + "_" + getIntent().getIntExtra("photo_type", 0) + "_" + getIntent().getIntExtra("photo_number", 0) + ".jpg";
    }


    @Override
    protected void initViews() {
        ButterKnife.bind(this);
        tvTitle.setText("拍照上传");
        btnRight.setVisibility(View.VISIBLE);
        Drawable drawable = getResources().getDrawable(R.drawable.logo_camera);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        btnRight.setCompoundDrawables(drawable, null, null, null);
        btnRight.setOnClickListener(mController.getTake());
        btnUpload.setOnClickListener(mController.getUpload());
        mImageView.setImageResource(R.drawable.equipment_demo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.RequestCode.code_camera:
                    Uri selectedImage;
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(new File(temp));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    selectedImage = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                    startPhotoZoom(selectedImage);
                    break;
                case Constants.RequestCode.code_file:
                    Uri selectedImage2 = data.getData();
                    startPhotoZoom(selectedImage2);
                    break;
                case Constants.RequestCode.photo_zoom:
                    File temp = new File(ImgPath);
                    if (temp != null) {
                        mImageView.setImageBitmap(BitmapFactory.decodeFile(ImgPath));
                        btnUpload.setEnabled(true);
                        mImgPhotoDemo.setVisibility(View.GONE);
                    }
                    break;

            }
        }
    }

    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String url = getPath(TakePhotoActivity.this, uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        } else {
            intent.setDataAndType(uri, "image/*");
        }
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1.5);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 900);
        intent.putExtra("outputY", 600);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(ImgPath)));
        startActivityForResult(intent, Constants.RequestCode.photo_zoom);
    }

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
