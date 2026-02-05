package sn.ods.obi.presentation.dto.responses.authentication;




import sn.ods.obi.domain.model.utilisateur.Profile;
import java.util.Set;


public record UtilisateurInfo(Long id, String email, String prenom, String nom, Set<Profile> profil, boolean status) {
}
