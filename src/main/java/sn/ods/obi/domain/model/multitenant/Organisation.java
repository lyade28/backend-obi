package sn.ods.obi.domain.model.multitenant;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "TD_ORGANISATION")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "seq_org", initialValue = 1, allocationSize = 1, sequenceName = "seq_org")
public class Organisation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_org")
    @Column(nullable = false, updatable = false)
    private Long id;

    @Size(max = 100)
    @Column(name = "org_nom", nullable = false)
    private String nom;

    @Size(max = 50)
    @Column(name = "org_type", length = 50)
    private String type = "Siège";

    @Size(max = 20)
    @Column(name = "org_statut", length = 20)
    private String statut = "Actif";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;
}
