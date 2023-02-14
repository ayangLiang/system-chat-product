package com.example.chat.client;

import com.alibaba.fastjson.JSON;
import com.example.chat.vo.ai.Answer;
import com.example.chat.vo.ai.Choices;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.ServerException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Properties;

@Component
public class ChatGptClient {

    @Value("${SECRET_KEY}")
    private String openAiKey;

    @Value("${connectTimeout}")
    private String connectTimeout;

    @Value("${connectionRequestTimeout}")
    private String connectionRequestTimeout;

    @Value("${socketTimeout}")
    private String socketTimeout;
    public HttpPost getHttpPost() throws IOException {

        HttpPost post = new HttpPost("https://api.openai.com/v1/completions");
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer " + openAiKey);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Integer.parseInt(connectTimeout)).setConnectionRequestTimeout(Integer.parseInt(connectionRequestTimeout))
                .setSocketTimeout(Integer.parseInt(socketTimeout)).build();
        post.setConfig(requestConfig);
        return post;
    }

    public SSLConnectionSocketFactory getSslConnectionSocketFactory() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (x509Certificates, s) -> true;
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        return new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());

    }

    public String getRequestJson(String question) {
        return "{\"model\": \"text-davinci-003\", \"prompt\": \"" + question + "\", \"temperature\": 0, \"max_tokens\": 1024}";
    }

    public ContentType getContentType() {
        return ContentType.create("text/json", "UTF-8");
    }

    public String printAnswer(CloseableHttpResponse response) {
        try {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String responseJson = EntityUtils.toString(response.getEntity());
                Answer answer = JSON.parseObject(responseJson, Answer.class);
                StringBuilder answers = new StringBuilder();
                List<Choices> choices = answer.getChoices();
                for (Choices choice : choices) {
                    answers.append(choice.getText());
                }
                System.out.println(answers.substring(2, answers.length()));
                return answers.toString();
            } else if (response.getStatusLine().getStatusCode() == 429) {
                System.out.println("-- warning: Too Many Requests!");
            } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                throw new ServerException("------ Server error, program terminated! ------");
            } else {
                System.out.println("-- warning: Error, please try again!");
            }
        } catch (Exception e) {
            System.out.println("-- warning: Error, please try again!");
        }
        return null;

    }
}
