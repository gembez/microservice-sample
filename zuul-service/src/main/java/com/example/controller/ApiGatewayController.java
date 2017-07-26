package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class ApiGatewayController {

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(ApiGatewayController.class.getName());

    private static final String device = "web";

    @Autowired
    Tracer tracer;


    @RequestMapping(method=RequestMethod.GET, value="test/{service}")
    public String getNamesForService(@PathVariable("service") String service) {
        log.info(service);
        ResponseEntity<String> profiles =
                restTemplate.exchange(
                        "http://" + service,
                        HttpMethod.PUT,
                        null,
                        String.class);

        return profiles.getBody().toString();

    }

    // one-to-many call
    @RequestMapping(method=RequestMethod.GET, value="/lockCar")
    public String getProfileNamesForServices() {
        String results = getNamesForService("accounting/handleCarBooking");
        String additionalResults = getNamesForService("payments/handlePayment");
        results = results.concat(" --> " + additionalResults);
        return results;
    }

    /*
    @RequestMapping(method=RequestMethod.GET, value="/test")
    public String test() {
        ResponseEntity<String> profiles =
                restTemplate.exchange(
                        "http://accounting/",
                        HttpMethod.GET,
                        null,
                        String.class);

        return profiles.getBody().toString();
    }
    */

    @RequestMapping("/getCars")
    public String showCars(@RequestParam("sessionID") String sessionID, Model model) {
        tracer.addTag("sessionID", sessionID);
        tracer.addTag("device", device);
        String msg = "sessionID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://maps-service/generateMap", String.class);
        //log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }


    @RequestMapping("/reserveCar")
    public String reserveCar(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        tracer.addTag("sessionID", sessionID);
        tracer.addTag("device", device);
        String msg = "sessionID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://cars-service/allocateCar", String.class);
        //log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }

    @RequestMapping("/bookCar")
    public String bookCar(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        tracer.addTag("sessionID", sessionID);
        tracer.addTag("device", device);
        String msg = "sessionID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://cars-service/handleCarBooking", String.class);
        model.addAttribute("msg", msg);
        return "index";
    }

    @RequestMapping("/openCar")
    public String openCar(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        tracer.addTag("sessionID", sessionID);
        tracer.addTag("device", device);
        String msg = "sessionID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://cars-service/unlockCar", String.class);
        //log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }


    @RequestMapping("/endRental")
    public String endRental(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) throws InterruptedException {
        tracer.addTag("sessionID", sessionID);
        tracer.addTag("device", device);
        String msg = "sessionID = " + sessionID + " || WebUI ";
        //Thread.sleep(2200);
        msg += " --> " + restTemplate.getForObject("http://cars-service/lockCar", String.class);
        ResponseEntity<String> profiles =
                restTemplate.exchange(
                        "http://accounting-service/finalizeBooking",
                        HttpMethod.GET,
                        null,
                        String.class);
        msg += " --> " + profiles.getBody().toString();
        //log.info(msg);
        model.addAttribute("msg", msg);
        return msg;
    }


    @RequestMapping("/showBalance")
    public String showBalance(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        tracer.addTag("sessionID", sessionID);
        tracer.addTag("device", device);
        String msg = "sessionID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://accounting-service/getBalance", String.class);
        //log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }

    @RequestMapping("/bookPackage")
    public String bookPackage(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        tracer.addTag("sessionID", sessionID);
        tracer.addTag("device", device);
        String msg = "sessionID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://accounting-service/newPackage", String.class);
        //log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }

    @RequestMapping("/showHistory")
    public String showHistory(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        tracer.addTag("sessionID", sessionID);
        tracer.addTag("device", device);
        String msg = "sessionID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://user-service/getUserHistory", String.class);
        //log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }

    @RequestMapping("/findRoute")
    public String findRoute(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        tracer.addTag("sessionID", sessionID);
        tracer.addTag("device", device);
        String msg = "sessionID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://maps-service/getRoute", String.class);
        //log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }

    @RequestMapping("/reportIssue")
    public String reportIssue(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        tracer.addTag("sessionID", sessionID);
        tracer.addTag("device", device);
        String msg = "sessionID = " + System.getProperty("line.separator") + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://cars-service/createIssue", String.class);
        //log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }


    @RequestMapping("/test")
    public String test(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        tracer.addTag("sessionID", sessionID);
        tracer.addTag("device", device);
        String msg = "sessionID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://cars-service/lockCar", String.class);
        //log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }



}

