package sn.ods.obi.domain.repository.import_;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.ods.obi.domain.model.import_.DataImport;

import java.util.List;

@Repository
public interface DataImportRepository extends JpaRepository<DataImport, Long> {
    List<DataImport> findByTenantIdOrderByCreatedAtDesc(Long tenantId);
}
