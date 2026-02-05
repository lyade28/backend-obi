package sn.ods.obi.domain.repository.connexion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.ods.obi.domain.model.connexion.Connexion;

import java.util.List;

@Repository
public interface ConnexionRepository extends JpaRepository<Connexion, Long> {
    List<Connexion> findByTenantId(Long tenantId);
}
