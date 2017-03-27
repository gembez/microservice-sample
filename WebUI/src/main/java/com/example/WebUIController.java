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

    /*@Value("${message:Hello default}")
    private String message;

    @RequestMapping("/message")
    String getMessage() {
        return this.message;
    }

    @RequestMapping("/")
    public String root() {
        String msg = "Service 1 : Root";
        log.info(msg);
        tracer.addTag("SessionID", "123456789");
        return msg;
    }


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
    public String showCars(@RequestParam(value="sessionID", required=false, defaultValue="000") String sessionID, Model model) {
        String msg = "WebUI" + ", Session ID= " + sessionID;
        msg += " --> " + restTemplate.getForObject("http://localhost:6061/generateMap", String.class);
        tracer.addTag("SessionID", sessionID);
        log.info(msg);
        model.addAttribute("msg", msg);
        return "main";
    }

    @RequestMapping("/reserveCar")
    public String reserveCar(@RequestParam(value="sessionID", required=false, defaultValue="000") String sessionID, Model model) {
        String msg = "WebUI" + ", Session ID= " + sessionID;
        msg += " --> " + restTemplate.getForObject("http://localhost:9090/reserveCar", String.class);
        tracer.addTag("SessionID", sessionID);
        log.info(msg);
        model.addAttribute("msg", msg);
        return "main";
    }

    @RequestMapping("/bookCar")
    public String bookCar(@RequestParam(value="sessionID", required=false, defaultValue="000") String sessionID, Model model) {
        String msg = "WebUI" + ", Session ID= " + sessionID;
        msg += " --> " + restTemplate.getForObject("http://localhost:9090/bookCar", String.class);
        tracer.addTag("SessionID", sessionID);
        log.info(msg);
        model.addAttribute("msg", msg);
        return "main";
    }




}
