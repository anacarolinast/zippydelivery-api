package br.com.zippydeliveryapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/login").permitAll()

                    .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/cliente").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/empresa").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/produto").permitAll()
                    .requestMatchers(HttpMethod.PUT, "/api/produto/*").permitAll()
                    .requestMatchers(HttpMethod.DELETE, "/api/produto/*").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/produto/").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/empresa").permitAll()
                    .requestMatchers(HttpMethod.PUT, "/api/empresa/*").permitAll()
                    .requestMatchers(HttpMethod.DELETE, "/api/empresa/*").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/categoria-produto").permitAll()
                    .requestMatchers(HttpMethod.PUT, "/api/categoria-produto/*").permitAll()
                    .requestMatchers(HttpMethod.DELETE, "/api/categoria-produto/*").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/categoria-produto/").permitAll()
                    .requestMatchers(HttpMethod.PUT, "/api/cliente/*").permitAll()
                    .requestMatchers(HttpMethod.DELETE, "/api/cliente/*").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/cliente/").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/pedido").permitAll()
                    .requestMatchers(HttpMethod.PUT, "/api/pedido/*").permitAll()
                    .requestMatchers(HttpMethod.DELETE, "/api/pedido/*").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/pedido/").permitAll()

                    .requestMatchers("/api/cliente").permitAll()
                    .requestMatchers("/api/**").authenticated()
                    .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

