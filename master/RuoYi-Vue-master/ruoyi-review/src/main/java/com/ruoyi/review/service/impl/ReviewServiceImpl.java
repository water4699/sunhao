package com.ruoyi.review.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.course.domain.Course;
import com.ruoyi.course.mapper.CourseMapper;
import com.ruoyi.student.domain.Student;
import com.ruoyi.student.service.IStudentService;
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

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private IStudentService studentService;

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

    @Override
    public int submitReviewByLoginUser(Review review) {
        if (review == null || StringUtils.isEmpty(review.getBookingId())) {
            throw new ServiceException("请指定要评价的课程预约");
        }
        Long uid = SecurityUtils.getUserId();
        Course course = courseMapper.selectCourseByCourseId(review.getBookingId());
        if (course == null) {
            throw new ServiceException("预约记录不存在");
        }
        String courseSid = course.getStudentId();
        if (StringUtils.isEmpty(courseSid)) {
            throw new ServiceException("预约记录数据异常");
        }
        String myPk = resolveMyStudentPkForReview(uid);
        if (!courseSid.trim().equals(myPk)) {
            throw new ServiceException("只能评价自己的预约");
        }
        int sid;
        try {
            sid = Integer.parseInt(courseSid.trim());
        } catch (NumberFormatException e) {
            throw new ServiceException("学员编号格式异常，无法提交评价");
        }
        if (course.getStatus() == null || course.getStatus() != 1L) {
            throw new ServiceException("仅在课程已确认后可评价");
        }
        if (reviewMapper.countByStudentIdAndBookingId(sid, review.getBookingId()) > 0) {
            throw new ServiceException("该预约已提交过评价");
        }
        String rating = review.getRating();
        if (StringUtils.isEmpty(rating)) {
            throw new ServiceException("请选择评分");
        }
        int score;
        try {
            score = Integer.parseInt(rating.trim());
        } catch (NumberFormatException e) {
            throw new ServiceException("评分格式不正确");
        }
        if (score < 1 || score > 5) {
            throw new ServiceException("评分需在 1～5 之间");
        }
        if (StringUtils.isEmpty(course.getTeacherId())) {
            throw new ServiceException("预约缺少教师信息");
        }
        int tid;
        try {
            tid = Integer.parseInt(course.getTeacherId().trim());
        } catch (NumberFormatException e) {
            throw new ServiceException("教师编号格式异常");
        }
        Review row = new Review();
        row.setStudentId(sid);
        row.setTeacherId(tid);
        row.setBookingId(review.getBookingId());
        row.setRating(String.valueOf(score));
        row.setComment(StringUtils.isEmpty(review.getComment()) ? "" : review.getComment().trim());
        row.setStatus("1");
        row.setCreatedAt(new Date());
        return reviewMapper.insertReview(row);
    }

    /**
     * course.student_id 为 student 表主键；业务登录 userId 为 users_id，需先解析到同一主键再比对。
     */
    private String resolveMyStudentPkForReview(Long uid) {
        if (uid == null) {
            throw new ServiceException("请先登录");
        }
        Student st = studentService.selectStudentByUserId(String.valueOf(uid));
        if (st != null && StringUtils.isNotEmpty(st.getStudentId())) {
            return st.getStudentId().trim();
        }
        return String.valueOf(uid);
    }

//    @Override
//    public String selectTeacherByTeacherId(Integer teacherId) {
//        return reviewMapper.selectTeacherByTeacherId(teacherId);
//    }


}
