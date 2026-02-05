package sn.ods.obi.domain.repository.favori;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.ods.obi.domain.model.favori.Favori;

import java.util.List;

@Repository
public interface FavoriRepository extends JpaRepository<Favori, Long> {
    List<Favori> findByUserId(Long userId);
    void deleteByUserIdAndTypeAndEntityId(Long userId, String type, String entityId);
}
