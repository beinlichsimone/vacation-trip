package io.github.beinlichsimone.vacationtrip.config.security;

import io.github.beinlichsimone.vacationtrip.config.tenancy.TenantRequestFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@Profile("dev") // irá considerar essa SecurityConfiguration nos perfis de DEV. Isso é usado para criar diferentes ambiente da aplicação e neste caso iremos usar para desabilitar o esquema de segurança em ambiente de dev
// o perfil dev é configurado no IJ na configuração do projeto em VM. (vide imagem na pasta do projeto).
public class DevSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private TenantRequestFilter tenantRequestFilter;

    //configurações de autorização
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .cors() // habilita CORS integrado ao Spring MVC/WebConfig
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // libera preflight
                .antMatchers(HttpMethod.GET, "/**").permitAll() // GET liberado em dev
                .antMatchers(HttpMethod.POST, "/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/**").permitAll()
                .and()
                .csrf().disable();

        // Mesmo em dev, resolvemos o tenant para que Hibernate use o schema correto
        http.addFilterBefore(tenantRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
