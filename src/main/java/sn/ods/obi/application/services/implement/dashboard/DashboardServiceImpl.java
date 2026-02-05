package sn.ods.obi.application.services.implement.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ods.obi.domain.model.dashboard.Dashboard;
import sn.ods.obi.domain.repository.dashboard.DashboardRepository;
import sn.ods.obi.application.services.interfaces.dashboard.DashboardService;
import sn.ods.obi.presentation.dto.dashboard.DashboardDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;

    @Override
    public List<DashboardDTO> getDashboards(Long tenantId) {
        return dashboardRepository.findByTenantId(tenantId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DashboardDTO> getDashboard(Long id) {
        return dashboardRepository.findById(id).map(this::toDTO);
    }

    @Override
    @Transactional
    public DashboardDTO createDashboard(DashboardDTO dto) {
        Dashboard d = Dashboard.builder()
                .nom(dto.getNom())
                .description(dto.getDescription())
                .layoutJson(dto.getLayoutJson())
                .tenantId(dto.getTenantId())
                .build();
        d = dashboardRepository.save(d);
        return toDTO(d);
    }

    @Override
    @Transactional
    public DashboardDTO updateDashboard(Long id, DashboardDTO dto) {
        Dashboard d = dashboardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Dashboard non trouvé"));
        if (dto.getNom() != null) d.setNom(dto.getNom());
        if (dto.getDescription() != null) d.setDescription(dto.getDescription());
        if (dto.getLayoutJson() != null) d.setLayoutJson(dto.getLayoutJson());
        d = dashboardRepository.save(d);
        return toDTO(d);
    }

    @Override
    @Transactional
    public void deleteDashboard(Long id) {
        dashboardRepository.deleteById(id);
    }

    private DashboardDTO toDTO(Dashboard d) {
        return DashboardDTO.builder()
                .id(d.getId())
                .nom(d.getNom())
                .description(d.getDescription())
                .layoutJson(d.getLayoutJson())
                .tenantId(d.getTenantId())
                .build();
    }
}
