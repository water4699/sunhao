package com.ruoyi.ai.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ruoyi.ai.config.RuoyiAiProperties;
import com.ruoyi.ai.domain.dto.AiAssistantRequest;
import com.ruoyi.ai.domain.dto.AiAssistantResponse;
import com.ruoyi.ai.service.IAiTutorService;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.teacher.domain.Teacher;
import com.ruoyi.teacher.service.ITeacherService;

@Service
public class AiTutorServiceImpl implements IAiTutorService
{
    private static final Logger log = LoggerFactory.getLogger(AiTutorServiceImpl.class);
    private static final List<String> DEFAULT_RECOMMEND_KEYWORDS = Arrays.asList(
        "推荐老师", "推荐教师", "找老师", "找家教", "选老师", "选教师", "家教", "预约", "上课", "辅导",
        "teacher", "tutor", "recommend"
    );

    @Autowired
    private ITeacherService teacherService;

    @Autowired
    private RuoyiAiProperties aiProperties;

    @Autowired
    private RestTemplate aiRestTemplate;

    @Override
    public AiAssistantResponse assistant(AiAssistantRequest request)
    {
        AiAssistantResponse res = new AiAssistantResponse();
        boolean recommendMode = shouldRecommendTeachers(request);
        List<Teacher> teachers = recommendMode ? selectRecommendedTeachers(request) : Collections.emptyList();
        res.setTeachers(teachers == null ? Collections.emptyList() : teachers);

        boolean wantAi = aiProperties.isEnabled() && StringUtils.isNotEmpty(aiProperties.getApiKey());
        if (!wantAi)
        {
            res.setUsedAi(false);
            res.setSource(StringUtils.isEmpty(aiProperties.getApiKey()) ? "no_api_key" : "disabled");
            if (recommendMode)
            {
                res.setReply(buildFallbackReply(request, teachers, "当前未配置 AI 密钥，已按您的条件从系统推荐以下认证教师，可直接在「找老师」中进一步筛选。"));
            }
            else
            {
                res.setReply("当前未配置 AI 密钥，暂无法进行智能问答。请稍后重试。");
            }
            return res;
        }

        try
        {
            String aiText = callChatCompletions(request, recommendMode, teachers);
            if (StringUtils.isNotEmpty(aiText))
            {
                res.setUsedAi(true);
                res.setSource(resolveAiSource());
                if (recommendMode)
                {
                    res.setReply(aiText.trim() + "\n\n（以下为平台内符合条件的教师，您可直接查看详情并预约）");
                }
                else
                {
                    res.setReply(aiText.trim());
                }
                return res;
            }
        }
        catch (RestClientException e)
        {
            log.warn("AI 调用失败，使用规则回退: {}", e.getMessage());
        }
        catch (Exception e)
        {
            log.warn("AI 解析失败，使用规则回退", e);
        }

        res.setUsedAi(false);
        res.setSource("fallback");
        if (recommendMode)
        {
            res.setReply(buildFallbackReply(request, teachers, "智能助手暂时不可用，已按条件为您推荐以下认证教师。"));
        }
        else
        {
            res.setReply("智能助手暂时不可用，请稍后再试。");
        }
        return res;
    }

    private List<Teacher> selectRecommendedTeachers(AiAssistantRequest request)
    {
        Teacher q = new Teacher();
        q.setStatus(1L);
        if (request.getSubjectId() != null)
        {
            q.setSubjectId(request.getSubjectId());
        }
        if (StringUtils.isNotEmpty(request.getAreaId()))
        {
            q.setAreaId(request.getAreaId());
        }
        if (StringUtils.isNotEmpty(request.getGradeId()))
        {
            q.setGradeId(request.getGradeId());
        }
        PageHelper.startPage(1, 8);
        try
        {
            return teacherService.selectTeacherList(q);
        }
        finally
        {
            PageHelper.clearPage();
        }
    }

    private String buildFallbackReply(AiAssistantRequest request, List<Teacher> teachers, String head)
    {
        StringBuilder sb = new StringBuilder(head);
        if (teachers == null || teachers.isEmpty())
        {
            sb.append(" 当前条件下暂无已审核教师，可放宽科目/地区/年级后再试。");
        }
        else
        {
            sb.append(" 共 ").append(teachers.size()).append(" 位。建议优先看老师的授课经历、学校背景、评分和时薪是否与当前学习目标匹配。");
            sb.append(" 当前可优先关注：");
            int n = Math.min(3, teachers.size());
            for (int i = 0; i < n; i++)
            {
                Teacher t = teachers.get(i);
                sb.append(" ").append(t.getRealName() != null ? t.getRealName() : "教师");
                if (t.getUniversity() != null)
                {
                    sb.append("（").append(t.getUniversity()).append("）");
                }
                sb.append("；");
            }
        }
        return sb.toString();
    }

