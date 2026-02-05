package sn.ods.obi.presentation.web.dashboard;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ods.obi.application.services.interfaces.dashboard.DashboardService;
import sn.ods.obi.presentation.dto.responses.APIResponse;
import sn.ods.obi.presentation.dto.dashboard.DashboardDTO;

import java.util.List;

@RestController
@RequestMapping("/dashboards")
@RequiredArgsConstructor
public class DashboardResource {

    private final DashboardService dashboardService;

    @GetMapping
    @Operation(summary = "Liste des dashboards")
    public ResponseEntity<APIResponse> getDashboards(@RequestParam Long tenantId) {
        List<DashboardDTO> list = dashboardService.getDashboards(tenantId);
        return ResponseEntity.ok(APIResponse.success(list));
    }

    @GetMapping("/{id:\\d+}")
    @Operation(summary = "Détail d'un dashboard")
    public ResponseEntity<APIResponse> getDashboard(@PathVariable Long id) {
        return dashboardService.getDashboard(id)
                .map(APIResponse::success)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer un dashboard")
    public ResponseEntity<APIResponse> createDashboard(@RequestBody DashboardDTO dto) {
        DashboardDTO created = dashboardService.createDashboard(dto);
        return ResponseEntity.ok(APIResponse.success(created));
    }

    @PutMapping("/{id:\\d+}")
    @Operation(summary = "Modifier un dashboard")
    public ResponseEntity<APIResponse> updateDashboard(@PathVariable Long id, @RequestBody DashboardDTO dto) {
        DashboardDTO updated = dashboardService.updateDashboard(id, dto);
        return ResponseEntity.ok(APIResponse.success(updated));
    }

    @DeleteMapping("/{id:\\d+}")
    @Operation(summary = "Supprimer un dashboard")
    public ResponseEntity<APIResponse> deleteDashboard(@PathVariable Long id) {
        dashboardService.deleteDashboard(id);
        return ResponseEntity.ok(APIResponse.success("OK"));
    }
}
