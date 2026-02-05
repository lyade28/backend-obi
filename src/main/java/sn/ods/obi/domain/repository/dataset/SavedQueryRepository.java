package sn.ods.obi.domain.repository.dataset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.ods.obi.domain.model.dataset.SavedQuery;

import java.util.List;

@Repository
public interface SavedQueryRepository extends JpaRepository<SavedQuery, Long> {
    List<SavedQuery> findByTenantId(Long tenantId);
}
