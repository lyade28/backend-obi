package sn.ods.obi.domain.repository.utilisateur;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import sn.ods.obi.domain.model.utilisateur.Profile;


import java.util.Optional;



@Repository
public interface ProfilRepository extends JpaRepository<Profile, Long>, QuerydslPredicateExecutor<Profile> {

    Optional<Profile> findByCode(String code);


}
