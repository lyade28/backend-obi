package sn.ods.obi.presentation.dto.multitenant;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganisationDTO {
    private Long id;
    private String nom;
    private String type;
    private String statut;
    private Long tenantId;
    private Integer utilisateurs;
}
