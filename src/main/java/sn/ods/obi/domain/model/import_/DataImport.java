package sn.ods.obi.domain.model.import_;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "TD_DATA_IMPORT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "seq_data_import", initialValue = 1, allocationSize = 1, sequenceName = "seq_data_import")
public class DataImport {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_data_import")
    @Column(nullable = false, updatable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "import_nom_fichier", nullable = false)
    private String nomFichier;

    @Size(max = 50)
    @Column(name = "import_type")
    private String typeFichier; // excel, csv

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "import_row_count")
    private Integer rowCount;

    /** Colonnes détectées : [{nom, type}, ...] */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "import_columns", columnDefinition = "jsonb")
    private List<Map<String, String>> columns;

    @Column(name = "import_created_at")
    private LocalDateTime createdAt;
}
