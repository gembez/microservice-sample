package com.example;

import com.netflix.discovery.DiscoveryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;

//@RefreshScope
@Controller
public class WebUIController {

    private final RestTemplate restTemplate;

    public WebUIController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    //private static final Logger log = LoggerFactory.getLogger(WebUIApplication.class.getName());

    private static final String device = "web";

    @Autowired
    Tracer tracer;

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
        jdbcTemplate.execute("INSERT INTO sessions(session_id, user_name) VALUES ('" + sessionID + "', '" + username + "');");
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
        jdbcTemplate.execute("INSERT INTO sessions(session_id, user_name) VALUES ('" + sessionID + "', '" + username + "');");
        return "index";
    }

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
        return "index";
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


    // @Scheduled(fixedRate=30000) // script is executed every 30 s
    @RequestMapping("/generateActivities")
    public String generateActivities() throws Exception {

        jdbcTemplate.execute("INSERT INTO activities_am (\n" +
                "session_id )\n" +
                "SELECT 1 AS session_ID;\n");


        /*

        // empty activities table
        jdbcTemplate.execute("DELETE FROM activities;");

        // create successful activities
        jdbcTemplate.execute("INSERT INTO activities (\n" +
                "  start_ts, end_ts, session_id, activity, trace_id\n" +
                ")\n" +
                "  SELECT DISTINCT\n" +
                "    FROM_UNIXTIME((s1.start_ts / 1000000))   AS start_ts,\n" +
                "    FROM_UNIXTIME((a4.a_timestamp / 1000000)) AS end_ts,\n" +
                "    a3.a_value                                AS session_ID,\n" +
                "    SUBSTRING(s1.name, 7) AS activity,\n" +
                "    LOWER(HEX(s1.trace_id))                  AS trace_id\n" +
                " FROM zipkin_annotations AS a1\n" +
                "    INNER JOIN zipkin_spans AS s1 ON\n" +
                "                                    s1.trace_id = a1.trace_id\n" +
                "                                    AND s1.id = a1.span_id\n" +
                "                                     AND s1.parent_id IS NULL\n" +
                "                                     AND a1.a_key = 'sr'\n" +
                "    INNER JOIN zipkin_annotations AS a2 ON\n" +
                "                                          a1.trace_id = a2.trace_id\n" +
                "                                          AND a2.span_id = a2.trace_id\n" +
                "                                          AND a1.trace_id NOT IN (SELECT trace_id FROM zipkin_annotations WHERE a_key = 'error')\n" +
                "                                          AND a2.a_key = 'ss'\n" +
                "    JOIN zipkin_annotations AS a3 ON\n" +
                "                                          a3.a_key = 'sessionID'\n" +
                "                                          AND a1.trace_id = a3.trace_id\n" +
                "                                          AND a3.span_id IN (SELECT DISTINCT b.id\n" +
                "                                                             FROM zipkin_spans AS a, zipkin_spans AS b\n" +
                "                                                             WHERE a.parent_id IS NULL\n" +
                "                                                                   AND a.trace_id = b.trace_id\n" +
                "                                                                   AND b.parent_id = a.id\n" +
                "                                          )\n" +
                "    JOIN zipkin_annotations AS a4 ON\n" +
                "                                          a1.trace_id = a4.trace_id\n" +
                "                                          AND a4.span_id = a4.trace_id\n" +
                "                                          AND a4.a_key = 'ss';");

        // create failed actitvies
        jdbcTemplate.execute("INSERT INTO activities (\n" +
                "  start_ts, end_ts, session_id, activity, trace_id\n" +
                ")\n" +
                "  SELECT DISTINCT\n" +
                "    FROM_UNIXTIME((s1.start_ts / 1000000))   AS start_ts,\n" +
                "    FROM_UNIXTIME((a4.a_timestamp / 1000000)) AS end_ts,\n" +
                "    a3.a_value                                AS session_ID,\n" +
                "    CONCAT(SUBSTRING(s1.name, 7), ' failed') AS activity,\n" +
                "    LOWER(HEX(s1.trace_id))                  AS trace_id\n" +
                " FROM zipkin_annotations AS a1\n" +
                "    INNER JOIN zipkin_spans AS s1 ON\n" +
                "                                    s1.trace_id = a1.trace_id\n" +
                "                                    AND s1.id = a1.span_id\n" +
                "                                     AND s1.parent_id IS NULL\n" +
                "                                     AND a1.a_key = 'sr'\n" +
                "    INNER JOIN zipkin_annotations AS a2 ON\n" +
                "                                          a1.trace_id = a2.trace_id\n" +
                "                                          AND a2.span_id = a2.trace_id\n" +
                "                                          AND a2.a_key = 'error'\n" +
                "                                          AND a1.trace_id IN (SELECT trace_id FROM zipkin_annotations WHERE a_key = 'error')\n" +
                "    JOIN zipkin_annotations AS a3 ON\n" +
                "                                          a3.a_key = 'sessionID'\n" +
                "                                          AND a1.trace_id = a3.trace_id\n" +
                "                                          AND a3.span_id IN (SELECT DISTINCT b.id\n" +
                "                                                             FROM zipkin_spans AS a, zipkin_spans AS b\n" +
                "                                                             WHERE a.parent_id IS NULL\n" +
                "                                                                   AND a.trace_id = b.trace_id\n" +
                "                                                                   AND b.parent_id = a.id\n" +
                "                                          )\n" +
                "    JOIN zipkin_annotations AS a4 ON\n" +
                "                                          a1.trace_id = a4.trace_id\n" +
                "                                          AND a4.span_id = a4.trace_id\n" +
                "                                          AND a4.a_key = 'ss';");


        // create application activities
        jdbcTemplate.execute("INSERT INTO activities (\n" +
                "  start_ts, end_ts, session_id, activity, trace_id, layer\n" +
                ")\n" +
                "  SELECT DISTINCT\n" +
                "    FROM_UNIXTIME((s1.start_ts / 1000000))    AS start_ts,\n" +
                "    NULL                                      AS end_ts,\n" +
                "    a3.a_value                                AS session_ID,\n" +
                "    CONCAT('a_', a1.endpoint_service_name)    AS activity,\n" +
                "    LOWER(HEX(a1.trace_id))                   AS trace_id,\n" +
                "    'application'                                AS layer\n" +
                "  FROM zipkin_annotations AS a1\n" +
                "    INNER JOIN zipkin_spans AS s1 ON\n" +
                "                                    s1.trace_id = a1.trace_id\n" +
                "                                    AND s1.id = a1.span_id\n" +
                "                                    AND a1.a_key = 'sr'\n" +
                "    JOIN zipkin_annotations AS a3 ON\n" +
                "                                          a3.a_key = 'sessionID'\n" +
                "                                          AND a1.trace_id = a3.trace_id\n" +
                "                                          AND a3.span_id IN (SELECT DISTINCT b.id\n" +
                "                                                             FROM zipkin_spans AS a, zipkin_spans AS b\n" +
                "                                                             WHERE a.parent_id IS NULL\n" +
                "                                                                   AND a.trace_id = b.trace_id\n" +
                "                                                                   AND b.parent_id = a.id\n" +
                "                                          );\n");

        // fill trigger table with timestamp in order to trigger a reload the data model in celonis (table is beeing checked every 5 min)
        jdbcTemplate.execute("INSERT INTO RELOAD_TRIGGER_TABLE (.RELOAD_TRIGGER_TABLE.DATA_MODEL_NAME, .RELOAD_TRIGGER_TABLE.RELOAD_REQUEST_TIME) VALUES ('Datamodel', AddTime(now(), '00:00:00'));");

        */

        return "index";
    }


/*
    @RequestMapping("/reloadModel")
    public String reloadModel() throws Exception {

        // empty activities table
        jdbcTemplate.execute("INSERT INTO RELOAD_TRIGGER_TABLE (.RELOAD_TRIGGER_TABLE.DATA_MODEL_NAME, .RELOAD_TRIGGER_TABLE.RELOAD_REQUEST_TIME) VALUES ('Datamodel', AddTime(now(), '00:00:00'));");
        return "index";

    }
*/

    }
