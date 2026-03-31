package com.ruoyi.my.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 管理员对象 admin
 */
public class Admin extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String adminId;

    /** 管理员账号 */
    @Excel(name = "管理员账号")
    private String username;

    /** 密码哈希 */
    @Excel(name = "密码哈希")
    private String passwordHash;

    /** 管理员姓名 */
    @Excel(name = "管理员姓名")
    private String fullName;

    /** 管理员角色 */
    @Excel(name = "管理员角色")
    private String role;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 账号状态 */
    @Excel(name = "账号状态")
    private Long status;

    /** 最后登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastLogin;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date createdAt;

    public void setAdminId(String adminId) 
    {
        this.adminId = adminId;
    }

    public String getAdminId() 
    {
        return adminId;
    }

    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getUsername() 
    {
        return username;
    }

    public void setPasswordHash(String passwordHash) 
    {
        this.passwordHash = passwordHash;
    }

    public String getPasswordHash() 
    {
        return passwordHash;
    }

    public void setFullName(String fullName) 
    {
        this.fullName = fullName;
    }

    public String getFullName() 
    {
        return fullName;
    }

    public void setRole(String role) 
    {
        this.role = role;
    }

    public String getRole() 
    {
        return role;
    }

    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    public void setLastLogin(Date lastLogin) 
    {
        this.lastLogin = lastLogin;
    }

    public Date getLastLogin() 
    {
        return lastLogin;
    }

    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("adminId", getAdminId())
            .append("username", getUsername())
            .append("passwordHash", getPasswordHash())
            .append("fullName", getFullName())
            .append("role", getRole())
            .append("email", getEmail())
            .append("status", getStatus())
            .append("lastLogin", getLastLogin())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}
