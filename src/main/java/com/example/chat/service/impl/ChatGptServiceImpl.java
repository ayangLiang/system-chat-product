package com.example.chat.service.impl;

import com.example.chat.client.ChatGptClient;
import com.example.chat.enmus.ErrorConstantEnum;
import com.example.chat.service.ChatGptService;
import com.example.chat.vo.ResultVo;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ChatGptServiceImpl implements ChatGptService {

    @Resource
    private ChatGptClient client;

    @Override
    public ResultVo<String> chatGptResponse(String value) {
        try {
            HttpPost post = client.getHttpPost();
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setSSLSocketFactory(client.getSslConnectionSocketFactory())
                    .build();
            String question = value;
            StringEntity stringEntity = new StringEntity(client.getRequestJson(question), client.getContentType());
            post.setEntity(stringEntity);
            CloseableHttpResponse response = null;
            response = httpClient.execute(post);
            return ResultVo.buildSuccess().setData(client.printAnswer(response));
        } catch (Exception e) {
            System.out.println("-- warning: Please try again!,e"+e.toString());
            return ResultVo.build(ErrorConstantEnum.FAILURE);
        }

    }
}
