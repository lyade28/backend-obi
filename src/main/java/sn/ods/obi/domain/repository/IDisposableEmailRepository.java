package sn.ods.obi.domain.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.ods.obi.domain.model.other.DisposableEmail;


import java.util.Optional;

/**
 * @author G2k R&D
 */

@Repository
public interface IDisposableEmailRepository extends JpaRepository<DisposableEmail,Long> {
    Optional<DisposableEmail> findByDomain(String domain);
}
