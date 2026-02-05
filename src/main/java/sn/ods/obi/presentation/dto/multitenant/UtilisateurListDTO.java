package sn.ods.obi.presentation.dto.multitenant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la liste des utilisateurs par tenant (module Organisations).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurListDTO {
    private Long id;
    private String nom;
    private String email;
    private String role;
    private String organisationNom;
}
