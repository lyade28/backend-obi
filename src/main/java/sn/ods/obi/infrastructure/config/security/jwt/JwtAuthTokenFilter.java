package sn.ods.obi.infrastructure.config.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import sn.ods.obi.infrastructure.config.security.services.UtilisateurDetailsSerciveImpl;
import sn.ods.obi.infrastructure.config.utils.AuthUtils;


import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    private final JwtProvider tokenProvider;
    private final UtilisateurDetailsSerciveImpl utilisateurDetailsSercive;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = AuthUtils.getJwt(request);

            if (jwt != null && tokenProvider.validationJwtToken(jwt)) {

                String userName = tokenProvider.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = utilisateurDetailsSercive.loadUserByUsername(userName);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }
}
