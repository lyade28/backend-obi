package sn.ods.obi.presentation.dto.multitenant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurCreateUpdateDTO {
    private String nom;
    private String email;
    private String role;
    private Long organisationId;
}
