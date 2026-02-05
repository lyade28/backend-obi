package sn.ods.obi.presentation.web.connexion;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.ods.obi.application.services.implement.shared.file.IFile;
import sn.ods.obi.application.services.interfaces.connexion.ConnexionService;
import sn.ods.obi.application.services.interfaces.import_.DataImportService;
import sn.ods.obi.presentation.dto.responses.APIResponse;
import sn.ods.obi.infrastructure.config.exceptions.APIException;
import sn.ods.obi.presentation.dto.connexion.ConnexionDTO;
import sn.ods.obi.presentation.dto.connexion.ConnexionSafeDTO;
import sn.ods.obi.presentation.dto.connexion.SchemaTableDTO;
import sn.ods.obi.presentation.dto.import_.DataImportDTO;
import sn.ods.obi.presentation.dto.import_.ImportPreviewDTO;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/connexions")
@RequiredArgsConstructor
public class ConnexionResource {

    private final ConnexionService connexionService;
    private final IFile fileService;
    private final DataImportService dataImportService;

    @GetMapping
    @Operation(summary = "Liste des connexions")
    public ResponseEntity<APIResponse> getConnexions(@RequestParam Long tenantId) {
        List<ConnexionSafeDTO> list = connexionService.getConnexions(tenantId);
        return ResponseEntity.ok(APIResponse.success(list));
    }

    @GetMapping("/{id:\\d+}")
    @Operation(summary = "Détail d'une connexion")
    public ResponseEntity<APIResponse> getConnexion(@PathVariable Long id) {
        return connexionService.getConnexion(id)
                .map(APIResponse::success)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer une connexion")
    public ResponseEntity<APIResponse> createConnexion(@RequestBody ConnexionDTO dto) {
        ConnexionSafeDTO created = connexionService.createConnexion(dto);
        return ResponseEntity.ok(APIResponse.success(created));
    }

    @PutMapping("/{id:\\d+}")
    @Operation(summary = "Modifier une connexion")
    public ResponseEntity<APIResponse> updateConnexion(@PathVariable Long id, @RequestBody ConnexionDTO dto) {
        ConnexionSafeDTO updated = connexionService.updateConnexion(id, dto);
        return ResponseEntity.ok(APIResponse.success(updated));
    }

    @DeleteMapping("/{id:\\d+}")
    @Operation(summary = "Supprimer une connexion")
    public ResponseEntity<APIResponse> deleteConnexion(@PathVariable Long id) {
        connexionService.deleteConnexion(id);
        return ResponseEntity.ok(APIResponse.success("OK"));
    }

    @PostMapping("/{id:\\d+}/test")
    @Operation(summary = "Tester une connexion")
    public ResponseEntity<APIResponse> testConnexion(@PathVariable Long id) {
        boolean ok = connexionService.testConnexion(id);
        return ResponseEntity.ok(APIResponse.success(ok ? "OK" : "Échec"));
    }

    @GetMapping("/{id:\\d+}/schemas")
    @Operation(summary = "Explorer les schémas/tables")
    public ResponseEntity<APIResponse> getSchemas(@PathVariable Long id) {
        SchemaTableDTO dto = connexionService.getSchemas(id);
        return ResponseEntity.ok(APIResponse.success(dto));
    }

    @PostMapping("/import/file")
    @Operation(summary = "Importer un fichier Excel/CSV (stockage disque)")
    public ResponseEntity<APIResponse> importFile(@RequestParam("file") MultipartFile file,
                                                  @RequestParam(required = false) String directory) throws APIException {
        var result = fileService.storeFile(file, directory != null ? directory : "imports", false);
        return ResponseEntity.ok(APIResponse.success(result));
    }

    @PostMapping("/import/excel")
    @Operation(summary = "Importer un fichier Excel (.xlsx, .xls) et stocker les données en base")
    public ResponseEntity<APIResponse> importExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam Long tenantId,
            @RequestParam(required = false, defaultValue = "true") boolean firstRowHeaders) {
        DataImportDTO dto = dataImportService.importExcel(file, tenantId, firstRowHeaders);
        return ResponseEntity.ok(APIResponse.success(dto));
    }

    @PostMapping("/import/csv")
    @Operation(summary = "Importer un fichier CSV et stocker les données en base")
    public ResponseEntity<APIResponse> importCsv(
            @RequestParam("file") MultipartFile file,
            @RequestParam Long tenantId,
            @RequestParam(required = false, defaultValue = ";") String csvDelimiter,
            @RequestParam(required = false, defaultValue = "UTF-8") String csvEncoding,
            @RequestParam(required = false, defaultValue = "true") boolean firstRowHeaders) {
        DataImportDTO dto = dataImportService.importCsv(file, tenantId, csvDelimiter, csvEncoding, firstRowHeaders);
        return ResponseEntity.ok(APIResponse.success(dto));
    }

    @GetMapping("/imports")
    @Operation(summary = "Liste des imports par tenant")
    public ResponseEntity<APIResponse> listImports(@RequestParam Long tenantId) {
        return ResponseEntity.ok(APIResponse.success(dataImportService.listByTenant(tenantId)));
    }

    @GetMapping("/imports/{id:\\d+}/preview")
    @Operation(summary = "Aperçu des données importées")
    public ResponseEntity<APIResponse> previewImport(@PathVariable("id") Long id,
                                                     @RequestParam(required = false, defaultValue = "100") int limit) {
        ImportPreviewDTO preview = dataImportService.getPreview(id, limit);
        return ResponseEntity.ok(APIResponse.success(preview));
    }

    @GetMapping("/imports/{id:\\d+}/columns")
    @Operation(summary = "Colonnes de l'import")
    public ResponseEntity<APIResponse> importColumns(@PathVariable("id") Long id) {
        List<Map<String, String>> columns = dataImportService.getColumns(id);
        return ResponseEntity.ok(APIResponse.success(columns));
    }

    @GetMapping("/imports/{id:\\d+}/aggregate")
    @Operation(summary = "Valeur agrégée sur une colonne de l'import")
    public ResponseEntity<APIResponse> importAggregate(@PathVariable("id") Long id,
                                                     @RequestParam String column,
                                                     @RequestParam(required = false, defaultValue = "sum") String agg) {
        Object value = dataImportService.getImportAggregate(id, column, agg);
        return ResponseEntity.ok(APIResponse.success(value));
    }

    @DeleteMapping("/imports/{id:\\d+}")
    @Operation(summary = "Supprimer un import et ses données")
    public ResponseEntity<APIResponse> deleteImport(@PathVariable("id") Long id) {
        dataImportService.deleteImport(id);
        return ResponseEntity.ok(APIResponse.success("OK"));
    }
}
