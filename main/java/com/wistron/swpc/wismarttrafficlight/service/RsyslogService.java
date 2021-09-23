package com.wistron.swpc.wismarttrafficlight.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 单独提供 rsyslog 记录服务
 */
@Service
public class RsyslogService {

    private final Logger logger = LoggerFactory.getLogger(RsyslogService.class);

    public void log(String msg) {
        // logger.info("webserver: {}", msg);
        String log = logSecureService.vaildLog("webserver: " + msg);
        logger.info(log);
    }

}
