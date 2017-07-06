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

//@RefreshScope
@RestController
public class CarManagementController {

    public CarManagementController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(CarManagementApplication.class.getName());

    @Autowired Tracer tracer;

    @RequestMapping("/getAvailableCars")
    public String getAvailableCars() throws InterruptedException {
        String msg = "Car Management Service";
        Thread.sleep(800);
        log.info(msg);
        return msg;
    }

    @RequestMapping("/allocateCar")
    public String allocateCar() {
        String msg = "Car Management Service";
        msg += " --> " + restTemplate.getForObject("http://notifications-service/notifyUser", String.class);
        log.info(msg);
        return msg;
    }

    @RequestMapping("/handleCarBooking")
    public String handleCarReservation() {
        String msg = "Car Management Service";
        msg += " --> " + restTemplate.getForObject("http://accounting-service/initilizeBooking", String.class);
        log.info(msg);
        return msg;
    }

    @RequestMapping("/unlockCar")
    public String unlockCar() throws InterruptedException {
        String msg = "Car Management Service";
        Thread.sleep(300);
        log.info(msg);
        return msg;
    }

    @RequestMapping("/createIssue")
    public String createIssue() throws InterruptedException {
        String msg = "Car Management Service";
        Thread.sleep(200);
        log.info(msg);
        return msg;
    }

    @RequestMapping("/lockCar")
    public String lockCar() {
        String msg = "Car Management Service";
        msg += " --> " + restTemplate.getForObject("http://accounting-service/finalizeBooking", String.class);
        log.info(msg);
        return msg;
    }

    @RequestMapping("/getCarHistory")
    public String showCarHistory() {
        String msg = "Car Management Service";
        msg += " --> " + restTemplate.getForObject("http://maps-service/getHistoricalRoutes", String.class);
        log.info(msg);
        return msg;
    }

}
