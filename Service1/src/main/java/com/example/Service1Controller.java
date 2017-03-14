package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class Service1Controller {

    private final RestTemplate restTemplate;

    public Service1Controller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final Logger log = LoggerFactory.getLogger(Service1Application.class.getName());


    @Autowired Tracer tracer;

    @Value("${message:Hello default}")
    private String message;

    @RequestMapping("/message")
    String getMessage() {
        return this.message;
    }

    @RequestMapping("/")
    public String root() {
        String msg = "Service 1 : Root";
        log.info(msg);
        tracer.addTag("SessionID", "123456789");
        return msg;
    }

    @RequestMapping("/callService2")
    public String callService2() {
        String msg = "service1 : callService2";
        tracer.addTag("SessionID", "123456789");
        msg += " --> " + restTemplate.getForObject("http://localhost:9090", String.class);
        log.info(msg);
        return msg;
    }

    @RequestMapping("/callService4")
    public String callService4() {
        String msg = "service1 : callService4";
        msg += " --> " + restTemplate.getForObject("http://localhost:5050", String.class);
        tracer.addTag("SessionID", "123456789");
        log.info(msg);
        return msg;
    }

}
