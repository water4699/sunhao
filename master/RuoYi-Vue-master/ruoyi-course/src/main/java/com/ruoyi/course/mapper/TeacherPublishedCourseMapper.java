package com.ruoyi.course.mapper;

import com.ruoyi.course.domain.TeacherPublishedCourse;

/**
 * 老师发布课程 Mapper
 */
public interface TeacherPublishedCourseMapper
{
    int insertTeacherPublishedCourse(TeacherPublishedCourse course);

    TeacherPublishedCourse selectTeacherPublishedCourseById(String publishId);

    java.util.List<TeacherPublishedCourse> selectTeacherPublishedCourseList(TeacherPublishedCourse query);

    int updateTeacherPublishedCourse(TeacherPublishedCourse course);

    int updateTeacherPublishedCourseStatus(TeacherPublishedCourse course);

    int deleteTeacherPublishedCourse(TeacherPublishedCourse course);
}
