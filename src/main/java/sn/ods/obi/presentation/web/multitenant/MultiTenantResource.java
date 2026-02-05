package sn.ods.obi.presentation.web.multitenant;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ods.obi.application.services.interfaces.multitenant.MultiTenantService;
import sn.ods.obi.presentation.dto.responses.APIResponse;
import sn.ods.obi.presentation.dto.multitenant.*;

import java.util.List;

@RestController
@RequestMapping("/organisations")
@RequiredArgsConstructor
public class MultiTenantResource {

    private final MultiTenantService multiTenantService;

    @GetMapping("/tenants")
    @Operation(summary = "Liste des tenants (admin)")
    public ResponseEntity<APIResponse> getTenants() {
        List<TenantDTO> list = multiTenantService.getTenants();
        return ResponseEntity.ok(APIResponse.success(list));
    }

    @PostMapping("/tenants")
    @Operation(summary = "Créer un tenant")
    public ResponseEntity<APIResponse> createTenant(@RequestBody TenantDTO dto) {
        TenantDTO created = multiTenantService.createTenant(dto);
        return ResponseEntity.ok(APIResponse.success(created));
    }

    @PutMapping("/tenants/{id:\\d+}")
    @Operation(summary = "Modifier un tenant")
    public ResponseEntity<APIResponse> updateTenant(@PathVariable Long id, @RequestBody TenantDTO dto) {
        TenantDTO updated = multiTenantService.updateTenant(id, dto);
        return ResponseEntity.ok(APIResponse.success(updated));
    }

    @DeleteMapping("/tenants/{id:\\d+}")
    @Operation(summary = "Supprimer un tenant")
    public ResponseEntity<APIResponse> deleteTenant(@PathVariable Long id) {
        multiTenantService.deleteTenant(id);
        return ResponseEntity.ok(APIResponse.success("OK"));
    }

    @GetMapping
    @Operation(summary = "Liste des organisations d'un tenant")
    public ResponseEntity<APIResponse> getOrganisations(@RequestParam Long tenantId) {
        List<OrganisationDTO> list = multiTenantService.getOrganisations(tenantId);
        return ResponseEntity.ok(APIResponse.success(list));
    }

    @GetMapping("/utilisateurs")
    @Operation(summary = "Liste des utilisateurs du tenant")
    public ResponseEntity<APIResponse> getUtilisateurs(@RequestParam Long tenantId) {
        List<UtilisateurListDTO> list = multiTenantService.getUtilisateurs(tenantId);
        return ResponseEntity.ok(APIResponse.success(list));
    }

    @PostMapping("/utilisateurs")
    @Operation(summary = "Créer un utilisateur")
    public ResponseEntity<APIResponse> createUtilisateur(@RequestParam Long tenantId, @RequestBody UtilisateurCreateUpdateDTO dto) {
        UtilisateurListDTO created = multiTenantService.createUtilisateur(tenantId, dto);
        return ResponseEntity.ok(APIResponse.success(created));
    }

    @PutMapping("/utilisateurs/{id:\\d+}")
    @Operation(summary = "Modifier un utilisateur")
    public ResponseEntity<APIResponse> updateUtilisateur(@PathVariable Long id, @RequestBody UtilisateurCreateUpdateDTO dto) {
        UtilisateurListDTO updated = multiTenantService.updateUtilisateur(id, dto);
        return ResponseEntity.ok(APIResponse.success(updated));
    }

    @DeleteMapping("/utilisateurs/{id:\\d+}")
    @Operation(summary = "Supprimer un utilisateur")
    public ResponseEntity<APIResponse> deleteUtilisateur(@PathVariable Long id) {
        multiTenantService.deleteUtilisateur(id);
        return ResponseEntity.ok(APIResponse.success("OK"));
    }

    @GetMapping("/{id:\\d+}")
    @Operation(summary = "Détail d'une organisation")
    public ResponseEntity<APIResponse> getOrganisation(@PathVariable Long id) {
        return multiTenantService.getOrganisation(id)
                .map(APIResponse::success)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer une organisation")
    public ResponseEntity<APIResponse> createOrganisation(@RequestParam Long tenantId, @RequestBody OrganisationDTO dto) {
        OrganisationDTO created = multiTenantService.createOrganisation(tenantId, dto);
        return ResponseEntity.ok(APIResponse.success(created));
    }

    @PutMapping("/{id:\\d+}")
    @Operation(summary = "Modifier une organisation")
    public ResponseEntity<APIResponse> updateOrganisation(@PathVariable Long id, @RequestBody OrganisationDTO dto) {
        OrganisationDTO updated = multiTenantService.updateOrganisation(id, dto);
        return ResponseEntity.ok(APIResponse.success(updated));
    }

    @DeleteMapping("/{id:\\d+}")
    @Operation(summary = "Supprimer une organisation")
    public ResponseEntity<APIResponse> deleteOrganisation(@PathVariable Long id) {
        multiTenantService.deleteOrganisation(id);
        return ResponseEntity.ok(APIResponse.success("OK"));
    }

    @GetMapping("/audit")
    @Operation(summary = "Logs d'audit")
    public ResponseEntity<APIResponse> getAuditLogs(
            @RequestParam Long tenantId,
            @RequestParam(required = false) Long organisationId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<AuditLogDTO> page = multiTenantService.getAuditLogs(tenantId, organisationId, pageable);
        return ResponseEntity.ok(APIResponse.success(page));
    }

    @GetMapping("/parametrage")
    @Operation(summary = "Paramétrage du tenant")
    public ResponseEntity<APIResponse> getParametrage(@RequestParam Long tenantId) {
        ParametrageDTO dto = multiTenantService.getParametrage(tenantId);
        return ResponseEntity.ok(APIResponse.success(dto));
    }

    @PutMapping("/parametrage")
    @Operation(summary = "Modifier le paramétrage")
    public ResponseEntity<APIResponse> updateParametrage(@RequestParam Long tenantId, @RequestBody ParametrageDTO dto) {
        ParametrageDTO updated = multiTenantService.updateParametrage(tenantId, dto);
        return ResponseEntity.ok(APIResponse.success(updated));
    }

    @GetMapping("/quotas")
    @Operation(summary = "Quotas du tenant")
    public ResponseEntity<APIResponse> getQuotas(@RequestParam Long tenantId) {
        QuotaDTO dto = multiTenantService.getQuotas(tenantId);
        return ResponseEntity.ok(APIResponse.success(dto));
    }

    @GetMapping("/roles")
    @Operation(summary = "Liste des rôles")
    public ResponseEntity<APIResponse> getRoles() {
        List<RoleDTO> list = multiTenantService.getRoles();
        return ResponseEntity.ok(APIResponse.success(list));
    }
}
