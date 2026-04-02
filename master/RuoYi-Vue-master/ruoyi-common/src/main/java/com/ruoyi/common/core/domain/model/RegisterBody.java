package com.ruoyi.common.core.domain.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

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
     * student=学生、parent=家长（与学生同属选课侧）、teacher=教师（见 docs/sql/register_roles_student_teacher.sql）
     */
    @JsonProperty("registerRole")
    @JsonAlias({ "register_role", "RegisterRole", "registerType" })
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
