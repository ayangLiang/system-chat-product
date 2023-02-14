package com.example.chat.controller;

import com.example.chat.service.ChatGptService;
import com.example.chat.vo.ResultVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ChatController extends AbstractV1Controller {

    @Resource
    private ChatGptService chatGptService;

    @GetMapping(value = "chat")
    public ResultVo<String> getChat(@RequestParam(value = "value") String value){
        return chatGptService.chatGptResponse(value);
    }
}
