package com.ruoyi.ai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 大模型调用配置（默认按 DeepSeek OpenAI 兼容接口；密钥请用环境变量 RUOYI_AI_API_KEY，勿提交仓库）
 */
@ConfigurationProperties(prefix = "ruoyi.ai")
public class RuoyiAiProperties
{
    /** 是否尝试调用外部大模型 */
    private boolean enabled = true;

    private String apiUrl = "https://api.deepseek.com/chat/completions";

    private String apiKey = "";

    private String model = "deepseek-chat";

    private int connectTimeoutMs = 8000;

    private int readTimeoutMs = 20000;

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public String getApiUrl()
    {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl)
    {
        this.apiUrl = apiUrl;
    }

    public String getApiKey()
    {
        return apiKey;
    }

    public void setApiKey(String apiKey)
    {
        this.apiKey = apiKey;
    }

    public String getModel()
    {
        return model;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public int getConnectTimeoutMs()
    {
        return connectTimeoutMs;
    }

    public void setConnectTimeoutMs(int connectTimeoutMs)
    {
        this.connectTimeoutMs = connectTimeoutMs;
    }

    public int getReadTimeoutMs()
    {
        return readTimeoutMs;
    }

    public void setReadTimeoutMs(int readTimeoutMs)
    {
        this.readTimeoutMs = readTimeoutMs;
    }
}
