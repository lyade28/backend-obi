package sn.ods.obi.presentation.dto.requests.utilisateur;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.ods.obi.domain.model.utilisateur.Profile;

import java.time.LocalDate;
import java.util.Set;

/**
 * @author Abdou Karim CISSOKHO
 * @created 09/01/2025-15:45
 * @project obi
 */

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class UtilisateurReqDTO {
    protected String prenom;
    protected String nom;
    protected String email;
    protected String telephone;
    protected String adresse;
    protected String sexe;
    protected Set<Profile> profiles;
    protected String lieuDeNaissance;
    protected LocalDate dateNaissance;

}
