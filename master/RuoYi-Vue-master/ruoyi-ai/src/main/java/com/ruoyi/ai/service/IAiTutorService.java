package com.ruoyi.ai.service;

import com.ruoyi.ai.domain.dto.AiAssistantRequest;
import com.ruoyi.ai.domain.dto.AiAssistantResponse;

public interface IAiTutorService
{
    AiAssistantResponse assistant(AiAssistantRequest request);
}
