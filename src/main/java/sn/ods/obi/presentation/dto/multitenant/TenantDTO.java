package sn.ods.obi.presentation.dto.multitenant;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantDTO {
    private Long id;
    private String nom;
    private String statut;
    private Integer orgs;
    private Integer users;
}
