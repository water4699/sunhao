package com.ruoyi.ai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.ai.domain.dto.AiAssistantRequest;
import com.ruoyi.ai.domain.dto.AiAssistantResponse;
import com.ruoyi.ai.service.IAiTutorService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * AI 智能匹配/问答（需登录；无密钥时自动规则回退）
 */
@RestController
@RequestMapping("/system/ai")
public class AiTutorController extends BaseController
{
    @Autowired
    private IAiTutorService aiTutorService;

    @PostMapping("/assistant")
    public AjaxResult assistant(@RequestBody AiAssistantRequest request)
    {
        AiAssistantResponse res = aiTutorService.assistant(request);
        return success(res);
    }
}
