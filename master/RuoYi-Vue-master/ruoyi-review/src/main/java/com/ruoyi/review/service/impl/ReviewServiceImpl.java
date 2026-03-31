package com.ruoyi.review.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.review.mapper.ReviewMapper;
import com.ruoyi.review.domain.Review;
import com.ruoyi.review.service.IReviewService;

/**
 * 评价Service业务层处理
 */
@Service
public class ReviewServiceImpl implements IReviewService {
    @Autowired
    private ReviewMapper reviewMapper;

    /**
     * 查询评价
     *
     * @param reviewId 评价主键
     * @return 评价
     */
    @Override
    public Review selectReviewByReviewId(String reviewId) {
        return reviewMapper.selectReviewByReviewId(reviewId);
    }

    /**
     * 查询评价列表
     *
     * @param review 评价
     * @return 评价
     */
    @Override
    public List<Review> selectReviewList(Review review) {
        return reviewMapper.selectReviewList(review);
    }

    /**
     * 新增评价
     *
     * @param review 评价
     * @return 结果
     */
    @Override
    public int insertReview(Review review) {
        return reviewMapper.insertReview(review);
    }

    /**
     * 修改评价
     *
     * @param review 评价
     * @return 结果
     */
    @Override
    public int updateReview(Review review) {
        return reviewMapper.updateReview(review);
    }

    /**
     * 批量删除评价
     *
     * @param reviewIds 需要删除的评价主键
     * @return 结果
     */
    @Override
    public int deleteReviewByReviewIds(String[] reviewIds) {
        return reviewMapper.deleteReviewByReviewIds(reviewIds);
    }

    /**
     * 删除评价信息
     *
     * @param reviewId 评价主键
     * @return 结果
     */
    @Override
    public int deleteReviewByReviewId(String reviewId) {
        return reviewMapper.deleteReviewByReviewId(reviewId);
    }


    @Override
    public String selectStudentByStudentId(Integer studentId) {
        return reviewMapper.selectStudentByStudentId(studentId);
    }

//    @Override
//    public String selectTeacherByTeacherId(Integer teacherId) {
//        return reviewMapper.selectTeacherByTeacherId(teacherId);
//    }


}
