package sn.ods.obi.domain.model.dataset;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "TD_SAVED_QUERY")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "seq_saved_query", initialValue = 1, allocationSize = 1, sequenceName = "seq_saved_query")
public class SavedQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_saved_query")
    @Column(nullable = false, updatable = false)
    private Long id;

    @Size(max = 200)
    @Column(name = "query_nom", nullable = false)
    private String nom;

    @Column(name = "query_sql", columnDefinition = "TEXT")
    private String sql;

    @Column(name = "connexion_id")
    private Long connexionId;

    @Column(name = "tenant_id")
    private Long tenantId;
}
