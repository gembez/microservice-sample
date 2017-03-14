package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Johnny on 08.03.17.
 */

@RestController
public class Service2Controller {

    public Service2Controller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(Service2Application.class.getName());

    @Autowired Tracer tracer;

    @RequestMapping("/")
    public String root() {
        String msg = "Service 2 : Root";
        msg += " --> " + callService3();
        log.info(msg);
        tracer.addTag("SessionID", "123456789");
        return msg;
    }

    public String callService3() {
        return restTemplate.getForObject("http://localhost:6060", String.class);
    }
}
