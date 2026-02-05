package sn.ods.obi.domain.repository.multitenant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.ods.obi.domain.model.multitenant.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    Page<AuditLog> findByTenantIdOrderByDateDesc(Long tenantId, Pageable pageable);
    Page<AuditLog> findByOrganisationIdOrderByDateDesc(Long organisationId, Pageable pageable);
}
