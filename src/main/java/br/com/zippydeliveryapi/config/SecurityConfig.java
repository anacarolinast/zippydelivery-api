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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    @Autowired
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8081", "exp://192.168.0.153:8081")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/cliente").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/cliente/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/cliente/").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/cliente/*").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/empresa").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/empresa/*").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/empresa/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/empresa").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/produto").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/produto/*").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/produto/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/produto/").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/categoria-produto").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/categoria-produto/*").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/categoria-produto/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categoria-produto/").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/categoria-empresa").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/categoria-empresa/*").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/categoria-empresa/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categoria-empresa").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/pedido").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/pedido/*").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/pedido/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/pedido/").permitAll()
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

