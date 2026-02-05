package sn.ods.obi.application.services.interfaces.dashboard;

import sn.ods.obi.presentation.dto.dashboard.DashboardDTO;

import java.util.List;
import java.util.Optional;

public interface DashboardService {
    List<DashboardDTO> getDashboards(Long tenantId);
    Optional<DashboardDTO> getDashboard(Long id);
    DashboardDTO createDashboard(DashboardDTO dto);
    DashboardDTO updateDashboard(Long id, DashboardDTO dto);
    void deleteDashboard(Long id);
}
