package sn.ods.obi.application.services.interfaces.authentication;

import org.springframework.http.ResponseEntity;
import sn.ods.obi.domain.model.utilisateur.Utilisateur;
import sn.ods.obi.presentation.dto.requests.authencation.ForgetFormDTO;
import sn.ods.obi.presentation.dto.requests.authencation.InitialAuthenticationDTO;
import sn.ods.obi.presentation.dto.requests.authencation.LoginFormDTO;
import sn.ods.obi.presentation.dto.requests.authencation.ResetOrForgetFormDTO;
import sn.ods.obi.presentation.dto.responses.APIResponse;
import sn.ods.obi.presentation.dto.responses.Response;
import sn.ods.obi.presentation.dto.responses.authentication.JwtDTO;

/**
 * @author Abdou Karim CISSOKHO
 * @created 09/01/2025-16:19
 * @project obi
 */
public interface AuthenticationService {


    JwtDTO singIn(LoginFormDTO loginFormDTO);
    JwtDTO refreshToken(String token);
    ResponseEntity<APIResponse> authenticateUserWithFirstUrlConnexion(InitialAuthenticationDTO formRequest);
    JwtDTO authenticateUserWithForgetPasswordUrlConnexion(ForgetFormDTO formRequest);
    Utilisateur reinitPassword(String login);
    //  Response<Object> editUserInfos(EditMonCompteDTO req);
    ResponseEntity<APIResponse> updatePasswordFromInterface(ResetOrForgetFormDTO form);

    Utilisateur getCurrentConnectedUser();
}
