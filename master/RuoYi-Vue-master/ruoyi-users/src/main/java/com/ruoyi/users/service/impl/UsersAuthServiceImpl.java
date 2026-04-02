package com.ruoyi.users.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.student.domain.Student;
import com.ruoyi.student.service.IStudentService;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.users.domain.Users;
import com.ruoyi.users.mapper.UsersMapper;
import com.ruoyi.users.service.IUsersAuthService;
import com.ruoyi.users.support.BusinessLoginUserBuilder;

@Service
public class UsersAuthServiceImpl implements IUsersAuthService
{
    private final UsersMapper usersMapper;

    private final IStudentService studentService;

    public UsersAuthServiceImpl(UsersMapper usersMapper, IStudentService studentService)
    {
        this.usersMapper = usersMapper;
        this.studentService = studentService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String registerBusiness(RegisterBody body)
    {
        String username = body.getUsername();
        String password = body.getPassword();
        String phone = body.getPhone();
        if (StringUtils.isEmpty(body.getRegisterRole()))
        {
            return "请选择注册身份：学生(student)、家长(parent) 或 教师(teacher)";
        }
        String roleKey = body.getRegisterRole().trim().toLowerCase();
        if (!"student".equals(roleKey) && !"parent".equals(roleKey) && !"teacher".equals(roleKey))
        {
            return "注册身份仅支持学生(student)、家长(parent) 或 教师(teacher)；学生与家长同属选课/约课侧";
        }
        if (StringUtils.isEmpty(username))
        {
            return "用户名不能为空";
        }
        if (StringUtils.isEmpty(password))
        {
            return "用户密码不能为空";
        }
        if (StringUtils.isEmpty(phone))
        {
            return "手机号不能为空";
        }
        if (!phone.matches("^1\\d{10}$"))
        {
            return "手机号格式不正确";
        }
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            return "账户长度必须在2到20个字符之间";
        }
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            return "密码长度必须在5到20个字符之间";
        }
        if (usersMapper.countUsersByUsersname(username) > 0)
        {
            return "保存用户'" + username + "'失败，注册账号已存在";
        }
        if (usersMapper.countUsersByPhone(phone) > 0)
        {
            return "该手机号已被注册";
        }
        Users row = new Users();
        row.setUsersname(username);
        row.setPhone(phone);
        row.setPassword(SecurityUtils.encryptPassword(password));
        row.setUsersType(roleKey);
        row.setImage("");
        row.setStatus(0L);
        row.setCreateTime(DateUtils.getNowDate());
        if (usersMapper.insertUsers(row) <= 0)
        {
            return "注册失败,请联系系统管理人员";
        }
        if ("student".equals(roleKey) || "parent".equals(roleKey))
        {
            Student s = new Student();
            s.setUserId(row.getUsersId());
            s.setStatus(0L);
            s.setCreatedAt(DateUtils.getNowDate());
            if (studentService.insertStudent(s) <= 0)
            {
                throw new ServiceException("创建学员档案失败，请重试或联系管理员");
            }
        }
        return "";
    }

    @Override
    public LoginUser loginByUsernamePassword(String username, String password)
    {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
        {
            throw new ServiceException("用户名或密码不能为空");
        }
        Users u = usersMapper.selectUsersByUsersnameForAuth(username.trim());
        if (u == null)
        {
            throw new ServiceException("用户不存在");
        }
        if (StringUtils.isEmpty(u.getPassword()))
        {
            throw new ServiceException("未设置登录密码，请使用验证码登录");
        }
        if (!SecurityUtils.matchesPassword(password, u.getPassword()))
        {
            throw new ServiceException("用户不存在或密码错误");
        }
        if (u.getStatus() != null && u.getStatus() != 0L)
        {
            throw new ServiceException("账号已停用");
        }
        return BusinessLoginUserBuilder.build(u);
    }

    @Override
    public LoginUser loginUserForVerifiedPhone(String phone)
    {
        if (StringUtils.isEmpty(phone))
        {
            throw new ServiceException("手机号不能为空");
        }
        Users u = usersMapper.selectUsersByPhoneForAuth(phone.trim());
        if (u == null)
        {
            throw new ServiceException("用户不存在");
        }
        if (u.getStatus() != null && u.getStatus() != 0L)
        {
            throw new ServiceException("账号已停用");
        }
        return BusinessLoginUserBuilder.build(u);
    }
}
