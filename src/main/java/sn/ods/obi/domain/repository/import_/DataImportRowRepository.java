package sn.ods.obi.domain.repository.import_;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sn.ods.obi.domain.model.import_.DataImportRow;

import java.util.List;

@Repository
public interface DataImportRowRepository extends JpaRepository<DataImportRow, Long> {
    List<DataImportRow> findByImportIdOrderByRowIndexAsc(Long importId, Pageable pageable);

    List<DataImportRow> findByImportIdOrderByRowIndexAsc(Long importId);

    long countByImportId(Long importId);

    @Modifying
    @Query("DELETE FROM DataImportRow r WHERE r.importId = :importId")
    void deleteByImportId(Long importId);
}
