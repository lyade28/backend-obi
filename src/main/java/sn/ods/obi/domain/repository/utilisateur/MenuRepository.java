package sn.ods.obi.domain.repository.utilisateur;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.ods.obi.domain.model.utilisateur.Menu;
import sn.ods.obi.domain.model.utilisateur.Profile;

import java.util.Set;


/**
 * @author G2k R&D
 */

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
   // ProfileMenuSousMenu findProfileMenuSousMenuByProfileIdAndMenId(Long profileId, Long menuId);

    Set<Menu> findByProfiles(Profile profile);
}
