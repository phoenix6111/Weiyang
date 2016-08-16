package com.wanghaisheng.template_lib.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.R;
import com.wanghaisheng.template_lib.component.fresco.FrescoImageDownloadUtil;
import com.wanghaisheng.template_lib.component.fresco.ImageDownloadListener;
import com.wanghaisheng.template_lib.utils.AppConfig;
import com.wanghaisheng.template_lib.utils.ToastUtil;

import java.util.ArrayList;

public class SaveAllImageService extends IntentService implements ImageDownloadListener {
    private static final String TAG = "SaveAllImageService";

    public static final String ARG_GROUPID = "group_id";
    public static final String ARG_URLS = "arg_urls";
    public static final String ARG_TITLE = "arg_title";

    private String title;
    private String groupId;
    private ArrayList<String> urls;

    private NotificationManager notificationManager;
    private NotificationCompat.Builder mBuilder;
    private int downloadCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtils.d("服务初始化。。。");

    }


    public SaveAllImageService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        initDatas(intent);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(getString(R.string.msg_download_image))
                .setContentText(getString(R.string.msg_download_progress))
                .setSmallIcon(R.drawable.image_download);

        String[] urlArrs = new String[urls.size()];
        urls.toArray(urlArrs);
        Handler mHandler = new Handler(getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showShortCenterToast(getApplicationContext(),"开始下载图片...");
            }
        });
        FrescoImageDownloadUtil downloadUtil = new FrescoImageDownloadUtil(getApplicationContext(),this);
        downloadUtil.savePicture(getApplicationContext(),urlArrs);

    }

    private void initDatas(Intent intent) {
        LogUtils.d("print initdatas............................................");
        urls = intent.getStringArrayListExtra(ARG_URLS);

    }

    @Override
    public void downloadListener(Boolean success) {
        mBuilder.setProgress(urls.size(),++downloadCount,false);
        notificationManager.notify(1,mBuilder.build());
    }

    @Override
    public void downloadComplete(Boolean success) {
        Handler mHandler = new Handler(getMainLooper());
        if(success) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    String formatStr = getString(R.string.msg_image_download_path);
                    String distStr = AppConfig.DEFAULT_SAVE_IMAGE_PATH;
                    String resultStr = String.format(formatStr,distStr);

                    ToastUtil.showToast(getApplicationContext(),resultStr);
                }
            });
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    String resultStr = getString(R.string.msg_download_failure);

                    ToastUtil.showToast(getApplicationContext(),resultStr);
                }
            });
        }
    }
}