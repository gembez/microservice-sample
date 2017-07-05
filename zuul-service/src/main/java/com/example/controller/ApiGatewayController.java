package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RefreshScope
public class ApiGatewayController {

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(ApiGatewayController.class.getName());

    // unklar wie ich endpoint despite root aufrufen kann
    // one-to-one call
    @RequestMapping(method=RequestMethod.GET, value="test/{service}")
    public String getNamesForService(@PathVariable("service") String service) {
        log.info(service);
        ResponseEntity<String> profiles =
                restTemplate.exchange(
                        "http://" + service + "/",
                        HttpMethod.GET,
                        null,
                        String.class);

        return profiles.getBody().toString();

    }

    // one-to-many call
    @RequestMapping(method=RequestMethod.GET, value="/lockCar")
    public String getProfileNamesForServices() {
        String results = getNamesForService("accounting");
        String svcbResults = getNamesForService("payments");
        results = results.concat(" --> " + svcbResults);
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


}

