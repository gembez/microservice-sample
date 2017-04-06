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

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.security.SecureRandom;


@RefreshScope
@Controller
public class WebUIController {

    private final RestTemplate restTemplate;

    public WebUIController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final Logger log = LoggerFactory.getLogger(WebUIApplication.class.getName());


    @Autowired Tracer tracer;

    @RequestMapping("/")
    String index(){
        return "index";
    }


    @RequestMapping("/login")
    public String login(HttpSession session) {
        String sessionID = nextSessionId();
        session.setAttribute("sessionID", sessionID);
        return "index";
    }

    @RequestMapping("/getCars")
    public String showCars(@RequestParam("sessionID") String sessionID, Model model) {
        String msg = "Session_ID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://localhost:6061/generateMap", String.class);
        tracer.addTag("SessionID", sessionID);
        log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }


    @RequestMapping("/reserveCar")
    public String reserveCar(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "Session_ID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://localhost:9090/reserveCar", String.class);
        tracer.addTag("SessionID", sessionID);
        log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }

    @RequestMapping("/bookCar")
    public String bookCar(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "Session_ID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://localhost:9090/bookCar", String.class);
        tracer.addTag("SessionID", sessionID);
        log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }

    @RequestMapping("/unlockCar")
    public String unlock(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "Session_ID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://localhost:9090/unlockCar", String.class);
        tracer.addTag("SessionID", sessionID);
        log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }


    @RequestMapping("/endRental")
    public String endRental(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "Session_ID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://localhost:9090/lockCar", String.class);
        tracer.addTag("SessionID", sessionID);
        log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }


    @RequestMapping("/showBalance")
    public String showBalance(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "Session_ID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://localhost:6060/getBalance", String.class);
        tracer.addTag("SessionID", sessionID);
        log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }

    @RequestMapping("/bookPackage")
    public String bookPackage(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "Session_ID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://localhost:6060/newPackage", String.class);
        tracer.addTag("SessionID", sessionID);
        log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }

    @RequestMapping("/showHistory")
    public String showHistory(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "Session_ID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://localhost:6062/getUserHistory", String.class);
        tracer.addTag("SessionID", sessionID);
        log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }

    @RequestMapping("/findRoute")
    public String findRoute(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "Session_ID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://localhost:6061/getShortestRoute", String.class);
        tracer.addTag("SessionID", sessionID);
        log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }

    private SecureRandom random = new SecureRandom();

    public String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }
}
