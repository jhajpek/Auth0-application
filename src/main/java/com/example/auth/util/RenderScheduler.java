package com.example.auth.util;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class RenderScheduler {

    private final static Logger LOGGER = Logger.getLogger(RenderScheduler.class.getName());

    @Scheduled(cron = "0 * * * * *")
    public void keepRenderAwake() {
        LOGGER.info("Keeping the render service up...");
    }

}
