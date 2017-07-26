package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RefreshScope
@RestController
public class AccountingController {

    private final RestTemplate restTemplate;
    private static final Logger log = LoggerFactory.getLogger(AccountingApplication.class.getName());
    private static final String app_name = "Accounting Service";

    public AccountingController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping("/initializeBooking")
    public String initialize() throws InterruptedException {
        String msg = app_name;
        Thread.sleep(500);
        log.info(msg);
        return msg;
    }

    @RequestMapping("/getBalance")
    public String getBalance() throws InterruptedException {
        String msg = app_name;
        // query database for user's balance
        Thread.sleep(300);
        log.info(msg);
        return msg;
    }

    @RequestMapping("/finalizeBooking")
    public String finalizeBooking() throws InterruptedException {
        String msg = app_name;
        Thread.sleep(200);
        msg += " --> " + restTemplate.getForObject("http://payments-service/handlePayment", String.class);
        log.info(msg);
        return msg;
    }

    @RequestMapping("/newPackage")
    public String newPackage() throws InterruptedException {
        String msg = app_name;
        Thread.sleep(400);
        msg += " --> " + restTemplate.getForObject("http://payments-service/handlePayment", String.class);
        log.info(msg);
        return msg;
    }

    @RequestMapping("/test")
    public String test() throws InterruptedException {
        String msg = app_name;
        //Thread.sleep(500);
        log.info(msg);
        return msg;
    }

}
