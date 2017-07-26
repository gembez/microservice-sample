package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class PaymentsController {

    private final RestTemplate restTemplate;

    public PaymentsController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final Logger log = LoggerFactory.getLogger(PaymentsApplication.class.getName());

    private static final String app_name = "Payments Service";

    @Autowired
    Tracer tracer;

    @RequestMapping("/handlePayment")
    public String handlePayment() {
        String msg = app_name;
        msg += " --> " + restTemplate.getForObject("http://notifications-service/notifyUser", String.class);
        log.info(msg);
        return msg;
    }


    @RequestMapping("/")
    public String test() throws InterruptedException {
        String msg = "Payment Service";
        //Thread.sleep(500);
        log.info(msg);
        return msg;
    }

}