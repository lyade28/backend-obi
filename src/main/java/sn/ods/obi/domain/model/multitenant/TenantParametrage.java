package sn.ods.obi.domain.model.multitenant;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "TD_TENANT_PARAMETRAGE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TenantParametrage {

    @Id
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @Size(max = 50)
    @Column(name = "param_theme")
    private String theme = "Défaut";

    @Size(max = 50)
    @Column(name = "param_fuseau")
    private String fuseau = "Europe/Paris";

    @Size(max = 10)
    @Column(name = "param_devise")
    private String devise = "EUR";

    @Size(max = 500)
    @Column(name = "param_logo")
    private String logo;
}
