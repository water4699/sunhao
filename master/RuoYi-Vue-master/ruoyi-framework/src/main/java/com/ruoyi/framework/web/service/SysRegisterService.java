package com.ruoyi.framework.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.CaptchaExpireException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;

/**
 * 注册校验方法
 * 
 * @author ruoyi
 */
@Component
public class SysRegisterService
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 注册
     */
    public String register(RegisterBody registerBody)
    {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);

        // 验证码开关
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled)
        {
            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        }

        if (StringUtils.isEmpty(username))
        {
            msg = "用户名不能为空";
        }
        else if (StringUtils.isEmpty(password))
        {
            msg = "用户密码不能为空";
        }
        else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            msg = "账户长度必须在2到20个字符之间";
        }
        else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            msg = "密码长度必须在5到20个字符之间";
        }
        else if (!userService.checkUserNameUnique(sysUser))
        {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        }
        else
        {
            StringBuilder roleErr = new StringBuilder();
            Long[] roleIds = resolveRegisterRoleIds(registerBody.getRegisterRole(), roleErr);
            if (roleIds == null)
            {
                return roleErr.length() > 0 ? roleErr.toString() : "注册角色解析失败";
            }
            sysUser.setNickName(username);
            sysUser.setPwdUpdateDate(DateUtils.getNowDate());
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            sysUser.setRoleIds(roleIds);
            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag)
            {
                msg = "注册失败,请联系系统管理人员";
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success")));
            }
        }
        return msg;
    }

    /**
     * 解析注册要绑定的角色 ID。
     * registerRole 为空：绑定 common（普通角色，默认 role_id=2）。
     * parent / teacher：查库绑定；不存在时返回错误提示执行 SQL。
     * 其它值：非法。
     *
     * @param outMsg 非法或缺角色时写入原因
     * @return 角色 ID 数组，失败返回 null
     */
    private Long[] resolveRegisterRoleIds(String registerRole, StringBuilder outMsg)
    {
        if (StringUtils.isEmpty(registerRole))
        {
            SysRole common = roleService.selectRoleByRoleKey("common");
            if (common == null)
            {
                outMsg.append("系统未配置 common 角色，请联系管理员初始化数据库");
                return null;
            }
            return new Long[] { common.getRoleId() };
        }
        String key = registerRole.trim().toLowerCase();
        if ("common".equals(key))
        {
            SysRole common = roleService.selectRoleByRoleKey("common");
            if (common == null)
            {
                outMsg.append("系统未配置 common 角色，请联系管理员初始化数据库");
                return null;
            }
            return new Long[] { common.getRoleId() };
        }
        if (!"parent".equals(key) && !"teacher".equals(key))
        {
            outMsg.append("注册身份无效，请使用 parent（家长）或 teacher（教师），或留空使用普通角色");
            return null;
        }
        SysRole role = roleService.selectRoleByRoleKey(key);
        if (role == null)
        {
            outMsg.append("系统未配置「").append(key).append("」角色，请在库中执行 docs/sql/register_roles_parent_teacher.sql 后重试");
            return null;
        }
        return new Long[] { role.getRoleId() };
    }

    /**
     * 校验验证码
     * 
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid)
    {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null)
        {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException();
        }
    }
}
