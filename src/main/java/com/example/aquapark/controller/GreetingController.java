package com.example.aquapark.controller;

import java.util.concurrent.atomic.AtomicLong;

import com.example.aquapark.model.Greeting;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";

    private final AtomicLong counter = new AtomicLong();

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(required = false, defaultValue = "World") String name) {
        System.out.println("==== get greeting ====");
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

}