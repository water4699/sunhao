package com.ruoyi.review.service;

import java.util.List;
import com.ruoyi.review.domain.Review;

/**
 * 评价Service接口
 */
public interface IReviewService 
{
    /**
     * 查询评价
     * 
     * @param reviewId 评价主键
     * @return 评价
     */
    public Review selectReviewByReviewId(String reviewId);

    /**
     * 查询评价列表
     * 
     * @param review 评价
     * @return 评价集合
     */
    public List<Review> selectReviewList(Review review);

    /**
     * 新增评价
     * 
     * @param review 评价
     * @return 结果
     */
    public int insertReview(Review review);

    /**
     * 修改评价
     * 
     * @param review 评价
     * @return 结果
     */
    public int updateReview(Review review);

    /**
     * 批量删除评价
     * 
     * @param reviewIds 需要删除的评价主键集合
     * @return 结果
     */
    public int deleteReviewByReviewIds(String[] reviewIds);

    /**
     * 删除评价信息
     * 
     * @param reviewId 评价主键
     * @return 结果
     */
    public int deleteReviewByReviewId(String reviewId);



    String selectStudentByStudentId(Integer studentId);
//    String selectTeacherByTeacherId(Integer teacherId);
}
