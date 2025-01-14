package com.example.UserService.config;
import com.example.UserService.Service.CustomSuccessAuthHandler;
import com.example.UserService.filter.AuthorLogginAfterFilter;
import com.example.UserService.filter.CsrfCookieFilter;
import com.example.UserService.filter.JWTValidatorFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    CustomSuccessAuthHandler customSuccessAuthHandler;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler handler = new CsrfTokenRequestAttributeHandler();
        http
//                .securityContext(context -> context
//                        // tell spring framework to generate jsessionid and store auth detail to the security context holder
//                        .requireExplicitSave(false))
//                .sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                // tell spring framework not to generate jsessionid
                .sessionManagement(session ->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .cors(cors ->cors.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowCredentials(true);
                        config.setExposedHeaders(Arrays.asList("Authorization"));
                        return config;
                    }
                }))
                .csrf(csrf -> csrf
                        .csrfTokenRequestHandler(handler)
                        .ignoringRequestMatchers("/register","/login")
                        // tell spring framework to create csrf cookie,storing it in the memory of the apps
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                //this line says please execute csrf filter after basic authen filter
                // after login complete, csrf token will be generated,persist in response
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new AuthorLogginAfterFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTValidatorFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/user").hasAuthority("teacher")
                        .requestMatchers("/student").hasAuthority("student")
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable);
//                .formLogin(form ->form.successHandler(customSuccessAuthHandler).permitAll());
        return http.build();
    }
    @Bean
    public  PasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder();
    }
}
