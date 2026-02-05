package sn.ods.obi.presentation.web.import_;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.ods.obi.application.services.interfaces.import_.DataImportService;
import sn.ods.obi.presentation.dto.import_.DataImportDTO;
import sn.ods.obi.presentation.dto.import_.ImportPreviewDTO;
import sn.ods.obi.presentation.dto.responses.APIResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/imports")
@RequiredArgsConstructor
public class DataImportResource {

    private final DataImportService dataImportService;

    @PostMapping
    @Operation(summary = "Importer un fichier Excel/CSV et stocker les données en base")
    public ResponseEntity<APIResponse> importFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam Long tenantId,
            @RequestParam(required = false, defaultValue = ";") String csvDelimiter,
            @RequestParam(required = false, defaultValue = "UTF-8") String csvEncoding,
            @RequestParam(required = false, defaultValue = "true") boolean firstRowHeaders) {
        DataImportDTO dto = dataImportService.importFile(file, tenantId, csvDelimiter, csvEncoding, firstRowHeaders);
        return ResponseEntity.ok(APIResponse.success(dto));
    }

    @GetMapping
    @Operation(summary = "Liste des imports par tenant")
    public ResponseEntity<APIResponse> list(@RequestParam Long tenantId) {
        List<DataImportDTO> list = dataImportService.listByTenant(tenantId);
        return ResponseEntity.ok(APIResponse.success(list));
    }

    @GetMapping("/{id}/preview")
    @Operation(summary = "Aperçu des données (premières lignes)")
    public ResponseEntity<APIResponse> preview(@PathVariable Long id,
                                               @RequestParam(required = false, defaultValue = "100") int limit) {
        ImportPreviewDTO preview = dataImportService.getPreview(id, limit);
        return ResponseEntity.ok(APIResponse.success(preview));
    }

    @GetMapping("/{id}/columns")
    @Operation(summary = "Colonnes de l'import")
    public ResponseEntity<APIResponse> columns(@PathVariable Long id) {
        List<Map<String, String>> columns = dataImportService.getColumns(id);
        return ResponseEntity.ok(APIResponse.success(columns));
    }
}
