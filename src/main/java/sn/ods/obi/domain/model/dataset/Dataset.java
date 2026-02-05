package sn.ods.obi.domain.model.dataset;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "TD_DATASET")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "seq_dataset", initialValue = 1, allocationSize = 1, sequenceName = "seq_dataset")
public class Dataset {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_dataset")
    @Column(nullable = false, updatable = false)
    private Long id;

    @Size(max = 200)
    @Column(name = "dataset_nom", nullable = false)
    private String nom;

    @Size(max = 500)
    @Column(name = "dataset_description")
    private String description;

    @Column(name = "source_type")
    private String sourceType;

    @Column(name = "source_label")
    private String sourceLabel;

    @Column(name = "query_id")
    private Long queryId;

    @Column(name = "connexion_id")
    private Long connexionId;

    @Column(name = "import_id")
    private Long importId;

    @Column(name = "tenant_id")
    private Long tenantId;
}
