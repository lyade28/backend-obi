
package sn.ods.obi.infrastructure.config.security.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sn.ods.obi.domain.model.utilisateur.Utilisateur;
import sn.ods.obi.domain.repository.utilisateur.UtilisateurRepository;
import sn.ods.obi.infrastructure.config.utils.i18n.I18nTranslate;

import static sn.ods.obi.infrastructure.config.utils.i18n.I18nKeys.CONNEXION_LOGIN_TENTATIVE;
import static sn.ods.obi.infrastructure.config.utils.i18n.I18nKeys.UTILISATEUR_ABSENT;


@Service
@RequiredArgsConstructor
public class UtilisateurDetailsSerciveImpl implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;
    private final LoginAttemptService loginAttemptService;
    private final I18nTranslate i18nTranslat;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (loginAttemptService.isBlocked(username)) {
            throw new InternalAuthenticationServiceException(i18nTranslat.toTranslate(CONNEXION_LOGIN_TENTATIVE));
        }
        Utilisateur utilisateur = utilisateurRepository.findUtilisateurByEmail(username).orElseThrow((() -> new UsernameNotFoundException(i18nTranslat.toTranslate(UTILISATEUR_ABSENT) + " : " + username)));
        return UtilisateurPrinciple.build(utilisateur);
    }

}
