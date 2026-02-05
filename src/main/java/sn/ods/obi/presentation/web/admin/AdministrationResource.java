package sn.ods.obi.presentation.web.admin;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ods.obi.domain.model.admin.Job;
import sn.ods.obi.domain.model.connexion.Connexion;
import sn.ods.obi.domain.repository.admin.JobRepository;
import sn.ods.obi.domain.repository.connexion.ConnexionRepository;
import sn.ods.obi.infrastructure.logging.ErrorLog;
import sn.ods.obi.infrastructure.logging.ErrorLogRepository;
import sn.ods.obi.presentation.dto.responses.APIResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Alias des endpoints admin sous le préfixe /administration pour compatibilité
 * avec les appels frontend GET /api/v1/administration/connexions-monitoring, jobs, logs.
 */
@RestController
@RequestMapping("/administration")
@RequiredArgsConstructor
public class AdministrationResource {

    private final ConnexionRepository connexionRepository;
    private final JobRepository jobRepository;
    private final ErrorLogRepository errorLogRepository;

    @GetMapping("/connexions-monitoring")
    @Operation(summary = "Monitoring des connexions (alias)")
    public ResponseEntity<APIResponse> getConnexionsMonitoring() {
        List<Connexion> conns = connexionRepository.findAll();
        List<Map<String, Object>> result = conns.stream()
                .map(c -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("nom", c.getNom());
                    m.put("statut", "OK");
                    m.put("tempsReponse", "—");
                    m.put("erreursRecentes", 0);
                    return m;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(APIResponse.success(result));
    }

    @GetMapping("/jobs")
    @Operation(summary = "Liste des jobs (alias)")
    public ResponseEntity<APIResponse> getJobs() {
        List<Job> jobs = jobRepository.findAll();
        List<Map<String, Object>> result = jobs.stream()
                .map(j -> Map.<String, Object>of(
                        "id", j.getId(),
                        "nom", j.getNom(),
                        "type", j.getType(),
                        "frequence", j.getFrequence(),
                        "dernierRun", j.getDernierRun() != null ? j.getDernierRun().toString() : "—",
                        "statut", j.getStatut()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(APIResponse.success(result));
    }

    @GetMapping("/jobs/{id:\\d+}")
    @Operation(summary = "Détail d'un job (alias)")
    public ResponseEntity<APIResponse> getJob(@PathVariable("id") Long id) {
        return jobRepository.findById(id)
                .map(j -> ResponseEntity.ok(APIResponse.success(Map.<String, Object>of(
                        "id", j.getId(),
                        "nom", j.getNom(),
                        "type", j.getType(),
                        "frequence", j.getFrequence(),
                        "dernierRun", j.getDernierRun() != null ? j.getDernierRun().toString() : "—",
                        "statut", j.getStatut()))))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/jobs")
    @Operation(summary = "Créer un job (alias)")
    public ResponseEntity<APIResponse> createJob(@RequestBody Map<String, String> body) {
        Job j = Job.builder()
                .nom(body.getOrDefault("nom", ""))
                .type(body.getOrDefault("type", "Import"))
                .frequence(body.getOrDefault("frequence", "Quotidien"))
                .statut(body.getOrDefault("statut", "OK"))
                .build();
        j = jobRepository.save(j);
        return ResponseEntity.ok(APIResponse.success(Map.of("id", j.getId(), "nom", j.getNom())));
    }

    @PutMapping("/jobs/{id:\\d+}")
    @Operation(summary = "Modifier un job (alias)")
    public ResponseEntity<APIResponse> updateJob(@PathVariable("id") Long id, @RequestBody Map<String, String> body) {
        return jobRepository.findById(id)
                .map(j -> {
                    j.setNom(body.getOrDefault("nom", j.getNom()));
                    j.setType(body.getOrDefault("type", j.getType()));
                    j.setFrequence(body.getOrDefault("frequence", j.getFrequence()));
                    j.setStatut(body.getOrDefault("statut", j.getStatut()));
                    j = jobRepository.save(j);
                    return ResponseEntity.ok(APIResponse.success(Map.of("id", j.getId(), "nom", j.getNom())));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/jobs/{id:\\d+}")
    @Operation(summary = "Supprimer un job (alias)")
    public ResponseEntity<APIResponse> deleteJob(@PathVariable("id") Long id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return ResponseEntity.ok(APIResponse.success("OK"));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/logs")
    @Operation(summary = "Logs applicatifs (alias)")
    public ResponseEntity<APIResponse> getLogs(@PageableDefault(size = 50) Pageable pageable) {
        Page<ErrorLog> page = errorLogRepository.findAll(pageable);
        List<Map<String, Object>> result = page.getContent().stream()
                .map(l -> Map.<String, Object>of(
                        "date", l.getTimestamp() != null ? l.getTimestamp().toString() : "",
                        "niveau", "INFO",
                        "message", l.getMessage() != null ? l.getMessage() : ""))
                .collect(Collectors.toList());
        return ResponseEntity.ok(APIResponse.success(result));
    }
}
