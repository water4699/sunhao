package com.ruoyi.web.controller.system;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
public class SysLogin2Controller {

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;
    /**
     * 验证码登录方法
     *
     * @param map 登录信息
     * @return 结果
     */
    @PostMapping("/codeLogin")
    public AjaxResult codeLogin(@RequestBody Map<String, String> map) {
        String phone = map.get("phone");
        String code = map.get("code");
        // 删除错误记录
//        redisCache.deleteObject("codeLogin" + phone);

        String cachedCode = redisCache.getCacheObject(phone);
        if (!code.equals(cachedCode)) {
            Object num = redisCache.getCacheObject("codeLogin" + phone);
            if (Objects.isNull(num)) {
                redisCache.setCacheObject("codeLogin" + phone, 1, 5, TimeUnit.MINUTES);
            } else {
                int number = (int) num;
                System.err.println(number);
                if (number > 5) {
                    return AjaxResult.error("验证码输入错误次数过多，请稍后重试");
                } else {
                    redisCache.setCacheObject("codeLogin" + phone,  number+ 1, 5, TimeUnit.MINUTES);
                }
            }

            return AjaxResult.error("验证码错误或已过期");
        }

        redisCache.deleteObject("codeLogin" + phone);
        SysUser user = userService.selectUserByPhone(phone);
        if (user == null) {
            return AjaxResult.error("用户不存在");
        }
        Set<String> permissions = permissionService.getMenuPermission(user);
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(user);
        loginUser.setPermissions(permissions);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        String token = tokenService.createToken(loginUser);
        AjaxResult ajax = AjaxResult.success();
        ajax.put(Constants.TOKEN, token);
        return ajax;

    }
}
