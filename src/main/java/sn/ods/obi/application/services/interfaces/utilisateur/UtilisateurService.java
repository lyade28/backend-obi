package sn.ods.obi.application.services.interfaces.utilisateur;




import sn.ods.obi.domain.model.utilisateur.Utilisateur;
import sn.ods.obi.presentation.dto.requests.utilisateur.UserReqForAdminDTO;
import sn.ods.obi.presentation.dto.requests.utilisateur.UserReqForUserDTO;
import sn.ods.obi.presentation.dto.responses.Response;

import java.util.Map;

/**
 * @author Abdou Karim CISSOKHO
 * @created 07/01/2025-13:01
 * @project obi
 */

public interface UtilisateurService {


    Utilisateur createUserFromAdmin(UserReqForAdminDTO dto);
    Utilisateur createUserFromUser(UserReqForUserDTO dto);
    Utilisateur updateUser(Long id, UserReqForAdminDTO dto);
    Utilisateur getUser(Long id);

    Response<Object> getUserPage(int page, int size, String filter);

    void activation(Map<String, String> activation);

}