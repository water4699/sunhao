package com.ruoyi.users.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.users.mapper.UsersMapper;
import com.ruoyi.users.domain.Users;
import com.ruoyi.users.service.IUsersService;

/**
 * 用户总Service业务层处理
 */
@Service
public class UsersServiceImpl implements IUsersService 
{
    @Autowired
    private UsersMapper usersMapper;

    /**
     * 查询用户总
     * 
     * @param usersId 用户总主键
     * @return 用户总
     */
    @Override
    public Users selectUsersByUsersId(String usersId)
    {
        return usersMapper.selectUsersByUsersId(usersId);
    }



    /**
     * 查询用户总列表
     * 
     * @param users 用户总
     * @return 用户总
     */
    @Override
    public List<Users> selectUsersList(Users users)
    {
        return usersMapper.selectUsersList(users);
    }

    /**
     * 新增用户总
     * 
     * @param users 用户总
     * @return 结果
     */

    @Override
    public int insertUsers(Users users)
    {
        users.setCreateTime(DateUtils.getNowDate());
        return usersMapper.insertUsers(users);
    }

    /**
     * 修改用户总
     * 
     * @param users 用户总
     * @return 结果
     */
    @Override
    public int updateUsers(Users users)
    {
        return usersMapper.updateUsers(users);
    }

    /**
     * 批量删除用户总
     * 
     * @param usersIds 需要删除的用户总主键
     * @return 结果
     */
    @Override
    public int deleteUsersByUsersIds(String[] usersIds)
    {
        return usersMapper.deleteUsersByUsersIds(usersIds);
    }

    /**
     * 删除用户总信息
     * @param usersId 用户总主键
     * @return 结果
     */
    @Override
    public int deleteUsersByUsersId(String usersId)
    {
        return usersMapper.deleteUsersByUsersId(usersId);
    }
}
