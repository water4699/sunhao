package com.ruoyi.web.controller.system;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.file.MimeTypeUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.users.domain.Users;
import com.ruoyi.users.service.IUsersService;

/**
 * 个人信息 业务处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private IUsersService usersService;

    @Autowired
    private TokenService tokenService;

    /**
     * 个人信息
     */
    @GetMapping
    public AjaxResult profile()
    {
        LoginUser loginUser = getLoginUser();
        if (loginUser.isBusinessUser())
        {
            String uid = String.valueOf(loginUser.getUserId());
            Users u = usersService.selectUsersByUsersId(uid);
            if (u == null)
            {
                return error("用户不存在");
            }
            SysUser su = new SysUser();
            su.setUserId(loginUser.getUserId());
            su.setUserName(u.getUsersname());
            su.setNickName(u.getUsersname());
            su.setPhonenumber(u.getPhone());
            // 业务库 users 表目前不落 email/sex；这里优先从登录态的 stub 里取，保证“刚修改后再次打开”可见。
            String stubEmail = (loginUser.getUser() != null && StringUtils.isNotEmpty(loginUser.getUser().getEmail()))
                    ? loginUser.getUser().getEmail()
                    : "";
            String stubSex = (loginUser.getUser() != null && StringUtils.isNotEmpty(loginUser.getUser().getSex()))
                    ? loginUser.getUser().getSex()
                    : "0";
            su.setEmail(stubEmail);
            su.setSex(stubSex);
            su.setAvatar(StringUtils.isNotEmpty(u.getImage()) ? u.getImage() : "");
            su.setCreateTime(u.getCreateTime());
            AjaxResult ajax = AjaxResult.success(su);
            // 身份（users.users_type）映射到展示用字段
            String usersType = u.getUsersType();
            String roleGroup;
            if ("teacher".equalsIgnoreCase(usersType))
            {
                roleGroup = "教师";
            }
            else if ("parent".equalsIgnoreCase(usersType))
            {
                roleGroup = "家长";
            }
            else if ("student".equalsIgnoreCase(usersType))
            {
                roleGroup = "学生";
            }
            else
            {
                roleGroup = StringUtils.isEmpty(usersType) ? "" : usersType;
            }
            ajax.put("roleGroup", roleGroup);
            ajax.put("postGroup", roleGroup);
            return ajax;
        }
        SysUser user = loginUser.getUser();
        AjaxResult ajax = AjaxResult.success(user);
        ajax.put("roleGroup", userService.selectUserRoleGroup(loginUser.getUsername()));
        ajax.put("postGroup", userService.selectUserPostGroup(loginUser.getUsername()));
        return ajax;
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@RequestBody SysUser user)
    {
        LoginUser loginUser = getLoginUser();
        if (loginUser.isBusinessUser())
        {
            String uid = String.valueOf(loginUser.getUserId());
            Users db = usersService.selectUsersByUsersId(uid);
            if (db == null)
            {
                return error("用户不存在");
            }
            if (StringUtils.isNotEmpty(user.getNickName()))
            {
                if (!user.getNickName().equals(db.getUsersname())
                    && usersService.countUsersByUsersname(user.getNickName()) > 0)
                {
                    return error("修改失败，该昵称已被占用");
                }
                db.setUsersname(user.getNickName());
            }
            if (StringUtils.isNotEmpty(user.getPhonenumber()))
            {
                if (!user.getPhonenumber().equals(db.getPhone()))
                {
                    Users byPhone = usersService.selectUsersByPhoneForAuth(user.getPhonenumber());
                    if (byPhone != null && !uid.equals(byPhone.getUsersId()))
                    {
                        return error("修改失败，手机号码已存在");
                    }
                }
                db.setPhone(user.getPhonenumber());
            }
            if (usersService.updateUsers(db) > 0)
            {
                SysUser stub = loginUser.getUser();
                stub.setNickName(db.getUsersname());
                stub.setUserName(db.getUsersname());
                stub.setPhonenumber(db.getPhone());
                if (StringUtils.isNotEmpty(user.getEmail()))
                {
                    stub.setEmail(user.getEmail());
                }
                if (StringUtils.isNotEmpty(user.getSex()))
                {
                    stub.setSex(user.getSex());
                }
                tokenService.setLoginUser(loginUser);
                return success();
            }
            return error("修改个人信息异常，请联系管理员");
        }
        SysUser currentUser = loginUser.getUser();
        currentUser.setNickName(user.getNickName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhonenumber(user.getPhonenumber());
        currentUser.setSex(user.getSex());
        if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(currentUser))
        {
            return error("修改用户'" + loginUser.getUsername() + "'失败，手机号码已存在");
        }
        if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(currentUser))
        {
            return error("修改用户'" + loginUser.getUsername() + "'失败，邮箱账号已存在");
        }
        if (userService.updateUserProfile(currentUser) > 0)
        {
            // 更新缓存用户信息
            tokenService.setLoginUser(loginUser);
            return success();
        }
        return error("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(@RequestBody Map<String, String> params)
    {
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        LoginUser loginUser = getLoginUser();
        Long userId = loginUser.getUserId();
        if (loginUser.isBusinessUser())
        {
            Users u = usersService.selectUsersByUsersIdForAuth(String.valueOf(userId));
            if (u == null || StringUtils.isEmpty(u.getPassword()))
            {
                return error("修改密码失败，账号未设置密码或用户不存在");
            }
            if (!SecurityUtils.matchesPassword(oldPassword, u.getPassword()))
            {
                return error("修改密码失败，旧密码错误");
            }
            if (SecurityUtils.matchesPassword(newPassword, u.getPassword()))
            {
                return error("新密码不能与旧密码相同");
            }
            String enc = SecurityUtils.encryptPassword(newPassword);
            Users patch = new Users();
            patch.setUsersId(String.valueOf(userId));
            patch.setPassword(enc);
            if (usersService.updateUsers(patch) > 0)
            {
                loginUser.getUser().setPwdUpdateDate(DateUtils.getNowDate());
                loginUser.getUser().setPassword(enc);
                tokenService.setLoginUser(loginUser);
                return success();
            }
            return error("修改密码异常，请联系管理员");
        }
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password))
        {
            return error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password))
        {
            return error("新密码不能与旧密码相同");
        }
        newPassword = SecurityUtils.encryptPassword(newPassword);
        if (userService.resetUserPwd(userId, newPassword) > 0)
        {
            // 更新缓存用户密码&密码最后更新时间
            loginUser.getUser().setPwdUpdateDate(DateUtils.getNowDate());
            loginUser.getUser().setPassword(newPassword);
            tokenService.setLoginUser(loginUser);
            return success();
        }
        return error("修改密码异常，请联系管理员");
    }

    /**
     * 头像上传
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws Exception
    {
        if (!file.isEmpty())
        {
            LoginUser loginUser = getLoginUser();
            String avatar = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION, true);
            if (loginUser.isBusinessUser())
            {
                Users patch = new Users();
                patch.setUsersId(String.valueOf(loginUser.getUserId()));
                patch.setImage(avatar);
                if (usersService.updateUsers(patch) <= 0)
                {
                    return error("上传图片异常，请联系管理员");
                }
                String oldAvatar = loginUser.getUser().getAvatar();
                if (StringUtils.isNotEmpty(oldAvatar))
                {
                    FileUtils.deleteFile(RuoYiConfig.getProfile() + FileUtils.stripPrefix(oldAvatar));
                }
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", avatar);
                loginUser.getUser().setAvatar(avatar);
                tokenService.setLoginUser(loginUser);
                return ajax;
            }
            if (userService.updateUserAvatar(loginUser.getUserId(), avatar))
            {
                String oldAvatar = loginUser.getUser().getAvatar();
                if (StringUtils.isNotEmpty(oldAvatar))
                {
                    FileUtils.deleteFile(RuoYiConfig.getProfile() + FileUtils.stripPrefix(oldAvatar));
                }
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", avatar);
                // 更新缓存用户头像
                loginUser.getUser().setAvatar(avatar);
                tokenService.setLoginUser(loginUser);
                return ajax;
            }
        }
        return error("上传图片异常，请联系管理员");
    }
}
