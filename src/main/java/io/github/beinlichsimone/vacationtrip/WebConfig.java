package io.github.beinlichsimone.vacationtrip;

import io.github.beinlichsimone.vacationtrip.interceptor.InterceptadorDeAcessos;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    protected void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new InterceptadorDeAcessos()).addPathPatterns("/**");
    }
}
