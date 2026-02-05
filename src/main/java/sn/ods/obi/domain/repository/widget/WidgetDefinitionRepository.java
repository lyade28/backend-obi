package sn.ods.obi.domain.repository.widget;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.ods.obi.domain.model.widget.WidgetDefinition;

import java.util.List;

@Repository
public interface WidgetDefinitionRepository extends JpaRepository<WidgetDefinition, Long> {
    List<WidgetDefinition> findByTenantId(Long tenantId);
}
