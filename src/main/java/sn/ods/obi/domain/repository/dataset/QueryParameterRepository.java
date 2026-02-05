package sn.ods.obi.domain.repository.dataset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.ods.obi.domain.model.dataset.QueryParameter;

import java.util.List;

@Repository
public interface QueryParameterRepository extends JpaRepository<QueryParameter, Long> {
    List<QueryParameter> findByQueryId(Long queryId);
}
