package com.ruoyi.users.service;

import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.domain.model.RegisterBody;

/**
 * 业务用户（users 表）注册与密码登录
 */
public interface IUsersAuthService
{
    /**
     * 小程序/业务注册：写入 users，不操作 sys_user。
     *
     * @return 空表示成功，否则为错误文案
     */
    String registerBusiness(RegisterBody body);

    /**
     * 用户名 + 密码登录（业务用户）
     */
    LoginUser loginByUsernamePassword(String username, String password);

    /**
     * 手机号已通过短信验证码校验后，构造业务端登录态（不写 sys_user）
     */
    LoginUser loginUserForVerifiedPhone(String phone);
}