    private String callChatCompletions(AiAssistantRequest request, boolean recommendMode, List<Teacher> teachers)
    {
        String userMsg = StringUtils.isNotEmpty(request.getMessage()) ? request.getMessage() : "请根据我的筛选条件给出家教选择建议。";
        String ctx = String.format("科目ID=%s，地区ID=%s，年级ID=%s。",
            request.getSubjectId() != null ? request.getSubjectId() : "不限",
            StringUtils.isNotEmpty(request.getAreaId()) ? request.getAreaId() : "不限",
            StringUtils.isNotEmpty(request.getGradeId()) ? request.getGradeId() : "不限");
        String teacherCtx = recommendMode ? buildTeacherContext(teachers) : "";

        JSONObject body = new JSONObject();
        body.put("model", aiProperties.getModel());
        JSONArray messages = new JSONArray();
        JSONObject sys = new JSONObject();
        sys.put("role", "system");
        if (recommendMode)
        {
            sys.put("content", "你是「聚星教育」家教平台的中文助理。你必须基于平台提供的候选教师信息给出选课和选老师建议，不能编造平台里不存在的老师。输出使用简洁中文，控制在220字内，优先包含：1. 学习问题判断；2. 选老师标准；3. 若有候选教师，点名1到3位并说明匹配原因。");
        }
        else
        {
            sys.put("content", "你是「聚星教育」平台的中文智能助手。请直接回答用户问题，给出清晰、可执行建议。不要强行推荐老师，不要虚构平台数据。输出控制在220字内。");
        }
        JSONObject usr = new JSONObject();
        usr.put("role", "user");
        if (recommendMode)
        {
            usr.put("content", ctx + "\n平台候选教师：\n" + teacherCtx + "\n用户说：" + userMsg);
        }
        else
        {
            usr.put("content", "用户说：" + userMsg + "\n（如与筛选有关可参考：" + ctx + "）");
        }
        messages.add(sys);
        messages.add(usr);
        body.put("messages", messages);
        body.put("temperature", 0.6);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(aiProperties.getApiKey());
        HttpEntity<String> entity = new HttpEntity<>(body.toJSONString(), headers);

        ResponseEntity<String> resp = aiRestTemplate.postForEntity(aiProperties.getApiUrl(), entity, String.class);
        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null)
        {
            return null;
        }
        JSONObject root = JSON.parseObject(resp.getBody());
        JSONArray choices = root.getJSONArray("choices");
        if (choices == null || choices.isEmpty())
        {
            return null;
        }
        JSONObject msg = choices.getJSONObject(0).getJSONObject("message");
        return msg != null ? msg.getString("content") : null;
    }

    private String buildTeacherContext(List<Teacher> teachers)
    {
        if (teachers == null || teachers.isEmpty())
        {
            return "暂无符合条件的候选教师。";
        }
        StringBuilder sb = new StringBuilder();
        int n = Math.min(5, teachers.size());
        for (int i = 0; i < n; i++)
        {
            Teacher t = teachers.get(i);
            sb.append(i + 1).append(". ")
                .append(StringUtils.isNotEmpty(t.getRealName()) ? t.getRealName() : "教师")
                .append("，学校=").append(StringUtils.isNotEmpty(t.getUniversity()) ? t.getUniversity() : "未填写")
                .append("，学历=").append(StringUtils.isNotEmpty(t.getEducation()) ? t.getEducation() : "未填写")
                .append("，时薪=").append(t.getHourlyRate() != null ? t.getHourlyRate() : "未填写")
                .append("，评分=").append(t.getRating() != null ? t.getRating() : "未填写")
                .append("。");
            if (i < n - 1)
            {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private String resolveAiSource()
    {
        String apiUrl = aiProperties.getApiUrl();
        if (StringUtils.isNotEmpty(apiUrl) && apiUrl.toLowerCase().contains("deepseek"))
        {
            return "deepseek";
        }
        return "llm";
    }

    private boolean shouldRecommendTeachers(AiAssistantRequest request)
    {
        if (request == null)
        {
            return false;
        }
        String rawMessage = StringUtils.trimToEmpty(request.getMessage());
        if (StringUtils.isEmpty(rawMessage))
        {
            return request.getSubjectId() != null
                || StringUtils.isNotEmpty(request.getAreaId())
                || StringUtils.isNotEmpty(request.getGradeId());
        }
        String msg = rawMessage.toLowerCase();
        List<String> recommendKeywords = aiProperties.getRecommendKeywords();
        if (recommendKeywords == null || recommendKeywords.isEmpty())
        {
            recommendKeywords = DEFAULT_RECOMMEND_KEYWORDS;
        }
        for (String k : recommendKeywords)
        {
            String keyword = StringUtils.trimToEmpty(k).toLowerCase();
            if (StringUtils.isNotEmpty(keyword) && msg.contains(keyword))
            {
                return true;
            }
        }
        return false;
    }
}
