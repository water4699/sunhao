package com.ruoyi.users.support;

import java.util.Collections;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.users.domain.Users;

/**
 * 将业务表 users 转为若依 {@link LoginUser}（内嵌 SysUser 桩对象，仅用于 token/getInfo 兼容，非 sys_user 落库）。
 */
public final class BusinessLoginUserBuilder
{
    private BusinessLoginUserBuilder()
    {
    }

    public static LoginUser build(Users u)
    {
        if (u == null || u.getUsersId() == null)
        {
            throw new IllegalArgumentException("users");
        }
        long id = Long.parseLong(u.getUsersId().trim());
        SysUser stub = new SysUser();
        stub.setUserId(id);
        stub.setUserName(u.getUsersname());
        stub.setNickName(u.getUsersname());
        stub.setPhonenumber(u.getPhone());
        stub.setAvatar(u.getImage() != null ? u.getImage() : "");
        stub.setPassword("");
        stub.setStatus("0");
        stub.setDelFlag("0");
        LoginUser loginUser = new LoginUser(id, null, stub, Collections.singleton(Constants.ALL_PERMISSION));
        loginUser.setBusinessUser(true);
        loginUser.setBusinessUsersType(u.getUsersType());
        return loginUser;
    }
}
