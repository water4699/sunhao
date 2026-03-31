package com.ruoyi.users.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.CodeUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.users.domain.Users;
import com.ruoyi.users.service.IUsersService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户总Controller
 */

@RestController
@RequestMapping("/system/users")
public class UsersController extends BaseController
{
    @Autowired
    private IUsersService usersService;
    @Autowired
    private RedisCache redisCache;

    /**
     * 查询用户总列表
     */
    @PreAuthorize("@ss.hasPermi('system:users:list')")
    @GetMapping("/list")
    public TableDataInfo list(Users users)
    {
        startPage();
        List<Users> list = usersService.selectUsersList(users);
        return getDataTable(list);
    }

    /**
     * 导出用户总列表
     */
    @PreAuthorize("@ss.hasPermi('system:users:export')")
    @Log(title = "用户总", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Users users)
    {
        List<Users> list = usersService.selectUsersList(users);
        ExcelUtil<Users> util = new ExcelUtil<Users>(Users.class);
        util.exportExcel(response, list, "用户总数据");
    }

    /**
     * 获取用户总详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:users:query')")
    @GetMapping(value = "/{usersId}")
    public AjaxResult getInfo(@PathVariable("usersId") String usersId)
    {
        return success(usersService.selectUsersByUsersId(usersId));
    }

    /**
     * 新增用户总
     */
    @PreAuthorize("@ss.hasPermi('system:users:add')")
    @Log(title = "用户总", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Users users)
    {
        return toAjax(usersService.insertUsers(users));
    }

    /**
     * 修改用户总
     */
    @PreAuthorize("@ss.hasPermi('system:users:edit')")
    @Log(title = "用户总", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Users users)
    {
        return toAjax(usersService.updateUsers(users));
    }

    /**
     * 删除用户总
     */
    @PreAuthorize("@ss.hasPermi('system:users:remove')")
    @Log(title = "用户总", businessType = BusinessType.DELETE)
	@DeleteMapping("/{usersIds}")
    public AjaxResult remove(@PathVariable String[] usersIds)
    {
        return toAjax(usersService.deleteUsersByUsersIds(usersIds));
    }

    @GetMapping("/getUserCode")
    public AjaxResult getUserCode(@RequestParam String phone) {
        String code = CodeUtils.generateValidateCode(6) + "";
        redisCache.setCacheObject(phone, code, 1, TimeUnit.MINUTES);
        return AjaxResult.success(code);
    }
}
