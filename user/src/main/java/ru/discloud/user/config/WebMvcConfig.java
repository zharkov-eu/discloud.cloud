package ru.discloud.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.discloud.user.interceptor.TokenAuthInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final TokenAuthInterceptor tokenAuthenticationInterceptor;

    @Autowired
    public WebMvcConfig(TokenAuthInterceptor tokenAuthenticationInterceptor) {
        this.tokenAuthenticationInterceptor = tokenAuthenticationInterceptor;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenAuthenticationInterceptor);
    }
}
