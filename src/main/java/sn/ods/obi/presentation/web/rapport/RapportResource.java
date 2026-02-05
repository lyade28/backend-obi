package sn.ods.obi.presentation.web.rapport;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ods.obi.application.services.interfaces.rapport.RapportService;
import sn.ods.obi.presentation.dto.responses.APIResponse;
import sn.ods.obi.presentation.dto.rapport.RapportDTO;

import java.util.List;

@RestController
@RequestMapping("/rapports")
@RequiredArgsConstructor
public class RapportResource {

    private final RapportService rapportService;

    @GetMapping
    @Operation(summary = "Liste des rapports")
    public ResponseEntity<APIResponse> getRapports(@RequestParam Long tenantId) {
        List<RapportDTO> list = rapportService.getRapports(tenantId);
        return ResponseEntity.ok(APIResponse.success(list));
    }

    @GetMapping("/{id:\\d+}")
    @Operation(summary = "Détail d'un rapport")
    public ResponseEntity<APIResponse> getRapport(@PathVariable("id") Long id) {
        return rapportService.getRapport(id)
                .map(APIResponse::success)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer un rapport")
    public ResponseEntity<APIResponse> createRapport(@RequestBody RapportDTO dto) {
        RapportDTO created = rapportService.createRapport(dto);
        return ResponseEntity.ok(APIResponse.success(created));
    }

    @PutMapping("/{id:\\d+}")
    @Operation(summary = "Modifier un rapport")
    public ResponseEntity<APIResponse> updateRapport(@PathVariable("id") Long id, @RequestBody RapportDTO dto) {
        RapportDTO updated = rapportService.updateRapport(id, dto);
        return ResponseEntity.ok(APIResponse.success(updated));
    }

    @DeleteMapping("/{id:\\d+}")
    @Operation(summary = "Supprimer un rapport")
    public ResponseEntity<APIResponse> deleteRapport(@PathVariable("id") Long id) {
        rapportService.deleteRapport(id);
        return ResponseEntity.ok(APIResponse.success("OK"));
    }
}
