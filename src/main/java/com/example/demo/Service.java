package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;


@org.springframework.stereotype.Service
public class Service {
	  @Autowired
	    private RestTemplate restTemplate;
	
    public  String getEmployeeById() {

        Map < String, String > params = new HashMap < String, String > ();
        params.put("id", "1");

        String result = restTemplate.getForObject("https://localhost:8443/", String.class, params);

        System.out.println(result);
        return result;
    }

}
