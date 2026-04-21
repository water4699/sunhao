package com.ruoyi.users.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户总对象 users
 */
public class Users extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String usersId;

    /** 用户名 */
    @Excel(name = "用户名")
    private String usersname;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 性别（0男/1女/2未知） */
    @Excel(name = "性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /** 登录密码（BCrypt，不落库展示） */
    @JsonIgnore
    private String password;

    /** 用户类型 */
    @Excel(name = "用户类型")
    private String usersType;

    /** 头像 */
    @Excel(name = "头像")
    private String image;

    /** 账号状态 */
    @Excel(name = "账号状态")
    private Long status;

    public void setUsersId(String usersId) 
    {
        this.usersId = usersId;
    }

    public String getUsersId() 
    {
        return usersId;
    }

    public void setUsersname(String usersname) 
    {
        this.usersname = usersname;
    }

    public String getUsersname() 
    {
        return usersname;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setUsersType(String usersType) 
    {
        this.usersType = usersType;
    }

    public String getUsersType() 
    {
        return usersType;
    }

    public void setImage(String image) 
    {
        this.image = image;
    }

    public String getImage() 
    {
        return image;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("usersId", getUsersId())
            .append("usersname", getUsersname())
            .append("phone", getPhone())
            .append("email", getEmail())
            .append("sex", getSex())
            .append("usersType", getUsersType())
            .append("image", getImage())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .toString();
    }
}
