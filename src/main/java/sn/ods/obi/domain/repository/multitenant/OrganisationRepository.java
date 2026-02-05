package sn.ods.obi.domain.repository.multitenant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.ods.obi.domain.model.multitenant.Organisation;

import java.util.List;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, Long> {
    List<Organisation> findByTenantId(Long tenantId);
}
