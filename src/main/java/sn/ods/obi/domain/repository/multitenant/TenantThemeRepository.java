package sn.ods.obi.domain.repository.multitenant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.ods.obi.domain.model.multitenant.TenantTheme;

import java.util.Optional;

@Repository
public interface TenantThemeRepository extends JpaRepository<TenantTheme, Long> {

    Optional<TenantTheme> findByTenantId(Long tenantId);
}
