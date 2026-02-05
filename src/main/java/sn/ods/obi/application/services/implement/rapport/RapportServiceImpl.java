package sn.ods.obi.application.services.implement.rapport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ods.obi.domain.model.rapport.Rapport;
import sn.ods.obi.domain.repository.rapport.RapportRepository;
import sn.ods.obi.application.services.interfaces.rapport.RapportService;
import sn.ods.obi.presentation.dto.rapport.RapportDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RapportServiceImpl implements RapportService {

    private final RapportRepository rapportRepository;

    @Override
    public List<RapportDTO> getRapports(Long tenantId) {
        return rapportRepository.findByTenantId(tenantId).stream()
                .map(r -> RapportDTO.builder()
                        .id(r.getId())
                        .nom(r.getNom())
                        .type(r.getType())
                        .dossier(r.getDossier())
                        .datasetId(r.getDatasetId())
                        .tenantId(r.getTenantId())
                        .modifieLe(java.time.LocalDate.now().toString())
                        .configJson(r.getConfigJson())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RapportDTO> getRapport(Long id) {
        return rapportRepository.findById(id).map(r -> RapportDTO.builder()
                .id(r.getId())
                .nom(r.getNom())
                .type(r.getType())
                .dossier(r.getDossier())
                .datasetId(r.getDatasetId())
                .tenantId(r.getTenantId())
                .modifieLe(java.time.LocalDate.now().toString())
                .configJson(r.getConfigJson())
                .build());
    }

    @Override
    @Transactional
    public RapportDTO createRapport(RapportDTO dto) {
        Rapport r = Rapport.builder()
                .nom(dto.getNom())
                .type(dto.getType() != null ? dto.getType() : "Tableau")
                .dossier(dto.getDossier())
                .datasetId(dto.getDatasetId())
                .tenantId(dto.getTenantId())
                .configJson(dto.getConfigJson())
                .build();
        r = rapportRepository.save(r);
        return RapportDTO.builder().id(r.getId()).nom(r.getNom()).type(r.getType()).dossier(r.getDossier()).datasetId(r.getDatasetId()).tenantId(r.getTenantId()).modifieLe(java.time.LocalDate.now().toString()).configJson(r.getConfigJson()).build();
    }

    @Override
    @Transactional
    public RapportDTO updateRapport(Long id, RapportDTO dto) {
        Rapport r = rapportRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Rapport non trouvé"));
        if (dto.getNom() != null) r.setNom(dto.getNom());
        if (dto.getType() != null) r.setType(dto.getType());
        if (dto.getDossier() != null) r.setDossier(dto.getDossier());
        if (dto.getConfigJson() != null) r.setConfigJson(dto.getConfigJson());
        r = rapportRepository.save(r);
        return RapportDTO.builder().id(r.getId()).nom(r.getNom()).type(r.getType()).dossier(r.getDossier()).datasetId(r.getDatasetId()).tenantId(r.getTenantId()).modifieLe(java.time.LocalDate.now().toString()).configJson(r.getConfigJson()).build();
    }

    @Override
    @Transactional
    public void deleteRapport(Long id) {
        rapportRepository.deleteById(id);
    }
}
