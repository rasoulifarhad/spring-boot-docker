package com.farhad.example.dockerdemo;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreatingResource {
    
    private static final String template = "Hello, %s!";
    private AtomicLong atomicLong = new AtomicLong(0);

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(atomicLong.incrementAndGet(), String.format(template, name));
    }
}
