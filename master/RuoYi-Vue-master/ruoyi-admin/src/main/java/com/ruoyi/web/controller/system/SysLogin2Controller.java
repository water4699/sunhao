package com.ruoyi.web.controller.system;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.users.service.IUsersAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@RestController
public class SysLogin2Controller {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1\\d{10}$");

    private static final Pattern CODE_PATTERN = Pattern.compile("^\\d{4,6}$");

    private static final String SMS_CODE_KEY_PREFIX = "sms:login:code:";

    private static final String SMS_FAIL_COUNT_KEY_PREFIX = "sms:login:fail:";

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IUsersAuthService usersAuthService;
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
        if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
            return AjaxResult.error("手机号格式不正确");
        }
        if (code == null || !CODE_PATTERN.matcher(code).matches()) {
            return AjaxResult.error("验证码格式不正确");
        }
        // 删除错误记录
//        redisCache.deleteObject("codeLogin" + phone);

        String cachedCode = redisCache.getCacheObject(SMS_CODE_KEY_PREFIX + phone);
        if (!Objects.equals(code, cachedCode)) {
            Object num = redisCache.getCacheObject(SMS_FAIL_COUNT_KEY_PREFIX + phone);
            if (Objects.isNull(num)) {
                redisCache.setCacheObject(SMS_FAIL_COUNT_KEY_PREFIX + phone, 1, 5, TimeUnit.MINUTES);
            } else {
                int number = (int) num;
                System.err.println(number);
                if (number > 5) {
                    return AjaxResult.error("验证码输入错误次数过多，请稍后重试");
                } else {
                    redisCache.setCacheObject(SMS_FAIL_COUNT_KEY_PREFIX + phone,  number+ 1, 5, TimeUnit.MINUTES);
                }
            }

            return AjaxResult.error("验证码错误或已过期");
        }

        redisCache.deleteObject(SMS_FAIL_COUNT_KEY_PREFIX + phone);
        redisCache.deleteObject(SMS_CODE_KEY_PREFIX + phone);
        LoginUser loginUser = usersAuthService.loginUserForVerifiedPhone(phone);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        String token = tokenService.createToken(loginUser);
        AjaxResult ajax = AjaxResult.success();
        ajax.put(Constants.TOKEN, token);
        return ajax;

    }
}
