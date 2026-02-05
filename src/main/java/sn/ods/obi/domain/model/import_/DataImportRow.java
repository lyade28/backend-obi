package sn.ods.obi.domain.model.import_;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "TD_DATA_IMPORT_ROW", indexes = {
    @Index(name = "idx_import_row_import_id", columnList = "import_id")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "seq_import_row", initialValue = 1, allocationSize = 50, sequenceName = "seq_import_row")
public class DataImportRow {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_import_row")
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(name = "import_id", nullable = false)
    private Long importId;

    @Column(name = "row_index")
    private Integer rowIndex;

    /** Données de la ligne : {nomColonne: valeur} */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "row_data", columnDefinition = "jsonb")
    private Map<String, Object> data;
}
