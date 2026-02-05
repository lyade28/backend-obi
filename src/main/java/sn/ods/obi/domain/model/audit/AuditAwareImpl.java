package sn.ods.obi.domain.model.audit;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import sn.ods.obi.infrastructure.config.security.services.UtilisateurPrinciple;


import java.util.Optional;

@Component
public class AuditAwareImpl implements AuditorAware<Long> {

 @Override
    public @NotNull Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UtilisateurPrinciple userPrincipal) {
            return Optional.of(userPrincipal.getUtilisateurInfo().id());
        }
        return Optional.empty();
    }
}
