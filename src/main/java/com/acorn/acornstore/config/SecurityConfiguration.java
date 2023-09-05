package com.acorn.acornstore.config;

import com.acorn.acornstore.util.JwtAuthenticationFilter;
import com.acorn.acornstore.util.OAuthSuccess;
import com.acorn.acornstore.service.OAuthUserService;
import com.acorn.acornstore.util.RedirectUrlCookieFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuthUserService oAuthUser;
    private final OAuthSuccess oAuthSuccess;
    private final RedirectUrlCookieFilter redirectUrlFilter ;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//        http.headers().frameOptions().sameOrigin()
//                .and()
//                .formLogin().disable()
//                .csrf().disable()
//                .httpBasic().disable()
//                .rememberMe().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .cors()
//                .and()
//                .authorizeRequests()
//                .antMatchers("/resources/static/**", "/auth/**").permitAll()
//                .antMatchers("/signup").permitAll()  // add this line
//                .anyRequest().authenticated();


        http.headers().frameOptions().sameOrigin()
                .and()
                .cors()
                .and()
                .csrf()
                .disable()
                .httpBasic()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/", "/auth/**", "/oauth2/**").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .redirectionEndpoint()
                                .baseUri("/oauth2/callback/*")
                                .and()
                                .authorizationEndpoint()
                                .baseUri("/auth/authorize")
                                .and()
                                .userInfoEndpoint()
                                .userService(oAuthUser)
                                .and()
                                .successHandler(oAuthSuccess)
                )
                .exceptionHandling()
                .authenticationEntryPoint(new Http403ForbiddenEntryPoint());

        http.addFilterAfter(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );
        http.addFilterBefore(
                redirectUrlFilter,
                OAuth2AuthorizationRequestRedirectFilter.class
        );

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
