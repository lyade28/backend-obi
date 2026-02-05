package sn.ods.obi.application.services.implement.widget;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ods.obi.application.services.interfaces.widget.WidgetDefinitionService;
import sn.ods.obi.domain.model.widget.WidgetDefinition;
import sn.ods.obi.domain.repository.widget.WidgetDefinitionRepository;
import sn.ods.obi.presentation.dto.widget.WidgetDefinitionDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WidgetDefinitionServiceImpl implements WidgetDefinitionService {

    private final WidgetDefinitionRepository repository;

    @Override
    public List<WidgetDefinitionDTO> getWidgetDefinitions(Long tenantId) {
        return repository.findByTenantId(tenantId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public WidgetDefinitionDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Widget definition non trouvée"));
    }

    @Override
    @Transactional
    public WidgetDefinitionDTO create(WidgetDefinitionDTO dto) {
        WidgetDefinition w = toEntity(dto);
        w.setCreatedAt(LocalDateTime.now());
        w.setUpdatedAt(LocalDateTime.now());
        w = repository.save(w);
        return toDTO(w);
    }

    @Override
    @Transactional
    public WidgetDefinitionDTO update(Long id, WidgetDefinitionDTO dto) {
        WidgetDefinition w = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Widget definition non trouvée"));
        if (dto.getName() != null) w.setName(dto.getName());
        if (dto.getWidgetType() != null) w.setWidgetType(dto.getWidgetType());
        if (dto.getDataSourceId() != null) w.setDataSourceId(dto.getDataSourceId());
        if (dto.getDataSourceType() != null) w.setDataSourceType(dto.getDataSourceType());
        if (dto.getDataSourceLabel() != null) w.setDataSourceLabel(dto.getDataSourceLabel());
        if (dto.getAxes() != null) w.setAxes(dto.getAxes());
        if (dto.getTitle() != null) w.setTitle(dto.getTitle());
        if (dto.getDirectory() != null) w.setDirectory(dto.getDirectory());
        if (dto.getConfig() != null) w.setConfig(dto.getConfig());
        if (dto.getFilterSegments() != null) w.setFilterSegments(dto.getFilterSegments());
        if (dto.getDefaultCols() != null) w.setDefaultCols(dto.getDefaultCols());
        if (dto.getDefaultRows() != null) w.setDefaultRows(dto.getDefaultRows());
        w.setUpdatedAt(LocalDateTime.now());
        w = repository.save(w);
        return toDTO(w);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private WidgetDefinition toEntity(WidgetDefinitionDTO dto) {
        return WidgetDefinition.builder()
                .name(dto.getName())
                .widgetType(dto.getWidgetType())
                .dataSourceId(dto.getDataSourceId())
                .dataSourceType(dto.getDataSourceType())
                .dataSourceLabel(dto.getDataSourceLabel())
                .axes(dto.getAxes())
                .title(dto.getTitle())
                .directory(dto.getDirectory() != null ? dto.getDirectory().trim() : null)
                .config(dto.getConfig())
                .filterSegments(dto.getFilterSegments())
                .defaultCols(dto.getDefaultCols())
                .defaultRows(dto.getDefaultRows())
                .tenantId(dto.getTenantId())
                .build();
    }

    private WidgetDefinitionDTO toDTO(WidgetDefinition w) {
        DateTimeFormatter fmt = DateTimeFormatter.ISO_DATE_TIME;
        return WidgetDefinitionDTO.builder()
                .id(w.getId())
                .name(w.getName())
                .widgetType(w.getWidgetType())
                .dataSourceId(w.getDataSourceId())
                .dataSourceType(w.getDataSourceType())
                .dataSourceLabel(w.getDataSourceLabel())
                .axes(w.getAxes())
                .title(w.getTitle())
                .directory(w.getDirectory())
                .config(w.getConfig())
                .filterSegments(w.getFilterSegments())
                .defaultCols(w.getDefaultCols())
                .defaultRows(w.getDefaultRows())
                .tenantId(w.getTenantId())
                .createdAt(w.getCreatedAt() != null ? w.getCreatedAt().format(fmt) : null)
                .updatedAt(w.getUpdatedAt() != null ? w.getUpdatedAt().format(fmt) : null)
                .build();
    }
}
