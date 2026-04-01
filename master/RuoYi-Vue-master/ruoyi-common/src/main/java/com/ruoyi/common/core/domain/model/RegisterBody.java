package com.ruoyi.common.core.domain.model;

/**
 * 用户注册对象
 * 
 * @author ruoyi
 */
public class RegisterBody extends LoginBody
{
    /** 手机号（小程序/业务注册必填） */
    private String phone;

    /**
     * 注册时绑定角色：空或未传则分配 common（普通角色，role_id=2）；
     * parent=家长、teacher=教师（需库中存在 role_key，见 docs/sql/register_roles_parent_teacher.sql）
     */
    private String registerRole;

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getRegisterRole()
    {
        return registerRole;
    }

    public void setRegisterRole(String registerRole)
    {
        this.registerRole = registerRole;
    }
}
