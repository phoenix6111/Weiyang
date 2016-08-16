package com.wanghaisheng.template_lib.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.wanghaisheng.template_lib.BaseApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import rx.Observable;

/**
 * Created by sll on 2015/3/7.
 */
public class FileHelper {

    private Context context;

    public FileHelper(Context context) {
        this.context = context;
    }

    public static boolean hasSDCard() {
        boolean mHasSDcard = false;
        if (Environment.MEDIA_MOUNTED.endsWith(Environment.getExternalStorageState())) {
            mHasSDcard = true;
        } else {
            mHasSDcard = false;
        }

        return mHasSDcard;
    }

    public String getSdcardPath() {

        if (hasSDCard())
            return Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator;

        return "/sdcard/";
    }


    public String stringFromAssetsFile(String fileName) {
        AssetManager manager = context.getAssets();
        InputStream file;
        try {
            file = manager.open(fileName);
            byte[] data = new byte[file.available()];
            file.read(data);
            file.close();
            return new String(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String length(long length) {
        String str = "0B";
        if (length == 0) {
            return str;
        }
        if (length < 1048576) {
            return Math.round(((double) length) / 1024) + "KBbyte";
        }
        return Math.round(((double) length) / 1048576) + "MBbyte";
    }


    /**
     * 复制assets文件到指定目录
     *
     * @param fileName 文件名
     * @param filePath 目录
     */
    public void copyAssets(String fileName, String filePath) {
        InputStream inputStream;
        try {
            inputStream = context.getResources().getAssets().open(fileName);// assets文件夹下的文件
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(filePath + "/" + fileName);// 保存到本地的文件夹下的文件
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, count);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean copy(File oldFile, File newFile) {
        if (!oldFile.exists()) {
            return false;
        }
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(oldFile);
            outputStream = new FileOutputStream(newFile);
            byte[] buffer = new byte[4096];
            while (inputStream.read(buffer) != -1) {
                outputStream.write(buffer);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean exist(String url) {
        File file = new File(url);
        return file.exists();
    }


    public void chmod(String permission, String path) {
        try {
            String command = "chmod " + permission + " " + path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Observable<Uri> saveImagesAndGetPathObservable(final String url, final String foldername, final String name) {
        /*return Observable.getMessage(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bitmap = null;
                try {
                    bitmap = Picasso.with(context).load(url).get();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
                if (bitmap == null) {
                    subscriber.onError(new Exception("无法下载到图片"));
                }
                subscriber.onNext(bitmap);
                subscriber.onCompleted();
            }
        }).flatMap(new Func1<Bitmap, Observable<Uri>>() {
            @Override
            public Observable<Uri> call(Bitmap bitmap) {
                LogUtils.d(bitmap.getByteCount());
                return Observable.just(saveimages(bitmap, foldername, name));
            }
        }).subscribeOn(Schedulers.io());*/
        return null;
    }

    public Uri saveimages(Bitmap bm, String path, String name) {
        File appDir = null;
        if(!TextUtils.isEmpty(path)) {
            appDir = new File(AppConfig.DEFAULT_SAVE_IMAGE_PATH,path);
        } else {
            appDir = new File(AppConfig.DEFAULT_SAVE_IMAGE_PATH);
        }

        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        File file = new File(appDir, name + ".jpg");
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri uri = Uri.fromFile(file);
        // 通知图库更新
        Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        context.sendBroadcast(scannerIntent);
        return uri;
    }

    /**
     * 通过uri获取文件的路径
     * @param uri
     * @return
     */
    public static String getFileNameFromUri(Uri uri) {
        Cursor cursor = BaseApplication.context().getContentResolver().query(uri,null,null,null,null);
        if (cursor == null) {
            return null;
        }
        cursor.moveToFirst();
        String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
        cursor.close();

        return fileName;
    }
}


