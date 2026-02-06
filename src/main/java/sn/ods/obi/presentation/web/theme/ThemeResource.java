package sn.ods.obi.presentation.web.theme;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ods.obi.application.services.interfaces.ThemeService;
import sn.ods.obi.presentation.dto.multitenant.ThemeConfigDTO;
import sn.ods.obi.presentation.dto.responses.APIResponse;

@RestController
@RequestMapping("/theme")
@RequiredArgsConstructor
public class ThemeResource {

    private final ThemeService themeService;

    @GetMapping
    @Operation(summary = "Récupérer le thème du tenant (personnalisation couleurs / logo)")
    public ResponseEntity<APIResponse> getTheme(@RequestParam Long tenantId) {
        return themeService.getTheme(tenantId)
                .map(APIResponse::success)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(APIResponse.success(null)));
    }

    @PutMapping
    @Operation(summary = "Enregistrer le thème du tenant")
    public ResponseEntity<APIResponse> saveTheme(@RequestParam Long tenantId, @RequestBody ThemeConfigDTO dto) {
        ThemeConfigDTO saved = themeService.saveTheme(tenantId, dto);
        return ResponseEntity.ok(APIResponse.success(saved));
    }
}
