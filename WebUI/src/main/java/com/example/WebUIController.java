package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;


@RefreshScope
@Controller
public class WebUIController {

    private final RestTemplate restTemplate;

    public WebUIController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final Logger log = LoggerFactory.getLogger(WebUIApplication.class.getName());


    @Autowired Tracer tracer;

    /*
    @RequestMapping("/login")
    public String login() {
        String msg = "service1 : callService2";
        tracer.addTag("SessionID", "123456789");
        msg += " --> " + restTemplate.getForObject("http://localhost:9090", String.class);
        log.info(msg);
        return msg;
    }

    */

    @RequestMapping("/getCars")
    public String showCars(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "WebUI" + ", Session ID = " + sessionID;
        msg += " --> " + restTemplate.getForObject("http://localhost:6061/generateMap", String.class);
        tracer.addTag("SessionID", sessionID);
        log.info(msg);
        model.addAttribute("msg", msg);
        return "main";
    }

    @RequestMapping("/reserveCar")
    public String reserveCar(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "WebUI" + ", Session ID = " + sessionID;
        msg += " --> " + restTemplate.getForObject("http://localhost:9090/reserveCar", String.class);
        tracer.addTag("SessionID", sessionID);
        log.info(msg);
        model.addAttribute("msg", msg);
        return "main";
    }

    @RequestMapping("/bookCar")
    public String bookCar(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "WebUI" + ", Session ID = " + sessionID;
        msg += " --> " + restTemplate.getForObject("http://localhost:9090/bookCar", String.class);
        tracer.addTag("SessionID", sessionID);
        log.info(msg);
        model.addAttribute("msg", msg);
        return "main";
    }

    @RequestMapping("/unlockCar")
    public String unlock(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "WebUI" + ", Session ID = " + sessionID;
        msg += " --> " + restTemplate.getForObject("http://localhost:9090/unlockCar", String.class);
        tracer.addTag("SessionID", sessionID);
        log.info(msg);
        model.addAttribute("msg", msg);
        return "main";
    }


    @RequestMapping("/endRental")
    public String endRental(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "WebUI" + ", Session ID = " + sessionID;
        msg += " --> " + restTemplate.getForObject("http://localhost:9090/lockCar", String.class);
        tracer.addTag("SessionID", sessionID);
        log.info(msg);
        model.addAttribute("msg", msg);
        return "main";
    }


    @RequestMapping("/showBalance")
    public String showBalance(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "WebUI" + ", Session ID = " + sessionID;
        msg += " --> " + restTemplate.getForObject("http://localhost:6060/getBalance", String.class);
        tracer.addTag("SessionID", sessionID);
        log.info(msg);
        model.addAttribute("msg", msg);
        return "main";
    }

    @RequestMapping("/bookPackage")
    public String bookPackage(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "WebUI" + ", Session ID = " + sessionID;
        msg += " --> " + restTemplate.getForObject("http://localhost:6060/newPackage", String.class);
        tracer.addTag("SessionID", sessionID);
        log.info(msg);
        model.addAttribute("msg", msg);
        return "main";
    }

    @RequestMapping("/showHistory")
    public String showHistory(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "WebUI" + ", Session ID = " + sessionID;
        msg += " --> " + restTemplate.getForObject("http://localhost:6062/getUserHistory", String.class);
        tracer.addTag("SessionID", sessionID);
        log.info(msg);
        model.addAttribute("msg", msg);
        return "main";
    }

    @RequestMapping("/findRoute")
    public String findRoute(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "WebUI" + ", Session ID = " + sessionID;
        msg += " --> " + restTemplate.getForObject("http://localhost:6061/getShortestRoute", String.class);
        tracer.addTag("SessionID", sessionID);
        log.info(msg);
        model.addAttribute("msg", msg);
        return "main";
    }

}
