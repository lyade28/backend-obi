package sn.ods.obi.domain.repository.multitenant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.ods.obi.domain.model.multitenant.TenantParametrage;

@Repository
public interface TenantParametrageRepository extends JpaRepository<TenantParametrage, Long> {
}
