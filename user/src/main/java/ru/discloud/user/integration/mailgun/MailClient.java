package ru.discloud.user.integration.mailgun;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.discloud.user.UserApplication;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

@Component
public class MailClient {
    private static String BASE_URL = "https://api.mailgun.net";
    private static String BASE_PATH = "/v3/mg.operative.cloud";
    private static Boolean DEFAULT_DEBUG = false;
    private static Integer DEFAULT_TIMEOUT = 60000;

    private final Logger logger = LoggerFactory.getLogger(UserApplication.class);
    private AsyncHttpClient httpClient;
    private ObjectMapper mapper;
    private String basicAuth;
    private String baseUrl;
    private String apiKey;

    public MailClient() {
        this.baseUrl = BASE_URL + BASE_PATH;
        this.apiKey = "key-5eff95b5abfef96b234b88d35ac49c74";
        this.basicAuth = Base64.getEncoder().encodeToString(("api:" + apiKey).getBytes(StandardCharsets.UTF_8));
        DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
                .setConnectTimeout(DEFAULT_TIMEOUT);
        this.httpClient = Dsl.asyncHttpClient(clientBuilder);
        this.mapper = new ObjectMapper();
    }

    public CompletableFuture<SendMessageResponse> sendMessage(SendMessageRequest request) throws JsonProcessingException {
        RequestBuilder requestBuilder = new RequestBuilder("POST")
                .setUrl(baseUrl)
                .setHeader("Authorization", this.basicAuth)
                .setHeader("Content-Type", "application/json")
                .setBody(mapper.writeValueAsString(request));
        Request sendMessageRequest = requestBuilder.build();
        CompletableFuture<Response> responseFuture = httpClient.executeRequest(sendMessageRequest).toCompletableFuture();
        return responseFuture.thenApply(response -> {
            SendMessageResponse sendMessageResponse = null;
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (response.getStatusCode() != 200) {
                logger.error("SendMessage response error: code = " + response.getStatusCode() + ", body = " + response.getResponseBody());
            } else {
                try {
                    sendMessageResponse = mapper.readValue(response.getResponseBody(), SendMessageResponse.class);
                } catch (IOException e) {
                    logger.error("SendMessage response error, failed to parse json: body = " + response.getResponseBody(), ", exception = " + e.getMessage());
                }
            }
            return sendMessageResponse;
        });
    }
}
