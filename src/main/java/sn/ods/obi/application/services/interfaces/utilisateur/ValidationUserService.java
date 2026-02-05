package sn.ods.obi.application.services.interfaces.utilisateur;

import sn.ods.obi.domain.model.utilisateur.Utilisateur;
import sn.ods.obi.domain.model.utilisateur.ValidationUser;

/**
 * @author Abdou Karim CISSOKHO
 * @created 17/01/2025-11:48
 * @project obi
 */
public interface ValidationUserService {

    void validateUser(Utilisateur user);

    ValidationUser readCode(String code);
}
