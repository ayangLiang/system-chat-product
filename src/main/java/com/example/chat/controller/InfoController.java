package com.example.chat.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {
    @RequestMapping(value = "/info")
    public String info() {
        return "success";
    }
}
