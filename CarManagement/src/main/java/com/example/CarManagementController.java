package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Johnny on 08.03.17.
 */

@RestController
public class CarManagementController {

    public CarManagementController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(CarManagementApplication.class.getName());

    @Autowired Tracer tracer;

    @RequestMapping("/getAvailableCars")
    public String getAvailableCars() {
        String msg = "Car Management Service";
        log.info(msg);
        return msg;
    }

    @RequestMapping("/reserveCar")
    public String reserveCar() {
        String msg = "Car Management Service";
        msg += " --> " + restTemplate.getForObject("http://localhost:6063/notifyUser", String.class);
        log.info(msg);
        return msg;
    }

    @RequestMapping("/bookCar")
    public String bookCar() {
        String msg = "Car Management Service";
        msg += " --> " + restTemplate.getForObject("http://localhost:6060/handleBooking", String.class);
        log.info(msg);
        return msg;
    }

    @RequestMapping("/unlockCar")
    public String unlockCar() {
        String msg = "Car Management Service";
        log.info(msg);
        return msg;
    }

}
