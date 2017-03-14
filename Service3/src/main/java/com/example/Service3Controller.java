package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Johnny on 08.03.17.
 */

@RefreshScope
@RestController
public class Service3Controller {

    private final RestTemplate restTemplate;

    public Service3Controller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final Logger log = LoggerFactory.getLogger(Service3Application.class.getName());

    @Autowired
    Tracer tracer;

    @RequestMapping("/")
    public String root() {
        String msg = "Service 3 : Root";
        log.info(msg);
        tracer.addTag("SessionID", "123456789");
        return msg;
    }

}
