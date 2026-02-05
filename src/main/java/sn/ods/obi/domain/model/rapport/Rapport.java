package sn.ods.obi.domain.model.rapport;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "TD_RAPPORT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "seq_rapport", initialValue = 1, allocationSize = 1, sequenceName = "seq_rapport")
public class Rapport {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_rapport")
    @Column(nullable = false, updatable = false)
    private Long id;

    @Size(max = 200)
    @Column(name = "rapport_nom", nullable = false)
    private String nom;

    @Size(max = 50)
    @Column(name = "rapport_type")
    private String type = "Tableau";

    @Size(max = 100)
    @Column(name = "rapport_dossier")
    private String dossier;

    @Column(name = "dataset_id")
    private Long datasetId;

    @Column(name = "tenant_id")
    private Long tenantId;

    /** Configuration de visualisation (axes, source, etc.) en JSON */
    @Column(name = "config_json", columnDefinition = "TEXT")
    private String configJson;
}
