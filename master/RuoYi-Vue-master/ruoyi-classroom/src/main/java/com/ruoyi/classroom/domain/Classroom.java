package com.ruoyi.classroom.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 教室对象 classroom
 */
public class Classroom extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String classroomId;

    /** 教室名称 */
    @Excel(name = "教室名称")
    private String name;

    /** 地区ID */
    @Excel(name = "地区ID")
    private String locationId;

    /** 所在建筑 */
    @Excel(name = "所在建筑")
    private String building;

    /** 房间号 */
    @Excel(name = "房间号")
    private String roomNumber;

    /** 容量 */
    @Excel(name = "容量")
    private String capacity;

    /** 设施列表 */
    @Excel(name = "设施列表")
    private String facilities;

    /** 每小时费用 */
    @Excel(name = "每小时费用")
    private BigDecimal hourlyRate;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** 管理管理员 */
    @Excel(name = "管理管理员")
    private String adminId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date createdAt;

    public void setClassroomId(String classroomId) 
    {
        this.classroomId = classroomId;
    }

    public String getClassroomId() 
    {
        return classroomId;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setLocationId(String locationId) 
    {
        this.locationId = locationId;
    }

    public String getLocationId() 
    {
        return locationId;
    }

    public void setBuilding(String building) 
    {
        this.building = building;
    }

    public String getBuilding() 
    {
        return building;
    }

    public void setRoomNumber(String roomNumber) 
    {
        this.roomNumber = roomNumber;
    }

    public String getRoomNumber() 
    {
        return roomNumber;
    }

    public void setCapacity(String capacity) 
    {
        this.capacity = capacity;
    }

    public String getCapacity() 
    {
        return capacity;
    }

    public void setFacilities(String facilities) 
    {
        this.facilities = facilities;
    }

    public String getFacilities() 
    {
        return facilities;
    }

    public void setHourlyRate(BigDecimal hourlyRate) 
    {
        this.hourlyRate = hourlyRate;
    }

    public BigDecimal getHourlyRate() 
    {
        return hourlyRate;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    public void setAdminId(String adminId) 
    {
        this.adminId = adminId;
    }

    public String getAdminId() 
    {
        return adminId;
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
            .append("classroomId", getClassroomId())
            .append("name", getName())
            .append("locationId", getLocationId())
            .append("building", getBuilding())
            .append("roomNumber", getRoomNumber())
            .append("capacity", getCapacity())
            .append("facilities", getFacilities())
            .append("hourlyRate", getHourlyRate())
            .append("status", getStatus())
            .append("adminId", getAdminId())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}
