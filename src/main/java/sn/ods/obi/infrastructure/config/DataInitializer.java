package sn.ods.obi.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sn.ods.obi.domain.model.multitenant.Organisation;
import sn.ods.obi.domain.model.multitenant.Tenant;
import sn.ods.obi.domain.model.utilisateur.Profile;
import sn.ods.obi.domain.model.utilisateur.Utilisateur;
import sn.ods.obi.domain.repository.IProfilRepository;
import sn.ods.obi.domain.repository.multitenant.OrganisationRepository;
import sn.ods.obi.domain.repository.multitenant.TenantRepository;
import sn.ods.obi.domain.repository.utilisateur.UtilisateurRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final String ADMIN_EMAIL = "ediop@ods.sn";
    private static final String ADMIN_PASSWORD = "231091Mar*";

    private final TenantRepository tenantRepository;
    private final OrganisationRepository organisationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final IProfilRepository profilRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (tenantRepository.count() == 0) {
            Tenant t = Tenant.builder().nom("Tenant par défaut").statut("Actif").build();
            t = tenantRepository.save(t);
            Organisation o = Organisation.builder().nom("Organisation principale").type("Siège").statut("Actif").tenant(t).build();
            organisationRepository.save(o);
        }

        Profile adminProfile = profilRepository.findByCode("ADMIN").orElseGet(() -> {
            Profile p = Profile.builder().code("ADMIN").label("Administrateur").build();
            return profilRepository.save(p);
        });

        if (utilisateurRepository.findUtilisateurByEmail(ADMIN_EMAIL).isEmpty()) {
            Set<Profile> profiles = new HashSet<>();
            profiles.add(adminProfile);
            Long orgId = null;
            var tenants = tenantRepository.findAll();
            if (!tenants.isEmpty()) {
                var orgs = organisationRepository.findByTenantId(tenants.get(0).getId());
                if (!orgs.isEmpty()) {
                    orgId = orgs.get(0).getId();
                }
            }
            Utilisateur admin = Utilisateur.builder()
                    .email(ADMIN_EMAIL)
                    .nom("Admin")
                    .prenom("OBI")
                    .password(passwordEncoder.encode(ADMIN_PASSWORD))
                    .status(true)
                    .firstLog(false)
                    .profiles(profiles)
                    .organisationId(orgId)
                    .build();
            utilisateurRepository.save(admin);
        }
    }
}
