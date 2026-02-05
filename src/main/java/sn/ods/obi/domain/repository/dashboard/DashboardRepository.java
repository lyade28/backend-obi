package sn.ods.obi.domain.repository.dashboard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.ods.obi.domain.model.dashboard.Dashboard;

import java.util.List;

@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, Long> {
    List<Dashboard> findByTenantId(Long tenantId);
}
