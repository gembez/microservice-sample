package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

//@RefreshScope
@RestController
public class UserManagementController {

    private final RestTemplate restTemplate;

    public UserManagementController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final Logger log = LoggerFactory.getLogger(UserManagementApplication.class.getName());

    @Autowired
    Tracer tracer;

    private static final String app_name = "User Service";


    @RequestMapping("/getUserHistory")
    public String showHistory() {
        String msg = app_name;
        msg += " --> " + restTemplate.getForObject("http://cars-service/getCarHistory", String.class);
        log.info(msg);
        return msg;
    }


    @RequestMapping("/test")
    public String test() {
        String msg = app_name;
        //msg += " --> " + restTemplate.getForObject("http://cars-service/getCarHistory", String.class);
        log.info(msg);
        return msg;
    }

}
