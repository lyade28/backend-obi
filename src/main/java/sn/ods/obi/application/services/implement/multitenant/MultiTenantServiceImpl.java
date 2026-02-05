package sn.ods.obi.application.services.implement.multitenant;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ods.obi.domain.model.multitenant.*;
import sn.ods.obi.domain.model.utilisateur.Profile;
import sn.ods.obi.domain.model.utilisateur.Utilisateur;
import sn.ods.obi.domain.repository.multitenant.*;
import sn.ods.obi.domain.repository.IProfilRepository;
import sn.ods.obi.domain.repository.utilisateur.UtilisateurRepository;
import sn.ods.obi.infrastructure.config.password.PasswordGenerator;
import sn.ods.obi.presentation.dto.multitenant.*;
import sn.ods.obi.application.services.interfaces.multitenant.MultiTenantService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MultiTenantServiceImpl implements MultiTenantService {

    private static final Map<String, String> ROLE_DROITS = Map.of(
            "ADMIN_TENANT", "Gestion complète organisation",
            "ANALYSTE", "Rapports, modélisation, connexions",
            "LECTEUR", "Consultation rapports et dashboards"
    );

    private final OrganisationRepository organisationRepository;
    private final TenantRepository tenantRepository;
    private final AuditLogRepository auditLogRepository;
    private final TenantParametrageRepository parametrageRepository;
    private final QuotaRepository quotaRepository;
    private final IProfilRepository profilRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<OrganisationDTO> getOrganisations(Long tenantId) {
        return organisationRepository.findByTenantId(tenantId).stream()
                .map(this::toOrgDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrganisationDTO> getOrganisation(Long id) {
        return organisationRepository.findById(id).map(this::toOrgDTO);
    }

    @Override
    public List<UtilisateurListDTO> getUtilisateurs(Long tenantId) {
        List<Long> orgIds = organisationRepository.findByTenantId(tenantId).stream()
                .map(Organisation::getId)
                .collect(Collectors.toList());
        if (orgIds.isEmpty()) return List.of();
        List<Utilisateur> users = utilisateurRepository.findByOrganisationIdIn(orgIds);
        Map<Long, String> orgNames = organisationRepository.findAllById(orgIds).stream()
                .collect(Collectors.toMap(Organisation::getId, o -> o.getNom() != null ? o.getNom() : ""));
        return users.stream()
                .map(u -> UtilisateurListDTO.builder()
                        .id(u.getId())
                        .nom(u.getPrenom() != null && u.getNom() != null ? u.getPrenom() + " " + u.getNom() : (u.getNom() != null ? u.getNom() : (u.getPrenom() != null ? u.getPrenom() : "")))
                        .email(u.getEmail())
                        .role(u.getProfiles() != null && !u.getProfiles().isEmpty()
                                ? u.getProfiles().iterator().next().getCode()
                                : "")
                        .organisationNom(u.getOrganisationId() != null ? orgNames.getOrDefault(u.getOrganisationId(), "") : "")
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UtilisateurListDTO createUtilisateur(Long tenantId, UtilisateurCreateUpdateDTO dto) {
        if (dto.getEmail() == null || dto.getEmail().isBlank()) throw new IllegalArgumentException("Email requis");
        if (utilisateurRepository.findUtilisateurByEmail(dto.getEmail().trim()).isPresent())
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        Set<Profile> profiles = resolveProfileByCodeOrLabel(dto.getRole()).map(Set::of).orElse(Set.of());
        String nom = dto.getNom() != null ? dto.getNom().trim() : "";
        Utilisateur u = Utilisateur.builder()
                .nom(nom)
                .prenom(null)
                .email(dto.getEmail().trim())
                .organisationId(dto.getOrganisationId())
                .password(passwordEncoder.encode(PasswordGenerator.generateRandomString()))
                .status(true)
                .firstLog(true)
                .profiles(profiles)
                .build();
        Utilisateur savedUser = utilisateurRepository.save(u);
        String orgName = dto.getOrganisationId() != null && organisationRepository.findById(dto.getOrganisationId()).isPresent()
                ? organisationRepository.findById(dto.getOrganisationId()).get().getNom() : "";
        String roleCode = profiles.isEmpty() ? "" : profiles.iterator().next().getCode();
        return UtilisateurListDTO.builder()
                .id(savedUser.getId())
                .nom(nom)
                .email(savedUser.getEmail())
                .role(roleCode)
                .organisationNom(orgName)
                .build();
    }

    @Override
    @Transactional
    public UtilisateurListDTO updateUtilisateur(Long id, UtilisateurCreateUpdateDTO dto) {
        Utilisateur u = utilisateurRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        if (dto.getNom() != null) u.setNom(dto.getNom().trim());
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) u.setEmail(dto.getEmail().trim());
        if (dto.getOrganisationId() != null) u.setOrganisationId(dto.getOrganisationId());
        if (dto.getRole() != null && !dto.getRole().isBlank()) {
            resolveProfileByCodeOrLabel(dto.getRole()).ifPresent(p -> u.setProfiles(Set.of(p)));
        }
        Utilisateur saved = utilisateurRepository.save(u);
        String orgName = saved.getOrganisationId() != null && organisationRepository.findById(saved.getOrganisationId()).isPresent()
                ? organisationRepository.findById(saved.getOrganisationId()).get().getNom() : "";
        return UtilisateurListDTO.builder()
                .id(saved.getId())
                .nom(saved.getNom())
                .email(saved.getEmail())
                .role(saved.getProfiles() != null && !saved.getProfiles().isEmpty() ? saved.getProfiles().iterator().next().getCode() : "")
                .organisationNom(orgName)
                .build();
    }

    @Override
    @Transactional
    public void deleteUtilisateur(Long id) {
        if (!utilisateurRepository.existsById(id)) throw new IllegalArgumentException("Utilisateur non trouvé");
        utilisateurRepository.deleteById(id);
    }

    @Override
    @Transactional
    public OrganisationDTO createOrganisation(Long tenantId, OrganisationDTO dto) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant non trouvé"));
        Organisation org = Organisation.builder()
                .nom(dto.getNom())
                .type(dto.getType() != null ? dto.getType() : "Siège")
                .statut(dto.getStatut() != null ? dto.getStatut() : "Actif")
                .tenant(tenant)
                .build();
        org = organisationRepository.save(org);
        return toOrgDTO(org);
    }

    @Override
    @Transactional
    public OrganisationDTO updateOrganisation(Long id, OrganisationDTO dto) {
        Organisation org = organisationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Organisation non trouvée"));
        if (dto.getNom() != null) org.setNom(dto.getNom());
        if (dto.getType() != null) org.setType(dto.getType());
        if (dto.getStatut() != null) org.setStatut(dto.getStatut());
        org = organisationRepository.save(org);
        return toOrgDTO(org);
    }

    @Override
    @Transactional
    public void deleteOrganisation(Long id) {
        organisationRepository.deleteById(id);
    }

    @Override
    public List<TenantDTO> getTenants() {
        return tenantRepository.findAll().stream()
                .map(this::toTenantDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TenantDTO createTenant(TenantDTO dto) {
        Tenant t = Tenant.builder()
                .nom(dto.getNom())
                .statut(dto.getStatut() != null ? dto.getStatut() : "Actif")
                .build();
        t = tenantRepository.save(t);
        return toTenantDTO(t);
    }

    @Override
    @Transactional
    public TenantDTO updateTenant(Long id, TenantDTO dto) {
        Tenant t = tenantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tenant non trouvé"));
        if (dto.getNom() != null) t.setNom(dto.getNom());
        if (dto.getStatut() != null) t.setStatut(dto.getStatut());
        t = tenantRepository.save(t);
        return toTenantDTO(t);
    }

    @Override
    @Transactional
    public void deleteTenant(Long id) {
        tenantRepository.deleteById(id);
    }

    @Override
    public Page<AuditLogDTO> getAuditLogs(Long tenantId, Long organisationId, Pageable pageable) {
        Page<AuditLog> page = organisationId != null
                ? auditLogRepository.findByOrganisationIdOrderByDateDesc(organisationId, pageable)
                : auditLogRepository.findByTenantIdOrderByDateDesc(tenantId, pageable);
        return page.map(a -> AuditLogDTO.builder()
                .id(a.getId())
                .date(a.getDate())
                .action(a.getAction())
                .utilisateur(a.getUtilisateur())
                .detail(a.getDetail())
                .build());
    }

    @Override
    @Transactional
    public void logAudit(String action, String utilisateur, String detail, Long orgId, Long tenantId) {
        AuditLog log = AuditLog.builder()
                .date(LocalDateTime.now())
                .action(action)
                .utilisateur(utilisateur)
                .detail(detail)
                .organisationId(orgId)
                .tenantId(tenantId)
                .build();
        auditLogRepository.save(log);
    }

    @Override
    public ParametrageDTO getParametrage(Long tenantId) {
        return parametrageRepository.findById(tenantId)
                .map(p -> ParametrageDTO.builder()
                        .theme(p.getTheme())
                        .fuseau(p.getFuseau())
                        .devise(p.getDevise())
                        .logo(p.getLogo())
                        .build())
                .orElse(ParametrageDTO.builder().theme("Défaut").fuseau("Europe/Paris").devise("EUR").build());
    }

    @Override
    @Transactional
    public ParametrageDTO updateParametrage(Long tenantId, ParametrageDTO dto) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant non trouvé"));
        TenantParametrage p = parametrageRepository.findById(tenantId).orElse(null);
        if (p == null) {
            p = new TenantParametrage();
            p.setTenantId(tenantId);
            p.setTenant(tenant);
            p.setTheme("Défaut");
            p.setFuseau("Europe/Paris");
            p.setDevise("EUR");
        }
        if (dto.getTheme() != null) p.setTheme(dto.getTheme());
        if (dto.getFuseau() != null) p.setFuseau(dto.getFuseau());
        if (dto.getDevise() != null) p.setDevise(dto.getDevise());
        if (dto.getLogo() != null) p.setLogo(dto.getLogo());
        p = parametrageRepository.save(p);
        return ParametrageDTO.builder().theme(p.getTheme()).fuseau(p.getFuseau()).devise(p.getDevise()).logo(p.getLogo()).build();
    }

    @Override
    public QuotaDTO getQuotas(Long tenantId) {
        return quotaRepository.findById(tenantId)
                .map(q -> QuotaDTO.builder()
                        .stockage(formatQuota(q.getStockageUtilise(), q.getStockageMax()))
                        .connexions(q.getConnexionsUtilise() + " / " + q.getConnexionsMax())
                        .utilisateurs(q.getUtilisateursUtilise() + " / " + q.getUtilisateursMax())
                        .build())
                .orElse(QuotaDTO.builder()
                        .stockage("0 / 10 Go")
                        .connexions("0 / 5")
                        .utilisateurs("0 / 50")
                        .build());
    }

    @Override
    public List<RoleDTO> getRoles() {
        return profilRepository.findAll().stream()
                .map(p -> RoleDTO.builder()
                        .nom(p.getLabel() != null ? p.getLabel() : p.getCode())
                        .droits(ROLE_DROITS.getOrDefault(p.getCode(), "—"))
                        .build())
                .collect(Collectors.toList());
    }

    /** Résout un profil par code (ex. ANALYSTE) ou par libellé (ex. Analyste). */
    private Optional<Profile> resolveProfileByCodeOrLabel(String role) {
        if (role == null || role.isBlank()) return Optional.empty();
        String r = role.trim();
        return profilRepository.findByCode(r)
                .or(() -> profilRepository.findAll().stream()
                        .filter(p -> r.equalsIgnoreCase(p.getLabel()))
                        .findFirst());
    }

    private OrganisationDTO toOrgDTO(Organisation org) {
        int count = (int) utilisateurRepository.countByOrganisationId(org.getId());
        return OrganisationDTO.builder()
                .id(org.getId())
                .nom(org.getNom())
                .type(org.getType())
                .statut(org.getStatut())
                .tenantId(org.getTenant().getId())
                .utilisateurs(count)
                .build();
    }

    private TenantDTO toTenantDTO(Tenant t) {
        int orgs = organisationRepository.findByTenantId(t.getId()).size();
        int users = organisationRepository.findByTenantId(t.getId()).stream()
                .mapToInt(o -> (int) utilisateurRepository.countByOrganisationId(o.getId()))
                .sum();
        return TenantDTO.builder()
                .id(t.getId())
                .nom(t.getNom())
                .statut(t.getStatut())
                .orgs(orgs)
                .users(users)
                .build();
    }

    private String formatQuota(long used, long max) {
        long usedGo = used / (1024 * 1024 * 1024);
        long maxGo = max / (1024 * 1024 * 1024);
        return usedGo + " Go / " + maxGo + " Go";
    }
}
