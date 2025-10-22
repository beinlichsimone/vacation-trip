package io.github.beinlichsimone.vacationtrip;

import io.github.beinlichsimone.vacationtrip.interceptor.InterceptadorDeAcessos;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    protected void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new InterceptadorDeAcessos()).addPathPatterns("/**");
    }

    //classe chamada WebConfig que acaba sobrescrevendo um compartamento do Spring. Sem isto não estava funcionando a chamada do controller (viagemcontroller) usando o obj paginação
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add( new PageableHandlerMethodArgumentResolver());
    }
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        // Libera o frontend em localhost:3000 para chamadas ao backend durante o desenvolvimento
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false);
    }

}
