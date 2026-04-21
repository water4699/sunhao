package com.ruoyi.review.domain;

/**
 * 评价列表展示用 VO（扩展学生/教师姓名，不依赖 Lombok 以便模块独立编译）。
 */
public class ReviewVo extends Review {
    private static final long serialVersionUID = 1L;

    private String studentName;
    private String teacherName;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
