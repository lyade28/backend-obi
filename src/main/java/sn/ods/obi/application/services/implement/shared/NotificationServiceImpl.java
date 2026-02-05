package sn.ods.obi.application.services.implement.shared;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sn.ods.obi.application.services.implement.shared.file.INotificationService;
import sn.ods.obi.domain.model.utilisateur.Utilisateur;
import sn.ods.obi.domain.model.utilisateur.ValidationUser;
import sn.ods.obi.infrastructure.config.security.jwt.JwtProvider;
import sn.ods.obi.infrastructure.mails.MailService;
import sn.ods.obi.presentation.dto.requests.authencation.LoginFormDTO;
import sn.ods.obi.presentation.dto.responses.mails.MailConnexionInfosDTO;
import sn.ods.obi.presentation.dto.responses.mails.MailInfosDTO;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService {
    private static final String RESET_PASSWORD_LINK = "reset-password";
    private static final String FORGET_PASSWORD_LINK = "forgot-password";
    private static final String TOKEN_NAME_IN_MAIL_NOTIFICATION = "token";
    private static final String INSCRIPTION = "Inscription";
    private static final String Statut = "Activation/Désactivation compte";
    private static final String CREATION_COMPTE = "Création compte";
    private static final String MODIFICATION_COMPTE = "Modification compte";
    private static final String REINITIALISATION_MOT_DE_PASSE = "Réinitialisation mot de passe";
    private static final String CODE_DE_ACTIVATION = "Votre code de activation";
    private static final String NOM_ORGANISATION = "ODS StarterKit V1";
    private final JwtProvider jwtProvider;
    private final MailService mailService;
    private static final String SUIVI_PERMUTATION = "Suivi demande de permutation";

    @Value("${app.url.front}")
    private String appUrlFront;

    @Value("${url.logo.obi}")
    private String urlLogoObi;

    @Override
    public void sendNotificationToNewUserRegistred(LoginFormDTO loginFormDTO, String action) {
        String lien = getLinkWithToken(loginFormDTO, action);
        String textMessage = """
                Bienvenue dans la plateforme STARTER KIT ODS <span>....</span>.\s
                Merci de cliquer <a href=%s style="color: #3498DB;">ici</a> pour activer votre compte.
               ODS, vous remercie de votre confiance.
                """.formatted(lien);

        MailInfosDTO mailInfosDTO = new MailInfosDTO(null, textMessage, INSCRIPTION, null, loginFormDTO.login());
        sendEmail(mailInfosDTO);
    }


    public void sendNotificationStatut(Utilisateur utilisateur) {
        System.out.println("##############");
        String textMessage = """
               Bonjour votre compte viens d'être %s  <span>....</span>.\s
            
                """.formatted(utilisateur.getStatus());

        MailInfosDTO mailInfosDTO = new MailInfosDTO(null, textMessage, Statut, null, utilisateur.getEmail());
        sendEmail(mailInfosDTO);
    }



    @Override
    public void sendNotificationToNewUserRegistredByAdmin(LoginFormDTO loginFormDTO, String action) {
        String lien = getLinkWithToken(loginFormDTO, action);
        String textMessage = """
                Votre compte utilisateur a été créé.
                Merci de cliquer <a href=%s style="color: #3498DB;">ici</a> pour activer votre compte.
                """.formatted(lien);
        MailInfosDTO mailInfosDTO = new MailInfosDTO(null, textMessage, CREATION_COMPTE, null, loginFormDTO.login());
        sendEmail(mailInfosDTO);
    }

    @Override
    public void sendNotificationToUserEdited(LoginFormDTO loginFormDTO, String action) {
        String lien = getLinkWithToken(loginFormDTO, action);
        String textMessage = """
                Votre compte utilisateur a été modifié.
                Merci de cliquer <a href=%s style="color: #3498DB;">ici</a> pour l'activer à nouveau.
                """.formatted(lien);

        MailInfosDTO mailInfosDTO = new MailInfosDTO(null, textMessage, MODIFICATION_COMPTE, null, loginFormDTO.login());
        sendEmail(mailInfosDTO);
    }

    @Override
    public void sendNotificationToUserForgetPassword(LoginFormDTO loginFormDTO, String action) {
        String lien = getLinkWithToken(loginFormDTO, action);
        String textMessage = """
                Votre mot de passe a été réinitialisé avec succès.
                Merci de cliquer <a href=%s style="color: #3498DB;">ici</a> pour choisir un nouveau mot de passe.
                """.formatted(lien);

        MailInfosDTO mailInfosDTO = new MailInfosDTO(null, textMessage, REINITIALISATION_MOT_DE_PASSE, null, loginFormDTO.login());
        sendEmail(mailInfosDTO);
    }

    @Override
    public void sendEmail(MailInfosDTO mailInfosDTO) {
        System.out.println("### send mail fonction");
        MailInfosDTO mailInfos = new MailInfosDTO(mailInfosDTO.id(), mailInfosDTO.originalText(), mailInfosDTO.subject(), getHtmlMessage(mailInfosDTO.originalText(), urlLogoObi,NOM_ORGANISATION), mailInfosDTO.destinataire());


        mailService.sendMail(mailInfos);
    }


    @Override
    public void envoyer(ValidationUser validationUser) {

        String fullName = validationUser.getUser().getPrenom() + " " + validationUser.getUser().getNom();

        String textMessage = """
               Bonjour %s, Votre code d'activation est  <span  style="color: #3498DB;"> %s;</span> <br/> .
                A bientôt
                """.formatted(fullName , validationUser.getCode());

        MailInfosDTO mailInfosDTO = new MailInfosDTO(null, textMessage, CODE_DE_ACTIVATION, null, validationUser.getUser().getEmail());
        sendEmail(mailInfosDTO);



        MailInfosDTO mailInfos = new MailInfosDTO(mailInfosDTO.id(), mailInfosDTO.originalText(), mailInfosDTO.subject(), getHtmlMessage(mailInfosDTO.originalText(), urlLogoObi,NOM_ORGANISATION), mailInfosDTO.destinataire());


        mailService.sendMail(mailInfos);
    }
    
    private String getLinkWithToken(LoginFormDTO loginFormDTO, String action) {
        MailConnexionInfosDTO infos = new MailConnexionInfosDTO(loginFormDTO.login(), loginFormDTO.password(), action);
        String mailToken = jwtProvider.generateJwtMailToken(infos);
        return appUrlFront + "/" + RESET_PASSWORD_LINK + "?" + TOKEN_NAME_IN_MAIL_NOTIFICATION + "=" + mailToken;
    }

    private String getHtmlMessage(String text, String logo,String nom_organisation) {

        return """
                <!doctype html>
                  <html lang="fr">
                      <head>
                          <title> Hello world </title>
                          <meta http-equiv="X-UA-Compatible" content="IE=edge">
                          <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                          <meta name="viewport" content="width=device-width, initial-scale=1">
                      </head>
                      <body style="word-spacing:normal;">
                          <p>Bonjour,</p></br>
                          %s </br></br></br>
                          <img alt height="auto" width='auto' src="%s" style="border:0;display:block;outline:none;text-decoration:none;font-size:13px;  display: block; margin-left: auto;margin-right: auto;" /></br></br>
                          <div style="font-family:Roboto, Helvetica, sans-serif;font-size:18px;font-weight:500;line-height:24px;text-align:center;color:blue;">%s</div>
                      </body>
                  </html>
                  """.formatted(text, logo, nom_organisation);
    }
}
