package com.ruoyi.my.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.my.mapper.AdminMapper;
import com.ruoyi.my.domain.Admin;
import com.ruoyi.my.service.IAdminService;

/**
 * 管理员Service业务层处理
 */
@Service
public class AdminServiceImpl implements IAdminService 
{
    @Autowired
    private AdminMapper adminMapper;

    /**
     * 查询管理员
     * 
     * @param adminId 管理员主键
     * @return 管理员
     */
    @Override
    public Admin selectAdminByAdminId(String adminId)
    {
        return adminMapper.selectAdminByAdminId(adminId);
    }

    /**
     * 查询管理员列表
     * 
     * @param admin 管理员
     * @return 管理员
     */
    @Override
    public List<Admin> selectAdminList(Admin admin)
    {
        return adminMapper.selectAdminList(admin);
    }

    /**
     * 新增管理员
     * 
     * @param admin 管理员
     * @return 结果
     */
    @Override
    public int insertAdmin(Admin admin)
    {
        return adminMapper.insertAdmin(admin);
    }

    /**
     * 修改管理员
     * 
     * @param admin 管理员
     * @return 结果
     */
    @Override
    public int updateAdmin(Admin admin)
    {
        return adminMapper.updateAdmin(admin);
    }

    /**
     * 批量删除管理员
     * 
     * @param adminIds 需要删除的管理员主键
     * @return 结果
     */
    @Override
    public int deleteAdminByAdminIds(String[] adminIds)
    {
        return adminMapper.deleteAdminByAdminIds(adminIds);
    }

    /**
     * 删除管理员信息
     * 
     * @param adminId 管理员主键
     * @return 结果
     */
    @Override
    public int deleteAdminByAdminId(String adminId)
    {
        return adminMapper.deleteAdminByAdminId(adminId);
    }
}
