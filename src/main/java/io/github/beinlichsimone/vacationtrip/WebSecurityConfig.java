package io.github.beinlichsimone.vacationtrip;

//@Configuration
//@EnableWebSecurity
public class WebSecurityConfig {//extends WebSecurityConfigurerAdapter {
    //Comentado pois está sobreposto em DevSecurityConfiguration. Não lembro mais qual a diferença entre os dois
    /*
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests()
                .antMatchers("/home/**")
                    .permitAll()
                .anyRequest()
                    .authenticated()
                .and()
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll())
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/home"));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(encoder);

//        UserDetails user =
//                User.builder().
//                        username("simone").
//                        password("simone").
//                        roles("ADM").
//                        build();


    }*/

}
