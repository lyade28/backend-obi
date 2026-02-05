package sn.ods.obi.presentation.dto.multitenant;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private String nom;
    private String droits;
}
