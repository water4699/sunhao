package com.ruoyi.users.support;

import com.ruoyi.common.utils.StringUtils;

/**
 * 业务端 users.users_type 归类：学生与家长为同一侧（选课/约课），教师为另一侧。
 */
public final class UsersAppType
{
    private UsersAppType()
    {
    }

    public static boolean isLearnerSide(String usersType)
    {
        if (StringUtils.isEmpty(usersType))
        {
            return false;
        }
        String t = usersType.trim().toLowerCase();
        return "student".equals(t) || "parent".equals(t);
    }

    public static boolean isTeacher(String usersType)
    {
        return "teacher".equals(StringUtils.trimToEmpty(usersType).toLowerCase());
    }
}
