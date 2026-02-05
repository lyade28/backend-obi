package sn.ods.obi.domain.repository.rapport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.ods.obi.domain.model.rapport.Rapport;

import java.util.List;

@Repository
public interface RapportRepository extends JpaRepository<Rapport, Long> {
    List<Rapport> findByTenantId(Long tenantId);
}
