package sn.ods.obi.domain.repository.utilisateur;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.ods.obi.domain.model.utilisateur.Utilisateur;


import java.util.List;
import java.util.Optional;



public interface UtilisateurRepository
        extends JpaRepository<Utilisateur, Long>, QuerydslPredicateExecutor<Utilisateur> {
    Optional<Utilisateur> findUtilisateurByEmail(String username);
    Optional<Utilisateur> findByTelephone(String telephone);


    @Query("SELECT u FROM Utilisateur u JOIN u.profiles p WHERE p.code IN :profileCodes")
    List<Utilisateur> findByProfileCodes(List<String> profileCodes);

    Utilisateur findByEmail(String email);

    long countByOrganisationId(Long organisationId);

    List<Utilisateur> findByOrganisationIdIn(List<Long> organisationIds);

}
