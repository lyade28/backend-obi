package sn.ods.obi.presentation.dto.widget;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WidgetDefinitionDTO {
    private Long id;
    private String name;
    private String widgetType;
    private String dataSourceId;
    private String dataSourceType;
    private String dataSourceLabel;
    private Map<String, Object> axes;
    private String title;
    private String directory;
    private Map<String, Object> config;
    private List<Map<String, String>> filterSegments;
    private Integer defaultCols;
    private Integer defaultRows;
    private Long tenantId;
    private String createdAt;
    private String updatedAt;
}
