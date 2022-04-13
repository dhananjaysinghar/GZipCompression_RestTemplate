package com.gzip.controller;

import com.gzip.config.AppConfig;
import com.gzip.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    @Qualifier("gzipRestTemplate")
    private RestTemplate restTemplate;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public String addData(@RequestBody User user) {
        return restTemplate.exchange("http://localhost:8080/test/gzip-type", HttpMethod.POST, new HttpEntity<>(user), String.class).getBody();
    }


    @PostMapping("/gzip-type")
    public String addData1(@RequestBody byte[] user) {
        HttpServletRequest httpRequest = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        return httpRequest.getHeader("content-encoding") + " , Data received : " + AppConfig.decompressGzipByteArray(user);
    }
}
