package sn.ods.obi.domain.model.multitenant;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TD_QUOTA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Quota {

    @Id
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @Column(name = "quota_stockage_max")
    private Long stockageMax = 10L * 1024 * 1024 * 1024; // 10 Go

    @Column(name = "quota_stockage_utilise")
    private Long stockageUtilise = 0L;

    @Column(name = "quota_connexions_max")
    private Integer connexionsMax = 5;

    @Column(name = "quota_connexions_utilise")
    private Integer connexionsUtilise = 0;

    @Column(name = "quota_utilisateurs_max")
    private Integer utilisateursMax = 50;

    @Column(name = "quota_utilisateurs_utilise")
    private Integer utilisateursUtilise = 0;
}
