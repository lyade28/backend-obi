package sn.ods.obi.domain.model.multitenant;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "TD_TENANT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "seq_tenant", initialValue = 1, allocationSize = 1, sequenceName = "seq_tenant")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tenant")
    @Column(nullable = false, updatable = false)
    private Long id;

    @Size(max = 100)
    @Column(name = "tenant_nom", nullable = false)
    private String nom;

    @Size(max = 20)
    @Column(name = "tenant_statut", length = 20)
    private String statut = "Actif";
}
