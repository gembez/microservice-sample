package com.example.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.model.SocialProfile;

@RestController
@RequestMapping("/profiles")
public class SocialProfileApiGatewayController {
    
    private static final String SERVICE_A = "svca-service";
    private static final String SERVICE_B = "svcb-service";

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private Source source;
    
    @RequestMapping(method=RequestMethod.GET, value="/names/{service}")
    public Collection<String> getProfileNamesForService(@PathVariable("service") String service) {
        try {
            ParameterizedTypeReference<Resources<SocialProfile>> typeReference = new ParameterizedTypeReference<Resources<SocialProfile>>() {
            };
            ResponseEntity<Resources<SocialProfile>> profiles =
                    restTemplate.exchange(
                            "http://" + service + "/socialProfiles",
                            HttpMethod.GET,
                            null,
                            typeReference);
            return profiles.getBody().getContent()
                    .stream()
                    .map(SocialProfile::getName)
                    .collect(Collectors.toList());
        }catch(Exception e){
            e.printStackTrace();
        }

    return null;
    }
    
    @RequestMapping(method=RequestMethod.GET, value="/names")
    public Collection<String> getProfileNamesForService() {
        Collection<String> results = getProfileNamesForService(SERVICE_A);
        Collection<String> svcbResults = getProfileNamesForService(SERVICE_B);
        results.addAll(svcbResults);
        return results;
    }


    
}
