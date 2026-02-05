package sn.ods.obi.presentation.web.admin;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ods.obi.application.services.interfaces.multitenant.MultiTenantService;
import sn.ods.obi.domain.model.admin.Job;
import sn.ods.obi.domain.model.connexion.Connexion;
import sn.ods.obi.domain.repository.admin.JobRepository;
import sn.ods.obi.domain.repository.connexion.ConnexionRepository;
import sn.ods.obi.infrastructure.logging.ErrorLog;
import sn.ods.obi.infrastructure.logging.ErrorLogRepository;
import sn.ods.obi.presentation.dto.responses.APIResponse;
import sn.ods.obi.presentation.dto.multitenant.TenantDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminResource {

    private final MultiTenantService multiTenantService;
    private final ConnexionRepository connexionRepository;
    private final JobRepository jobRepository;
    private final ErrorLogRepository errorLogRepository;

    @GetMapping("/tenants")
    @Operation(summary = "Liste des tenants (super-admin)")
    public ResponseEntity<APIResponse> getTenants() {
        List<TenantDTO> list = multiTenantService.getTenants();
        return ResponseEntity.ok(APIResponse.success(list));
    }

    @GetMapping("/connexions/monitoring")
    @Operation(summary = "Monitoring des connexions")
    public ResponseEntity<APIResponse> getMonitoring() {
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
    @Operation(summary = "Liste des jobs")
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

    @PostMapping("/jobs")
    @Operation(summary = "Créer un job")
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

    @GetMapping("/logs")
    @Operation(summary = "Logs applicatifs")
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
