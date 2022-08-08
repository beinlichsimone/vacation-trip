package io.github.beinlichsimone.vacationtrip.security;

import io.github.beinlichsimone.vacationtrip.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@Profile("dev") // irá considerar essa SecurityConfiguration nos perfis de DEV. Isso é usado para criar diferentes ambiente da aplicação e neste caso iremos usar para desabilitar o esquema de segurança em ambiente de dev
// o perfil dev é configurado no IJ na configuração do projeto em VM. (vide imagem na pasta do projeto).
public class DevSecurityConfiguration extends WebSecurityConfigurerAdapter {

    //configurações de autorização
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/**").permitAll() //tudo o que vier depois de /viagem com o tipo GET deverá liberar sem autenticar
                //.anyRequest().authenticated() //todo o resto será bloqueado
                .and().csrf().disable();
    }
}
