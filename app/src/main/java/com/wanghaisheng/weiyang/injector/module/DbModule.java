package com.wanghaisheng.weiyang.injector.module;

import android.content.Context;

import com.wanghaisheng.weiyang.database.ChannelDao;
import com.wanghaisheng.weiyang.database.DaoMaster;
import com.wanghaisheng.weiyang.database.DaoSession;
import com.wanghaisheng.weiyang.database.MeishiBeanDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


/**
 * Created by sheng on 2016/4/14.
 */

@Module
public class DbModule {

    @Provides
    @Singleton
    DaoMaster.DevOpenHelper provideDevOpenHelper(Context context) {
        return new DaoMaster.DevOpenHelper(context, "xiaoya.db", null);
    }

    @Provides
    @Singleton
    DaoMaster provideDaoMaster(DaoMaster.DevOpenHelper helper) {
        return new DaoMaster(helper.getWritableDatabase());
    }

    @Provides
    @Singleton
    DaoSession provideDaoSession(DaoMaster master) {
        return master.newSession();
    }

    @Provides
    @Singleton
    MeishiBeanDao provideMovieDao(DaoSession session) {
        return session.getMeishiBeanDao();
    }

    @Provides
    @Singleton
    ChannelDao provideChannelDao(DaoSession session) {
        return session.getChannelDao();
    }

}
