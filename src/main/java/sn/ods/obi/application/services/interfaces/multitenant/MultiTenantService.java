package sn.ods.obi.application.services.interfaces.multitenant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ods.obi.presentation.dto.multitenant.*;

import java.util.List;

public interface MultiTenantService {
    List<OrganisationDTO> getOrganisations(Long tenantId);
    java.util.Optional<OrganisationDTO> getOrganisation(Long id);
    java.util.List<sn.ods.obi.presentation.dto.multitenant.UtilisateurListDTO> getUtilisateurs(Long tenantId);
    sn.ods.obi.presentation.dto.multitenant.UtilisateurListDTO createUtilisateur(Long tenantId, sn.ods.obi.presentation.dto.multitenant.UtilisateurCreateUpdateDTO dto);
    sn.ods.obi.presentation.dto.multitenant.UtilisateurListDTO updateUtilisateur(Long id, sn.ods.obi.presentation.dto.multitenant.UtilisateurCreateUpdateDTO dto);
    void deleteUtilisateur(Long id);
    OrganisationDTO createOrganisation(Long tenantId, OrganisationDTO dto);
    OrganisationDTO updateOrganisation(Long id, OrganisationDTO dto);
    void deleteOrganisation(Long id);

    List<TenantDTO> getTenants();
    TenantDTO createTenant(TenantDTO dto);
    TenantDTO updateTenant(Long id, TenantDTO dto);
    void deleteTenant(Long id);

    Page<AuditLogDTO> getAuditLogs(Long tenantId, Long organisationId, Pageable pageable);
    void logAudit(String action, String utilisateur, String detail, Long orgId, Long tenantId);

    ParametrageDTO getParametrage(Long tenantId);
    ParametrageDTO updateParametrage(Long tenantId, ParametrageDTO dto);

    QuotaDTO getQuotas(Long tenantId);

    List<RoleDTO> getRoles();
}
