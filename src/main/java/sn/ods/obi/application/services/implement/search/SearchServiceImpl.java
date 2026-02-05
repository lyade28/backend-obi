package sn.ods.obi.application.services.implement.search;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.ods.obi.application.services.interfaces.search.SearchService;
import sn.ods.obi.domain.model.connexion.Connexion;
import sn.ods.obi.domain.model.dashboard.Dashboard;
import sn.ods.obi.domain.model.dataset.Dataset;
import sn.ods.obi.domain.model.rapport.Rapport;
import sn.ods.obi.domain.repository.connexion.ConnexionRepository;
import sn.ods.obi.domain.repository.dashboard.DashboardRepository;
import sn.ods.obi.domain.repository.dataset.DatasetRepository;
import sn.ods.obi.domain.repository.rapport.RapportRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final RapportRepository rapportRepository;
    private final DashboardRepository dashboardRepository;
    private final DatasetRepository datasetRepository;
    private final ConnexionRepository connexionRepository;

    @Override
    public Map<String, Object> search(String query, Long tenantId) {
        String q = query == null ? "" : query.toLowerCase().trim();
        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> rapports = rapportRepository.findByTenantId(tenantId).stream()
                .filter(r -> q.isEmpty() || (r.getNom() != null && r.getNom().toLowerCase().contains(q)) || (r.getDossier() != null && r.getDossier().toLowerCase().contains(q)))
                .map(r -> Map.<String, Object>of("id", r.getId().toString(), "nom", r.getNom(), "type", r.getType(), "dossier", r.getDossier() != null ? r.getDossier() : ""))
                .collect(Collectors.toList());

        List<Map<String, Object>> dashboards = dashboardRepository.findByTenantId(tenantId).stream()
                .filter(d -> q.isEmpty() || (d.getNom() != null && d.getNom().toLowerCase().contains(q)))
                .map(d -> Map.<String, Object>of("id", "d" + d.getId(), "nom", d.getNom()))
                .collect(Collectors.toList());

        result.put("rapports", rapports);
        result.put("dashboards", dashboards);
        return result;
    }
}
