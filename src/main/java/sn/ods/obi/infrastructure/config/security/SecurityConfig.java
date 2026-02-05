package sn.ods.obi.infrastructure.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import sn.ods.obi.infrastructure.config.security.jwt.JwtAuthEntryPoint;
import sn.ods.obi.infrastructure.config.security.jwt.JwtAuthTokenFilter;


import java.util.List;

/**
 * Spring Security configuration class for JWT-based authentication and CORS setup.
 *
 * @author G2k R&D
 */
@Configuration
@EnableWebSecurity
@EnableTransactionManagement
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthTokenFilter jwtAuthTokenFilter;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    // Whitelisted endpoints that do not require authentication
    private static final RequestMatcher[] AUTH_WHITELIST = {
            new AntPathRequestMatcher("/auth/**"),
            new AntPathRequestMatcher("/utilisateur/user/add-user/**"),
            new AntPathRequestMatcher("/utilisateur/activation/**"),
           new AntPathRequestMatcher("/errors/**"),
            new AntPathRequestMatcher("/logs/**"),
            new AntPathRequestMatcher("/swagger-ui.html"),
            new AntPathRequestMatcher("/swagger-ui/**"),
            new AntPathRequestMatcher("/v3/api-docs/**"),
            new AntPathRequestMatcher("/WEB-INF/classes/images/**"),
            new AntPathRequestMatcher("/static/**")
    };

    /**
     * Configures the security filter chain.
     *
     * @param httpSecurity the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
                .cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource())) // Enable CORS
                .authorizeHttpRequests(request -> request
                        .requestMatchers(AUTH_WHITELIST).permitAll() // Allow access to whitelisted endpoints
                        .anyRequest().authenticated()) // Require authentication for all other endpoints
                .sessionManagement(manager -> manager
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Use stateless sessions
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthEntryPoint)) // Handle unauthorized requests
                .addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return httpSecurity.build();
    }

    /**
     * Configures CORS (Cross-Origin Resource Sharing) settings.
     *
     * @return the CorsConfigurationSource object
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // Origines autorisées (localhost et 127.0.0.1 = origines différentes en CORS)
        corsConfiguration.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                "http://127.0.0.1:*"
        ));

        // Méthodes et en-têtes (explicites pour éviter les rejets avec credentials)
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of(
                "Origin", "Content-Type", "Accept", "Authorization",
                "X-Requested-With", "Accept-Language"
        ));

        // Credentials pour envoi du JWT
        corsConfiguration.setAllowCredentials(true);

        // Set the maximum age of the CORS preflight request cache
        corsConfiguration.setMaxAge(3600L);

        // Register CORS configuration for all paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}