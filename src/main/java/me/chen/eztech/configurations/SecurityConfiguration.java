package me.chen.eztech.configurations;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .requestMatchers(EndpointRequest.to(InfoEndpoint.class, HealthEndpoint.class)).permitAll()
//                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
//                .antMatchers("/*-api/**").hasRole("REST")
//                .antMatchers("/dashboard").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and()
//                .csrf().disable()
//                .httpBasic();

        http
                .authorizeRequests()
                    .antMatchers("/dashboard").hasRole("REST")
                .and()
                    .csrf().disable()
                .httpBasic()
                .and()
                    .rememberMe().key("eztech-rememberme-session-key").tokenValiditySeconds(36000);
    }

}
