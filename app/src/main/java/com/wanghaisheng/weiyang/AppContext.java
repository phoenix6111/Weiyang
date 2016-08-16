package com.wanghaisheng.weiyang;

import android.os.Handler;

import com.apkfuns.logutils.LogUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.wanghaisheng.template_lib.AppManager;
import com.wanghaisheng.template_lib.BaseApplication;
import com.wanghaisheng.template_lib.component.fresco.ImagePipelineConfigFactory;
import com.wanghaisheng.template_lib.utils.AppConfig;
import com.wanghaisheng.template_lib.utils.PrefsUtil;
import com.wanghaisheng.weiyang.datasource.repository.common.CommonRepository;
import com.wanghaisheng.weiyang.injector.component.AppComponent;
import com.wanghaisheng.weiyang.injector.component.DaggerAppComponent;
import com.wanghaisheng.weiyang.injector.module.AppModule;
import com.zhy.http.okhttp.OkHttpUtils;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

public class AppContext extends BaseApplication {

    private AppComponent appComponent;

    private static AppContext instance;

    @Inject
    OkHttpClient mOkHttpClient;
    @Inject
    CommonRepository commonRepository;
    @Inject
    PrefsUtil prefsUtil;

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;
        initDaggerComponent();
        //初始化Activity管理栈
        AppManager.init();

        initComponent();

        initChannelData();
    }

    private void initComponent() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                //为全局设置OkHttpUtils的OkHttpClient对象
                OkHttpUtils.getInstance(mOkHttpClient);
                Fresco.initialize(AppContext.this, ImagePipelineConfigFactory.getOkHttpImagePipelineConfig(AppContext.this,mOkHttpClient));
                LogUtils.getLogConfig().configAllowLog(BuildConfig.DEBUG);
            }
        });
    }

    private void initDaggerComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);

        LogUtils.d("print init daggercomponent");
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    /**
     * 获得当前app运行的AppContext
     * @return
     */
    public static AppContext getInstance() {
        return instance;
    }

    /**
     * 初始化频道信息，如是安装后第一次打开则执行，否则不执行
     */
    private void initChannelData() {
        boolean firstStart = prefsUtil.get(AppConfig.KEY_FRITST_START,true);
//        LogUtils.d("check init channel data ....");
        if(firstStart) {
            commonRepository.initChannelEntities();

            //重置value为不是第一次安装
            prefsUtil.set(AppConfig.KEY_FRITST_START,false);
        }

    }

}