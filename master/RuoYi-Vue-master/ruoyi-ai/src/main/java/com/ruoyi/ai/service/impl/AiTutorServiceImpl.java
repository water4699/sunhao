package com.ruoyi.ai.service.impl;

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
        List<Teacher> teachers = selectRecommendedTeachers(request);
        res.setTeachers(teachers == null ? Collections.emptyList() : teachers);

        boolean wantAi = aiProperties.isEnabled() && StringUtils.isNotEmpty(aiProperties.getApiKey());
        if (!wantAi)
        {
            res.setUsedAi(false);
            res.setSource(StringUtils.isEmpty(aiProperties.getApiKey()) ? "no_api_key" : "disabled");
            res.setReply(buildFallbackReply(request, teachers, "当前未配置 AI 密钥，已按您的条件从系统推荐以下认证教师，可直接在「找老师」中进一步筛选。"));
            return res;
        }

        try
        {
            String aiText = callChatCompletions(request);
            if (StringUtils.isNotEmpty(aiText))
            {
                res.setUsedAi(true);
                res.setSource("llm");
                res.setReply(aiText.trim() + "\n\n（以下为系统同步推荐的认证教师，供您快速查看）");
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
        res.setReply(buildFallbackReply(request, teachers, "智能助手暂时不可用，已按条件为您推荐以下认证教师。"));
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
            sb.append(" 共 ").append(teachers.size()).append(" 位：");
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

    private String callChatCompletions(AiAssistantRequest request)
    {
        String userMsg = StringUtils.isNotEmpty(request.getMessage()) ? request.getMessage() : "请根据我的筛选条件给出家教选择建议。";
        String ctx = String.format("科目ID=%s，地区ID=%s，年级ID=%s。请用简短中文（200字内）给家长一些选课/选老师建议，语气友好。不要编造不存在的教师姓名。",
            request.getSubjectId() != null ? request.getSubjectId() : "不限",
            StringUtils.isNotEmpty(request.getAreaId()) ? request.getAreaId() : "不限",
            StringUtils.isNotEmpty(request.getGradeId()) ? request.getGradeId() : "不限");

        JSONObject body = new JSONObject();
        body.put("model", aiProperties.getModel());
        JSONArray messages = new JSONArray();
        JSONObject sys = new JSONObject();
        sys.put("role", "system");
        sys.put("content", "你是「聚星教育」家教平台的中文助理，只回答与选课、学习方法、如何选老师相关的问题。");
        JSONObject usr = new JSONObject();
        usr.put("role", "user");
        usr.put("content", ctx + "\n用户说：" + userMsg);
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
}
