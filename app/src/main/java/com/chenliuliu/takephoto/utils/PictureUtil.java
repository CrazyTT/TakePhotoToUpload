package com.chenliuliu.takephoto.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.widget.Toast;

import com.chenliuliu.takephoto.app.MyApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author chenliuliu
 */
public class PictureUtil {

    public static String bitmapToString(String filePath, int w, int l) {
        int degree = readPictureDegree(filePath);
        Bitmap bm = getSmallBitmap(filePath, w, l);
        if (degree != 0) {
            bm = rotaingImageView(degree, bm);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static String byte16hex(String url) {
        Bitmap bitmap = BitmapFactory.decodeFile(url);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bt = stream.toByteArray();
        String photoStr = byte2hex(bt);
        return photoStr;
    }

    private static String byte2hex(byte[] b) {
        StringBuffer sb = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                sb.append("0" + stmp);
            } else {
                sb.append(stmp);
            }
        }
        return sb.toString();
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                            int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap getSmallBitmap(String filePath, int width, int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap getSmallBitmap(String filePath) {
        return getSmallBitmap(filePath, 480, 480);
    }

    public static void deleteTempFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children.length == 0) {
                return dir.delete();
            }
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so now it can be smoked
        return dir.delete();

    }

    public static void galleryAddPic(Context context, String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public static File getAlbumDir() {
        File dir;
        if (ExistSDCard()) {
            dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    getAlbumName());
        } else if (!ExistSDCard()) {
            dir = new File(MyApplication.getInstance().getCacheDir().getAbsolutePath(), getAlbumName());
        } else {
            return null;
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static String getAlbumName() {
        return "nonghuobang";
    }

    public static Bitmap rotate90Degree(Bitmap bm) {
        Bitmap result;
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        int nWidth = bm.getWidth();
        int nHeight = bm.getHeight();
        result = Bitmap.createBitmap(bm, 0, 0, nWidth, nHeight, matrix, true);
        return result;
    }

    public static String preparePhoto(String pathBig, String pathSmall) {
        if (pathBig != null) {
            FileOutputStream fos = null;
            Bitmap bm = null;
            try {
                int degree = readPictureDegree(pathBig);
                bm = getSmallBitmap(pathBig, 300, 460);
                if (bm == null) {
                    return null;
                }
                File nFile = new File(PictureUtil.getAlbumDir(), pathSmall);
                fos = new FileOutputStream(nFile);
                if (degree == 0) {
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    if (null != bm) {
                        bm.recycle();
                        bm = null;
                    }

                } else {
                    bm = rotaingImageView(degree, bm);
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    if (null != bm) {
                        bm.recycle();
                        bm = null;
                    }
                }
                return nFile.getPath();
            } catch (Exception e) {
                ToastUtils.toast(MyApplication.getInstance().getApplicationContext(), e.getMessage(),
                        Toast.LENGTH_SHORT);
                e.printStackTrace();
            } finally {
                if (null != fos) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                    }
                }
                if (null != bm) {
                    bm.recycle();
                    bm = null;
                }
            }
        }
        return null;
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(angle);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    private static boolean ExistSDCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }
}