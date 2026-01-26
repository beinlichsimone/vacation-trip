package io.github.beinlichsimone.vacationtrip.config.security;

import io.github.beinlichsimone.vacationtrip.repository.UserRepository;
import io.github.beinlichsimone.vacationtrip.config.tenancy.TenantRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@EnableWebSecurity
@Configuration
@Profile("prod") // irá considerar essa SecurityConfiguration nos perfis de produção. Isso é usado para criar diferentes ambiente da aplicação e neste caso iremos usar para habilitar apenas em ambiente de produção
public class SecurityConfiguration {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private TenantRequestFilter tenantRequestFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //configurações de autorização (SecurityFilterChain)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .accessDeniedHandler(new RestAccessDeniedHandler())
                .and()
                .authorizeRequests()
                //.antMatchers(HttpMethod.GET, "/viagem/*").permitAll() //tudo o que vier depois de /viagem com o tipo GET deverá liberar sem autenticar
                .antMatchers(HttpMethod.DELETE, "/viagem/*").hasAuthority("MODERADOR") // permite deletar apenas o usuário com perfil de MODERADOR
                .antMatchers("/auth/**").permitAll()
                //.antMatchers("/pessoa/**", "/passeio/**", "/documento/**", "/deslocamento/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                //.antMatchers(HttpMethod.GET, "/actuator/**").permitAll() //precisa desabilitar quando estiver em produção. Ele retorna informações sobre o funcionamento da API
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated();                                   //todo o resto será bloqueado

        // Garante que o tenant seja resolvido antes da autenticação
        http.addFilterBefore(tenantRequestFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    //configurações de recursos estáticos(js, css, imagens, etc.) via WebSecurityCustomizer
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(
                "/**.html",
                "/v2/api-docs",
                "/webjars/**",
                "/configuration/**",
                "/swagger-resources/**",
                "/auth/**" // ignora completamente autenticação e registro
        ); // para o swagger
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
