package com.wanghaisheng.template_lib.component.fresco;

/**
 * Created by sheng on 2016/7/8.
 */
public interface ImageDownloadListener {

    //下载进度
    void downloadListener(Boolean success);

    /**
     * 下载完成
     */
    void downloadComplete(Boolean success);


}
