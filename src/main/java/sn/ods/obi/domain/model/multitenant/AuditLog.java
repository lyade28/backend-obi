package sn.ods.obi.domain.model.multitenant;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TD_AUDIT_LOG")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "seq_audit", initialValue = 1, allocationSize = 1, sequenceName = "seq_audit")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_audit")
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(name = "audit_date")
    private LocalDateTime date;

    @Size(max = 100)
    @Column(name = "audit_action")
    private String action;

    @Size(max = 100)
    @Column(name = "audit_utilisateur")
    private String utilisateur;

    @Size(max = 500)
    @Column(name = "audit_detail")
    private String detail;

    @Column(name = "org_id")
    private Long organisationId;

    @Column(name = "tenant_id")
    private Long tenantId;
}
