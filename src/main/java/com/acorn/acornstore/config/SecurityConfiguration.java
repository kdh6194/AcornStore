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
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuthUserService oAuthUser;
    private final OAuthSuccess oAuthSuccess;
    private final RedirectUrlCookieFilter redirectUrlFilter ;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

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
                                .antMatchers( "/api/**","/auth/**", "/oauth2/**").permitAll()
                                .anyRequest().authenticated());

//        http
//                .authorizeRequests()
//                .antMatchers("/resources/**", "/signup", "/about").permitAll()
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .anyRequest().authenticated()
//                .and()
//                // ...
//                .formLogin().loginPage("/login").permitAll()
//                .and()
//                // Here we are configuring Remember-Me feature.
//                .rememberMe().key("uniqueAndSecret")
//                // Persistent token based remember-me.
//                // It will create a new auto-generated repository using specified data source.
//                // If you want to use custom token repository then uncomment the below line.
//                //.tokenRepository(persistentTokenRepository())
//                .tokenValiditySeconds(2419200)  // Token is valid for 4 weeks.
//                .userDetailsService(userDetailsService);

        http.csrf().disable();
        http.headers().frameOptions().disable();

        // OAuth2 로그인 설정
        http.oauth2Login(oauth2Login ->
                        oauth2Login.redirectionEndpoint().baseUri("/oauth2/callback/*")
                                .and().authorizationEndpoint().baseUri("/auth/authorize")
                // UserService 및 SuccessHandler 설정 추가
                                .and().userInfoEndpoint().userService(oAuthUser)
                                .and().successHandler(oAuthSuccess));

        // Exception handling 추가
        http.exceptionHandling()
                .authenticationEntryPoint(new Http403ForbiddenEntryPoint());

        http.addFilterAfter(
                jwtAuthenticationFilter,
                CorsFilter.class
        );
        http.addFilterBefore(
                redirectUrlFilter,
                OAuth2AuthorizationRequestRedirectFilter.class
        );
    }
        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

//    @Bean
//    public PersistentTokenRepository persistentTokenRepository() {
//        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
//        db.setDataSource(db.getDataSource());
//        return db;
//    }
}

//http.headers().frameOptions().sameOrigin()
//        .and()
//        .cors()
//        .and()
//        .csrf()
//        .disable()
//        .httpBasic()
//        .disable()
//        .sessionManagement()
//        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//        .and()
//        .authorizeRequests(authorizeRequests ->
//        authorizeRequests
//        .antMatchers("/", "/auth/**", "/oauth2/**").permitAll()
//        .anyRequest().authenticated()
//        )
//        .oauth2Login(oauth2Login ->
//        oauth2Login
//        .redirectionEndpoint()
//        .baseUri("/oauth2/callback/*")
//        .and()
//        .authorizationEndpoint()
//        .baseUri("/auth/authorize")
//        .and()
//        .userInfoEndpoint()
//        .userService(oAuthUser)
//        .and()
//        .successHandler(oAuthSuccess)
//        )
//        .exceptionHandling()
//        .authenticationEntryPoint(new Http403ForbiddenEntryPoint());
//
//        http.addFilterAfter(
//        jwtAuthenticationFilter,
//        UsernamePasswordAuthenticationFilter.class
//        );
//                http.addFilterBefore(
//                redirectUrlFilter,
//                OAuth2AuthorizationRequestRedirectFilter.class
//        );
//
//                return http.build();