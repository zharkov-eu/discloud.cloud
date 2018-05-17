package ru.discloud.user.integration.yandex.kassa;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Dsl;

public class YandexPayment {
    private static String BASE_URL = "https://payment.yandex.net";
    private static String BASE_PATH = "/api/v3/";
    private static Boolean DEFAULT_DEBUG = false;
    private static Integer DEFAULT_TIMEOUT = 120000;

    private Integer shopId;
    private String secretKey;
    private AsyncHttpClient httpClient;

    public YandexPayment(Integer shopId, String secretKey) {
        this.shopId = shopId;
        this.secretKey = secretKey;
        DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
                .setConnectTimeout(DEFAULT_TIMEOUT);
        this.httpClient = Dsl.asyncHttpClient(clientBuilder);
    }
}
