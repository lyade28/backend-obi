package sn.ods.obi.domain.repository.dataset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.ods.obi.domain.model.dataset.Dataset;

import java.util.List;

@Repository
public interface DatasetRepository extends JpaRepository<Dataset, Long> {
    List<Dataset> findByTenantId(Long tenantId);

    List<Dataset> findByImportId(Long importId);
}
