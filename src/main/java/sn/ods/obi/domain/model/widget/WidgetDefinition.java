package sn.ods.obi.domain.model.widget;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "TD_WIDGET_DEFINITION")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "seq_widget_def", initialValue = 1, allocationSize = 1, sequenceName = "seq_widget_def")
public class WidgetDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_widget_def")
    @Column(nullable = false, updatable = false)
    private Long id;

    @Size(max = 200)
    @Column(name = "wd_name", nullable = false)
    private String name;

    @Size(max = 50)
    @Column(name = "wd_widget_type", nullable = false)
    private String widgetType;

    @Column(name = "wd_data_source_id")
    private String dataSourceId;

    @Size(max = 50)
    @Column(name = "wd_data_source_type")
    private String dataSourceType;

    @Size(max = 200)
    @Column(name = "wd_data_source_label")
    private String dataSourceLabel;

    /** axes: { xAxisColumn, yAxisColumns, valueColumn, ... } */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "wd_axes", columnDefinition = "jsonb")
    private Map<String, Object> axes;

    @Size(max = 200)
    @Column(name = "wd_title")
    private String title;

    @Size(max = 100)
    @Column(name = "wd_directory")
    private String directory;

    /** config: KpiConfig, ChartConfig, SegmentConfig, etc. */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "wd_config", columnDefinition = "jsonb")
    private Map<String, Object> config;

    /** filterSegments: [{ dimension, label }] */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "wd_filter_segments", columnDefinition = "jsonb")
    private List<Map<String, String>> filterSegments;

    @Column(name = "wd_default_cols")
    private Integer defaultCols;

    @Column(name = "wd_default_rows")
    private Integer defaultRows;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "wd_created_at")
    private LocalDateTime createdAt;

    @Column(name = "wd_updated_at")
    private LocalDateTime updatedAt;
}
