package sn.ods.obi.presentation.web.widget;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ods.obi.application.services.interfaces.widget.WidgetDefinitionService;
import sn.ods.obi.presentation.dto.responses.APIResponse;
import sn.ods.obi.presentation.dto.widget.WidgetDefinitionDTO;

import java.util.List;

@RestController
@RequestMapping("/widget-definitions")
@RequiredArgsConstructor
public class WidgetDefinitionResource {

    private final WidgetDefinitionService widgetDefinitionService;

    @GetMapping
    @Operation(summary = "Liste des définitions de widgets")
    public ResponseEntity<APIResponse> getWidgetDefinitions(@RequestParam Long tenantId) {
        List<WidgetDefinitionDTO> list = widgetDefinitionService.getWidgetDefinitions(tenantId);
        return ResponseEntity.ok(APIResponse.success(list));
    }

    @GetMapping("/{id:\\d+}")
    @Operation(summary = "Détail d'une définition de widget")
    public ResponseEntity<APIResponse> getById(@PathVariable Long id) {
        WidgetDefinitionDTO dto = widgetDefinitionService.getById(id);
        return ResponseEntity.ok(APIResponse.success(dto));
    }

    @PostMapping
    @Operation(summary = "Créer une définition de widget")
    public ResponseEntity<APIResponse> create(@RequestBody WidgetDefinitionDTO dto) {
        WidgetDefinitionDTO created = widgetDefinitionService.create(dto);
        return ResponseEntity.ok(APIResponse.success(created));
    }

    @PutMapping("/{id:\\d+}")
    @Operation(summary = "Modifier une définition de widget")
    public ResponseEntity<APIResponse> update(@PathVariable Long id, @RequestBody WidgetDefinitionDTO dto) {
        WidgetDefinitionDTO updated = widgetDefinitionService.update(id, dto);
        return ResponseEntity.ok(APIResponse.success(updated));
    }

    @DeleteMapping("/{id:\\d+}")
    @Operation(summary = "Supprimer une définition de widget")
    public ResponseEntity<APIResponse> delete(@PathVariable Long id) {
        widgetDefinitionService.delete(id);
        return ResponseEntity.ok(APIResponse.success("OK"));
    }
}
