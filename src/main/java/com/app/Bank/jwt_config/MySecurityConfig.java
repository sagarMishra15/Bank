package com.app.Bank.jwt_config;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Component
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class MySecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public MySecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    private static final Logger logger = LoggerFactory.getLogger(MySecurityConfig.class);

    @Getter
    private static final List<String> noAuthPaths = Arrays.asList(
            "auth/register",
            "auth/authentication",
            "/swagger-ui/**",
            "/swagger-ui/index.html/",
            "/v3/api-docs/**"
    );

    public static String handleProUat(String reqUrl) {
        if (reqUrl.startsWith("/prod/")) {
            reqUrl = reqUrl.replaceFirst("prod/", "");
        } else if (reqUrl.startsWith("/uat/")) {
            reqUrl = reqUrl.replaceFirst("uat/", "");
        }
        return reqUrl;
    }

    public static boolean isNoAuthAllowed(String url) {
        for (String s : MySecurityConfig.getNoAuthPaths()) {
            if (s.endsWith("**")) {
                String base = s.replace("/**", "");
                if (url.startsWith(base)) {
                    return true;
                }
            } else {
                if (url.equals(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        logger.info("MySecurityConfig : filterChain");

        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(Arrays.asList("http://localhost:8080")); // Allow your frontend origin
            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
            config.setAllowedHeaders(Arrays.asList("*"));
            config.setAllowCredentials(true);
            return config;
        }))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> {
                    authz.requestMatchers("/swagger-ui/index.html").permitAll();
                    authz.requestMatchers("/swagger-ui/**").permitAll();
                    authz.requestMatchers("/v3/api-docs/**").permitAll();
                    authz.requestMatchers("/auth/register");
                    authz.requestMatchers("/auth/authentication");

                    // Iterate over noAuthPaths to permit them
                    noAuthPaths.stream()
                            .filter(path -> path != null && !path.isEmpty())
                            .forEach(path -> authz.requestMatchers(path).permitAll());
                    authz.anyRequest().authenticated();
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                );
//        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.getOrBuild();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}