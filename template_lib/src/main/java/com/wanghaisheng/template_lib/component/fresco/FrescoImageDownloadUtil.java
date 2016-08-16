package com.wanghaisheng.template_lib.component.fresco;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.wanghaisheng.template_lib.utils.AppConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhaodaizheng on 16/5/20.
 */
public class FrescoImageDownloadUtil {

    private static final String TAG = "FrescoUtil";
    public static final String IMAGE_PIC_CACHE_DIR = AppConfig.DEFAULT_SAVE_IMAGE_PATH;

    private ImageDownloadListener downloadListener;
    private Context context;

    public FrescoImageDownloadUtil(Context context, ImageDownloadListener downloadListener) {
        this.context = context;
        this.downloadListener = downloadListener;
    }

    /**
     * 保存图片
     * @param picUrls
     * @param context
     */
    public void savePicture(final Context context, String[] picUrls) {
        final File picDir = new File(IMAGE_PIC_CACHE_DIR);

        if (!picDir.exists()) {
            picDir.mkdir();
        }
        Observable.from(picUrls)
            .flatMap(new Func1<String, Observable<Boolean>>() {
                @Override
                public Observable<Boolean> call(final String picUrl) {
                    return Observable.create(new Observable.OnSubscribe<Boolean>() {
                        @Override
                        public void call(final Subscriber<? super Boolean> subscriber) {
                            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(Uri.parse(picUrl)),null);
                            File cacheFile = getCachedImageOnDisk(cacheKey);
                            if (cacheFile != null) {
                                copyTo(cacheFile,picDir,picUrl);
                                subscriber.onNext(true);
                                subscriber.onCompleted();

                            } else {

                                downLoadImage(picUrl,context)
                                    .subscribe(new Action1<Boolean>() {
                                        @Override
                                        public void call(Boolean aBoolean) {
                                            subscriber.onNext(true);
                                            subscriber.onCompleted();
                                        }
                                    });
                            }
                        }
                    });
                }
            }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {
                    downloadListener.downloadComplete(true);
                }

                @Override
                public void onError(Throwable e) {
                    downloadListener.downloadComplete(false);
                }

                @Override
                public void onNext(Boolean aBoolean) {
                    downloadListener.downloadListener(aBoolean);
                }
            });

    }

    public File getCachedImageOnDisk(CacheKey cacheKey) {
        File localFile = null;
        if (cacheKey != null) {
            if (ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            } else if (ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            }
        }
        return localFile;
    }


    /**
     * 复制文件
     * @param src 源文件
     * @param dir 目标文件dir
     * @return
     */
    public boolean copyTo(File src, File dir, String picUrl) {

        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(src);
            in = fi.getChannel();//得到对应的文件通道
            File dst;
//            String filePostfix = FileUtil.getFileFormat(src.getName());
            String suffix = null;
            if(picUrl.endsWith(".gif")) {
                suffix = ".gif";
            } else {
                suffix = ".jpg";
            }
            String fileName = System.currentTimeMillis()+suffix;
            dst = new File(dir, fileName);
            fo = new FileOutputStream(dst);
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
            Uri uri = Uri.fromFile(dst);
            // 通知图库更新
            Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
            context.sendBroadcast(scannerIntent);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (fi != null) {
                    fi.close();
                }

                if (in != null) {
                    in.close();
                }

                if (fo != null) {
                    fo.close();
                }

                if (out != null) {
                    out.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

    }


    public Observable<Boolean> downLoadImage(final String picUrl, final Context context) {
        final Uri uri = Uri.parse(picUrl);
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                ImageRequest imageRequest = ImageRequestBuilder
                        .newBuilderWithSource(uri)
                        .setProgressiveRenderingEnabled(true)
                        .build();
                ImagePipeline imagePipeline = Fresco.getImagePipeline();
                DataSource<CloseableReference<CloseableImage>>
                        dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
                dataSource.subscribe(new BaseBitmapDataSubscriber() {
                    @Override
                    public void onNewResultImpl(Bitmap bitmap) {
                        if (bitmap == null) {
                            Log.e(TAG,"保存图片失败啦,无法下载图片");
                            subscriber.onNext(false);
                            subscriber.onCompleted();
                        }
                        File appDir = new File(IMAGE_PIC_CACHE_DIR);
                        if (!appDir.exists()) {
                            appDir.mkdir();
                        }
                        String suffix = null;
                        if(picUrl.endsWith(".gif")) {
                            suffix = ".gif";
                        } else {
                            suffix = ".jpg";
                        }
                        String fileName = System.currentTimeMillis()+suffix;
                        File file = new File(appDir, fileName);
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            assert bitmap != null;
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.flush();
                            fos.close();

                            subscriber.onNext(true);
                            subscriber.onCompleted();
                            Uri uri = Uri.fromFile(file);
                            // 通知图库更新
                            Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                            context.sendBroadcast(scannerIntent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailureImpl(DataSource dataSource) {
                    }
                }, CallerThreadExecutor.getInstance());
            }
        }).subscribeOn(Schedulers.io());

    }
}