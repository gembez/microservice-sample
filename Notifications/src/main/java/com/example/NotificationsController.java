package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RefreshScope
@RestController
public class NotificationsController {

    private final RestTemplate restTemplate;

    public NotificationsController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final Logger log = LoggerFactory.getLogger(NotificationsApplication.class.getName());

    @Autowired
    Tracer tracer;

    @RequestMapping("/notifyUser")
    public String notifyUser() {
        String msg = "Notification Service";
        log.info(msg);
        return msg;
    }

}
