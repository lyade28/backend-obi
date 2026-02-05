package sn.ods.obi.domain.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.ods.obi.domain.model.other.FailedMail;



@Repository
public interface IFailedMailRepository extends JpaRepository<FailedMail,Long> {
}

