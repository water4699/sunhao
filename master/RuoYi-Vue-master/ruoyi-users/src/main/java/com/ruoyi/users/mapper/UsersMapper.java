package com.ruoyi.users.mapper;

import java.util.List;
import com.ruoyi.users.domain.Users;

/**
 * 用户总Mapper接口
 */
public interface UsersMapper 
{
    /**
     * 查询用户总
     * 
     * @param usersId 用户总主键
     * @return 用户总
     */
    public Users selectUsersByUsersId(String usersId);

    /**
     * 查询用户总列表
     * 
     * @param users 用户总
     * @return 用户总集合
     */
    public List<Users> selectUsersList(Users users);

    /**
     * 新增用户总
     * 
     * @param users 用户总
     * @return 结果
     */
    public int insertUsers(Users users);

    /**
     * 修改用户总
     * 
     * @param users 用户总
     * @return 结果
     */
    public int updateUsers(Users users);

    /**
     * 删除用户总
     * 
     * @param usersId 用户总主键
     * @return 结果
     */
    public int deleteUsersByUsersId(String usersId);

    /**
     * 批量删除用户总
     * 
     * @param usersIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUsersByUsersIds(String[] usersIds);
}
