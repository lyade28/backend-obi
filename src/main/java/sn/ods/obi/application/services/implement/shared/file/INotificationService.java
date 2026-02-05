package sn.ods.obi.application.services.implement.shared.file;


import sn.ods.obi.domain.model.utilisateur.Utilisateur;
import sn.ods.obi.domain.model.utilisateur.ValidationUser;
import sn.ods.obi.presentation.dto.requests.authencation.LoginFormDTO;
import sn.ods.obi.presentation.dto.responses.mails.MailInfosDTO;

public interface INotificationService {
   void sendNotificationToNewUserRegistred(LoginFormDTO loginFormDTO, String action);

    void sendNotificationToNewUserRegistredByAdmin(LoginFormDTO loginFormDTO, String action);
    void sendNotificationToUserEdited(LoginFormDTO loginFormDTO, String action);
    void sendNotificationToUserForgetPassword(LoginFormDTO loginFormDTO, String action);
    void sendEmail(MailInfosDTO mailInfosDTO);
    void sendNotificationStatut(Utilisateur utilisateur);

    void envoyer(ValidationUser validationUser);
}
