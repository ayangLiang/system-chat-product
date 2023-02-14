package com.example.chat.service;

import com.example.chat.vo.ResultVo;

public interface ChatGptService {

    ResultVo<String> chatGptResponse(String value);

}
