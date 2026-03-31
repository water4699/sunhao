package com.ruoyi.review.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 评价对象 review
 */
public class Review extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String reviewId;

    /** 学生ID */
    @Excel(name = "学生ID")
    private Integer studentId;

    /** 教师ID */
    @Excel(name = "教师ID")
    private Integer teacherId;

    /** 课程预约ID */
    @Excel(name = "课程预约ID")
    private String bookingId;

    /** 评分(1-5) */
    @Excel(name = "评分(1-5)")
    private String rating;

    /** 评论文本 */
    @Excel(name = "评论文本")
    private String comment;

    /** 审核状态 */
    @Excel(name = "审核状态")
    private String status;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date createdAt;

    public void setReviewId(String reviewId) 
    {
        this.reviewId = reviewId;
    }

    public String getReviewId() 
    {
        return reviewId;
    }

    public void setStudentId(Integer studentId)
    {
        this.studentId = studentId;
    }

    public Integer getStudentId()
    {
        return studentId;
    }
    public void setTeacherId(Integer teacherId)
    {
        this.teacherId = teacherId;
    }

    public Integer getTeacherId()
    {
        return teacherId;
    }

    public void setBookingId(String bookingId) 
    {
        this.bookingId = bookingId;
    }

    public String getBookingId() 
    {
        return bookingId;
    }

    public void setRating(String rating) 
    {
        this.rating = rating;
    }

    public String getRating() 
    {
        return rating;
    }

    public void setComment(String comment) 
    {
        this.comment = comment;
    }

    public String getComment() 
    {
        return comment;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
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
            .append("reviewId", getReviewId())
            .append("studentId", getStudentId())
            .append("teacherId", getTeacherId())
            .append("bookingId", getBookingId())
            .append("rating", getRating())
            .append("comment", getComment())
            .append("status", getStatus())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}
