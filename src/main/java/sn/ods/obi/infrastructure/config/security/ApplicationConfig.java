package sn.ods.obi.infrastructure.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;


import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserDetailsService userDetailsService;

    /**
     * Configures the authentication provider.
     *
     * @return the configured AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Set the UserDetailsService
        authProvider.setPasswordEncoder(passwordEncoder()); // Set the password encoder
        return authProvider;
    }

    /**
     * Configures the password encoder.
     *
     * @return the configured PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Use BCrypt for password hashing
    }

    /**
     * Configures the authentication manager.
     *
     * @param config the AuthenticationConfiguration
     * @return the configured AuthenticationManager
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configures the CORS filter.
     *
     * @return the configured CorsFilter
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow credentials (e.g., cookies)
        config.setAllowCredentials(true);

        // Patterns pour accepter localhost sur tout port (dev)
        config.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:*",
                "http://127.0.0.1:*"
        ));

        // Headers requis (Content-Type pour multipart, Authorization pour JWT)
        config.setAllowedHeaders(Arrays.asList(
                ORIGIN,
                CONTENT_TYPE,
                ACCEPT,
                AUTHORIZATION,
                "X-Requested-With",
                "Accept-Language"
        ));

        // OPTIONS requis pour preflight CORS
        config.setAllowedMethods(Arrays.asList(
                GET.name(),
                POST.name(),
                PUT.name(),
                DELETE.name(),
                PATCH.name(),
                "OPTIONS"
        ));

        // Register CORS configuration for all paths
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}