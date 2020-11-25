package com.viettel.luongnk.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author: luongnk
 * @since: 25/11/2020
 */

@Configuration
@EnableWebSecurity
public class ApiSecurity extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().anyRequest().fullyAuthenticated().and()
                .httpBasic();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationMgr)
        throws Exception {
        authenticationMgr.inMemoryAuthentication().withUser("admin")
                .password("$2y$12$CbjQNyOYTlEblv9x4N36cuJIcFOR7F3DAkgQx6ys5PglgSvqmibFa").roles("ADMIN");
    }
}
