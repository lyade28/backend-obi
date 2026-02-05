package sn.ods.obi.domain.model.dashboard;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "TD_DASHBOARD")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "seq_dashboard", initialValue = 1, allocationSize = 1, sequenceName = "seq_dashboard")
public class Dashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_dashboard")
    @Column(nullable = false, updatable = false)
    private Long id;

    @Size(max = 200)
    @Column(name = "dashboard_nom", nullable = false)
    private String nom;

    @Size(max = 500)
    @Column(name = "dashboard_description")
    private String description;

    @Column(name = "dashboard_layout", columnDefinition = "TEXT")
    private String layoutJson;

    @Column(name = "tenant_id")
    private Long tenantId;
}
