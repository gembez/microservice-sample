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

    public AccountingController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final Logger log = LoggerFactory.getLogger(AccountingApplication.class.getName());

    @Autowired
    Tracer tracer;

    @RequestMapping("/handleBooking")
    public String handleBooking() {
        String msg = "Accounting Service";
        log.info(msg);
        return msg;
    }

    @RequestMapping("/getBalance")
    public String displayBalance() {
        String msg = "Accounting Service";
        log.info(msg);
        return msg;
    }

    @RequestMapping("/finalizeBooking")
    public String finalizeBooking() {
        String msg = "Accounting Service";
        msg += " --> " + restTemplate.getForObject("http://localhost:5050/handlePayment", String.class);
        log.info(msg);
        return msg;
    }

    @RequestMapping("/newPackage")
    public String bookPackage() {
        String msg = "Accounting Service";
        msg += " --> " + restTemplate.getForObject("http://localhost:5050/handlePayment", String.class);
        log.info(msg);
        return msg;
    }



}
