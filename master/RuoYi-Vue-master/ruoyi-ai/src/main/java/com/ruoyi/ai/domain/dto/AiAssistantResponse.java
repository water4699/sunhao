package com.ruoyi.ai.domain.dto;

import java.util.List;
import com.ruoyi.teacher.domain.Teacher;

/**
 * AI 助手响应：文案 + 规则推荐教师列表
 */
public class AiAssistantResponse
{
    /** 展示给用户的说明或模型回复 */
    private String reply;

    /** 是否成功调用了外部大模型 */
    private boolean usedAi;

    /** 未用模型或失败时的原因简述（如 fallback、no_api_key、http_error） */
    private String source;

    private List<Teacher> teachers;

    public String getReply()
    {
        return reply;
    }

    public void setReply(String reply)
    {
        this.reply = reply;
    }

    public boolean isUsedAi()
    {
        return usedAi;
    }

    public void setUsedAi(boolean usedAi)
    {
        this.usedAi = usedAi;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public List<Teacher> getTeachers()
    {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers)
    {
        this.teachers = teachers;
    }
}
