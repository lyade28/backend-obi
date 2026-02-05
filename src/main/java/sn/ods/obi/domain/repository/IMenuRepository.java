package sn.ods.obi.domain.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.ods.obi.domain.model.utilisateur.Menu;


/**
 * @author G2k R&D
 */

@Repository
public interface IMenuRepository extends JpaRepository<Menu, Long> {
   // ProfileMenuSousMenu findProfileMenuSousMenuByProfileIdAndMenId(Long profileId, Long menuId);
}
