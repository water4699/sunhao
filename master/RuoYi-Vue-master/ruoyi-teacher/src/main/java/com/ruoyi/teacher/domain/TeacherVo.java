package com.ruoyi.teacher.domain;

public class TeacherVo extends Teacher {
    private static final long serialVersionUID = 1L;

    private String subjectName;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
