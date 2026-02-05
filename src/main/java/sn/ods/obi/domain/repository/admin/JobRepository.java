package sn.ods.obi.domain.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.ods.obi.domain.model.admin.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
}
