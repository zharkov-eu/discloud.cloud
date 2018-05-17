package ru.discloud.user.config;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.discloud.user.interceptor.TokenAuthInterceptor;

public class WebMvcConfig implements WebMvcConfigurer {

    private final TokenAuthInterceptor tokenAuthenticationInterceptor;

    public WebMvcConfig(TokenAuthInterceptor tokenAuthenticationInterceptor) {
        this.tokenAuthenticationInterceptor = tokenAuthenticationInterceptor;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenAuthenticationInterceptor);
    }
}
