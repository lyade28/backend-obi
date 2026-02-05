package sn.ods.obi.presentation.dto.dashboard;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private Long id;
    private String nom;
    private String description;
    private String layoutJson;
    private Long tenantId;
}
