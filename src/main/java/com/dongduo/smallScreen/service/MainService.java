package com.dongduo.smallScreen.service;

import com.dongduo.smallScreen.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MainService {
    private static final Logger logger = LoggerFactory.getLogger(MainService.class);

    @Value("${upload.url}")
    private String uploadUrl;

    @Value("${upload.enable}")
    private boolean enableUpload;

    @Async
    public void uploadStat(int inCount, int outCount) {
        if (enableUpload) {
            Map<String, String> param = new HashMap<>();
            param.put("inNum", String.valueOf(inCount));
            param.put("outNum", String.valueOf(outCount));
            try {
                HttpUtil.doGet(uploadUrl, param);
            } catch (Exception e) {
                logger.error("上传人数统计出错。", e);
            }
        }
    }
}
