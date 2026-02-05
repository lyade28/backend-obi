package sn.ods.obi.presentation.web.dataset;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ods.obi.application.services.interfaces.dataset.DatasetService;
import sn.ods.obi.presentation.dto.responses.APIResponse;
import sn.ods.obi.presentation.dto.dataset.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/datasets")
@RequiredArgsConstructor
public class DatasetResource {

    private final DatasetService datasetService;

    @GetMapping("/queries")
    @Operation(summary = "Liste des requêtes sauvegardées")
    public ResponseEntity<APIResponse> getQueries(@RequestParam Long tenantId) {
        List<SavedQueryDTO> list = datasetService.getSavedQueries(tenantId);
        return ResponseEntity.ok(APIResponse.success(list));
    }

    @GetMapping("/queries/{id:\\d+}")
    @Operation(summary = "Détail d'une requête sauvegardée")
    public ResponseEntity<APIResponse> getQuery(@PathVariable Long id) {
        SavedQueryDTO q = datasetService.getQuery(id);
        return ResponseEntity.ok(APIResponse.success(q));
    }

    @PostMapping("/queries")
    @Operation(summary = "Sauvegarder une requête")
    public ResponseEntity<APIResponse> saveQuery(@RequestBody SavedQueryDTO dto) {
        SavedQueryDTO created = datasetService.saveQuery(dto);
        return ResponseEntity.ok(APIResponse.success(created));
    }

    @PutMapping("/queries/{id:\\d+}")
    @Operation(summary = "Modifier une requête")
    public ResponseEntity<APIResponse> updateQuery(@PathVariable Long id, @RequestBody SavedQueryDTO dto) {
        SavedQueryDTO updated = datasetService.updateQuery(id, dto);
        return ResponseEntity.ok(APIResponse.success(updated));
    }

    @DeleteMapping("/queries/{id:\\d+}")
    @Operation(summary = "Supprimer une requête")
    public ResponseEntity<APIResponse> deleteQuery(@PathVariable Long id) {
        datasetService.deleteQuery(id);
        return ResponseEntity.ok(APIResponse.success("OK"));
    }

    @GetMapping
    @Operation(summary = "Liste des datasets")
    public ResponseEntity<APIResponse> getDatasets(@RequestParam Long tenantId) {
        List<DatasetDTO> list = datasetService.getDatasets(tenantId);
        return ResponseEntity.ok(APIResponse.success(list));
    }

    @PostMapping
    @Operation(summary = "Créer un dataset")
    public ResponseEntity<APIResponse> createDataset(@RequestBody DatasetDTO dto) {
        DatasetDTO created = datasetService.createDataset(dto);
        return ResponseEntity.ok(APIResponse.success(created));
    }

    @PutMapping("/{id:\\d+}")
    @Operation(summary = "Modifier un dataset")
    public ResponseEntity<APIResponse> updateDataset(@PathVariable Long id, @RequestBody DatasetDTO dto) {
        DatasetDTO updated = datasetService.updateDataset(id, dto);
        return ResponseEntity.ok(APIResponse.success(updated));
    }

    @DeleteMapping("/{id:\\d+}")
    @Operation(summary = "Supprimer un dataset")
    public ResponseEntity<APIResponse> deleteDataset(@PathVariable Long id) {
        datasetService.deleteDataset(id);
        return ResponseEntity.ok(APIResponse.success("OK"));
    }

    @GetMapping("/queries/{id:\\d+}/params")
    @Operation(summary = "Paramètres d'une requête")
    public ResponseEntity<APIResponse> getParams(@PathVariable Long id) {
        List<QueryParameterDTO> list = datasetService.getParams(id);
        return ResponseEntity.ok(APIResponse.success(list));
    }

    @PostMapping("/queries/params")
    @Operation(summary = "Ajouter un paramètre")
    public ResponseEntity<APIResponse> addParam(@RequestBody QueryParameterDTO dto) {
        QueryParameterDTO created = datasetService.addParam(dto);
        return ResponseEntity.ok(APIResponse.success(created));
    }

    @PostMapping("/execute")
    @Operation(summary = "Exécuter une requête SQL")
    public ResponseEntity<APIResponse> executeQuery(@RequestBody Map<String, Object> body) {
        Long connexionId = Long.valueOf(String.valueOf(body.get("connexionId")));
        String sql = (String) body.get("sql");
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) body.getOrDefault("params", Map.of());
        QueryResultDTO result = datasetService.executeQuery(connexionId, sql, params);
        return ResponseEntity.ok(APIResponse.success(result));
    }

    @GetMapping("/{id:\\d+}/columns")
    @Operation(summary = "Colonnes d'un dataset (nom + type pour agrégation)")
    public ResponseEntity<APIResponse> getDatasetColumns(@PathVariable Long id) {
        List<java.util.Map<String, String>> columns = datasetService.getDatasetColumnInfos(id);
        return ResponseEntity.ok(APIResponse.success(columns));
    }

    @GetMapping("/queries/{id:\\d+}/columns")
    @Operation(summary = "Colonnes d'une requête sauvegardée (nom + type pour agrégation)")
    public ResponseEntity<APIResponse> getQueryColumns(@PathVariable Long id) {
        List<java.util.Map<String, String>> columns = datasetService.getQueryColumnInfos(id);
        return ResponseEntity.ok(APIResponse.success(columns));
    }

    @GetMapping("/{id:\\d+}/data")
    @Operation(summary = "Données d'un dataset (premières lignes)")
    public ResponseEntity<APIResponse> getDatasetData(@PathVariable Long id,
                                                     @RequestParam(required = false, defaultValue = "100") int limit) {
        QueryResultDTO data = datasetService.getDatasetData(id, limit);
        return ResponseEntity.ok(APIResponse.success(data));
    }

    @GetMapping("/{id:\\d+}/aggregate")
    @Operation(summary = "Valeur agrégée d'une colonne (SUM, AVG, COUNT, MIN, MAX)")
    public ResponseEntity<APIResponse> getDatasetAggregate(@PathVariable Long id,
                                                          @RequestParam String column,
                                                          @RequestParam(required = false, defaultValue = "sum") String agg) {
        Object value = datasetService.getDatasetAggregate(id, column, agg);
        return ResponseEntity.ok(APIResponse.success(value));
    }

    @GetMapping("/queries/{id:\\d+}/aggregate")
    @Operation(summary = "Valeur agrégée sur une requête SQL")
    public ResponseEntity<APIResponse> getQueryAggregate(@PathVariable Long id,
                                                        @RequestParam String column,
                                                        @RequestParam(required = false, defaultValue = "sum") String agg) {
        Object value = datasetService.getQueryAggregate(id, column, agg);
        return ResponseEntity.ok(APIResponse.success(value));
    }
}
