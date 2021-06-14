package com.codesoom.demo.config;

import com.codesoom.demo.filters.JwtAuthTenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.Filter;

// WebSecurityConfigurer<WebSecurity>
@Configuration
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        Filter authenticationFilter = new JwtAuthTenticationFilter(authenticationManager());
        http
                .csrf().disable()
                .addFilter(authenticationFilter);
        //super.configure(http);
    }
}
