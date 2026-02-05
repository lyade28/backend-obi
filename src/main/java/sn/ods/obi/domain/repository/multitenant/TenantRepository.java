package sn.ods.obi.domain.repository.multitenant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.ods.obi.domain.model.multitenant.Tenant;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
}
