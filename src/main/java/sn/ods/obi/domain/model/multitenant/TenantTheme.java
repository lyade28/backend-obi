package sn.ods.obi.domain.model.multitenant;

import jakarta.persistence.*;
import lombok.*;

/**
 * Thème de personnalisation par tenant (couleurs, logo).
 * Le contenu complet est stocké en JSON (themeJson).
 * Clé primaire auto-générée pour éviter les conflits insert/merge avec @MapsId.
 */
@Entity
@Table(name = "TD_TENANT_THEME", uniqueConstraints = @UniqueConstraint(columnNames = "tenant_id"))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TenantTheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(name = "tenant_id", nullable = false, unique = true)
    private Long tenantId;

    /** Configuration complète du thème en JSON (primary, secondary, chartColors, logoUrl, etc.) */
    @Column(name = "theme_json", columnDefinition = "TEXT")
    private String themeJson;
}
