package com.wanghaisheng.weiyang.presenter.auth;


import com.wanghaisheng.template_lib.presenter.base.BaseLoadableView;
import com.wanghaisheng.weiyang.datasource.beans.User;

/**
 * Created by sheng on 2016/5/24.
 */
public interface ProfileView extends BaseLoadableView {
    int UPDATE_TYPE_TYPE_UPDATE_NICKNAME = 0x005;
    int UPDATE_TYPE_TYPE_UPDATE_GENDER = 0x006;
    int UPDATE_TYPE_TYPE_UPDATE_AVATAR = 0x007;

    /**
     * 数据修改成功
     * @param type 修改的类型
     * @param user 修改后返回的user
     */
    void updateSuccess(int type, User user);


}
