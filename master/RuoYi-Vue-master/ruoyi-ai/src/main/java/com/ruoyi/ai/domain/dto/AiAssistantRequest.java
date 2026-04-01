package com.ruoyi.ai.domain.dto;

/**
 * AI 助手请求：可选筛选条件 + 用户问题
 */
public class AiAssistantRequest
{
    private Integer subjectId;

    private String areaId;

    private String gradeId;

    /** 用户自然语言问题，可空 */
    private String message;

    public Integer getSubjectId()
    {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId)
    {
        this.subjectId = subjectId;
    }

    public String getAreaId()
    {
        return areaId;
    }

    public void setAreaId(String areaId)
    {
        this.areaId = areaId;
    }

    public String getGradeId()
    {
        return gradeId;
    }

    public void setGradeId(String gradeId)
    {
        this.gradeId = gradeId;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
