package com.kuaidaoresume.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeChatService {
    static final ILogger logger = SLoggerFactory.getLogger(WeChatService.class);

    public static final String WECHAT_APP_ID = "wx3dd711441403646b";
    public static final String WECHAT_APP_SECRET = "9038b0ebd71b4f266408a801f41eeb86";

    private RestTemplate restTemplate;

    @Autowired
    public WeChatService(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
                .errorHandler(new WeChatRestResponseErrorHandler())
                .build();
    }

    public JSONObject authByWeChat(String code) {
        String uri = buildUri(code);
        String responseStr = restTemplate.getForObject(uri, String.class);
        JSONObject response = null;
        try {
            response = new JSONObject(responseStr);
        } catch (JSONException exception) {
            logger.error("Parse WeChat data failed", exception);
        }
        return response;
    }

    private String buildUri(String code) {
        return "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WECHAT_APP_ID +
                "&secret=" + WECHAT_APP_SECRET + "&code=" + code + "&grant_type=authorization_code";
    }
}
