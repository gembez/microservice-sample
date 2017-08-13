package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//@RefreshScope
@Controller
public class WebUIController {

    private final RestTemplate restTemplate;

    public WebUIController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(WebUIApplication.class.getName());

    private static final String device = "web";


    @RequestMapping("/")
    String index(){
        return "index";
    }

    @RequestMapping("/newSession")
    public String newSession(@ModelAttribute(value="person") Person person, HttpSession session, Model model) throws Exception {
        SessionIdentifierGenerator id = new SessionIdentifierGenerator();
        String sessionID = id.nextSessionId();
        session.setAttribute("sessionID", sessionID);
        String username = session.getAttribute("person").toString();
        Date date = new Date();
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate= dateFormat.format(date);

        jdbcTemplate.execute("INSERT INTO sessions(session_id, user_id, session_start, device) VALUES ('" + sessionID + "', '" + username + "', '" + formattedDate + "', '" + device + "');");
        return "index";
    }

    @RequestMapping("/newUser")
    public String newUser(@ModelAttribute(value="person") Person person, Model model, HttpSession session) throws Exception {
        UserGenerator user = new UserGenerator();
        String username = user.generateUser();
        session.setAttribute("person", username);
        SessionIdentifierGenerator id = new SessionIdentifierGenerator();
        String sessionID = id.nextSessionId();
        session.setAttribute("sessionID", sessionID);
        Date date = new Date();
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate= dateFormat.format(date);
        jdbcTemplate.execute("INSERT INTO sessions(session_id, user_id, session_start, device) VALUES ('" + sessionID + "', '" + username + "', '" + formattedDate + "', '" + device + "');");
        return "index";
    }

    @RequestMapping("/generateActivities")
    public String generateActivities(Model model) throws Exception {
        String msg = restTemplate.getForObject("http://zuul-service/generateActivities", String.class);
        log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }


    @RequestMapping("/getCars")
    public String showCars(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "sessionID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://zuul-service/getCars?sessionID=" + sessionID, String.class);
        //log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }


    @RequestMapping("/reserveCar")
    public String reserveCar(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "sessionID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://zuul-service/reserveCar?sessionID=" + sessionID, String.class);
        //log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }

    @RequestMapping("/bookCar")
    public String bookCar(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        //tracer.addTag("sessionID", sessionID);
        //tracer.addTag("device", device);
        String msg = "sessionID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://zuul-service/bookCar?sessionID=" + sessionID, String.class);
        model.addAttribute("msg", msg);
        return "index";
    }


    // TEST
    @RequestMapping("/openCar")
    public String openCar(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "sessionID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://zuul-service/openCar?sessionID=" + sessionID, String.class);
        model.addAttribute("msg", msg);
        return "index";
    }

    @RequestMapping("/endRental")
    public String endRental(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "sessionID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://zuul-service/endRental?sessionID=" + sessionID, String.class);
        model.addAttribute("msg", msg);
        return "index";
    }
/*

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
        return "index";
    }
*/

    @RequestMapping("/showBalance")
    public String showBalance(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "sessionID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://zuul-service/showBalance?sessionID=" + sessionID, String.class);
        //log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }

    @RequestMapping("/bookPackage")
    public String bookPackage(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "sessionID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://zuul-service/bookPackage?sessionID=" + sessionID, String.class);
        //log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }

    @RequestMapping("/showHistory")
    public String showHistory(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "sessionID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://zuul-service/showHistory?sessionID=" + sessionID, String.class);
        //log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }

    @RequestMapping("/findRoute")
    public String findRoute(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "sessionID = " + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://zuul-service/findRoute?sessionID=" + sessionID, String.class);
        //log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }

    @RequestMapping("/reportIssue")
    public String reportIssue(@RequestParam(value="sessionID", required=false, defaultValue="null") String sessionID, Model model) {
        String msg = "sessionID = " + System.getProperty("line.separator") + sessionID + " || WebUI ";
        msg += " --> " + restTemplate.getForObject("http://zuul-service/reportIssue?sessionID=" + sessionID, String.class);
        //log.info(msg);
        model.addAttribute("msg", msg);
        return "index";
    }


}
