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
public class MapsController {

    private final RestTemplate restTemplate;

    public MapsController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final Logger log = LoggerFactory.getLogger(MapsApplication.class.getName());

    @Autowired
    Tracer tracer;

    @RequestMapping("/generateMap")
    public String generateMap() {
        String msg = "Maps service";
        msg += " --> " + restTemplate.getForObject("http://cars-service/getAvailableCars", String.class);
        log.info(msg);
        return msg;
    }

    @RequestMapping("/getHistoricalRoutes")
    public String generateRoutes() throws InterruptedException {
        String msg = "Maps service";
        Thread.sleep(200);
        log.info(msg);
        return msg;
    }

    @RequestMapping("/getRoute")
    public String calculateRoutes() throws InterruptedException {
        String msg = "Maps service";
        Thread.sleep(600);
        log.info(msg);
        return msg;
    }


}
