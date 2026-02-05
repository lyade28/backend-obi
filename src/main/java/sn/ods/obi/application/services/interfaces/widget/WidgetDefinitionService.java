package sn.ods.obi.application.services.interfaces.widget;

import sn.ods.obi.presentation.dto.widget.WidgetDefinitionDTO;

import java.util.List;

public interface WidgetDefinitionService {
    List<WidgetDefinitionDTO> getWidgetDefinitions(Long tenantId);
    WidgetDefinitionDTO getById(Long id);
    WidgetDefinitionDTO create(WidgetDefinitionDTO dto);
    WidgetDefinitionDTO update(Long id, WidgetDefinitionDTO dto);
    void delete(Long id);
}
