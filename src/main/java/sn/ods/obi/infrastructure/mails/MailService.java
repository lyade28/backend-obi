
package sn.ods.obi.infrastructure.mails;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import sn.ods.obi.domain.model.other.FailedMail;
import sn.ods.obi.domain.repository.utilisateur.FailedMailRepository;
import sn.ods.obi.presentation.dto.responses.mails.MailInfosDTO;


import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;

/**
 * @author G2k R&D
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    @Value("${smtp.server.host}")
    private String mailHost;

    @Value("${smtp.server.password}")
    private String password;


    @Value("${smtp.server.from}")
    private String from;
    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);
    private final FailedMailRepository failedMailRepository;
    @Value("${upload.path}")
    private String uploadDirectory;

    @Transactional
    @Async
    public void sendASynchronousMail(MailInfosDTO mailInfosDTO) {


        LOGGER.debug("Envoi mail en cours d'initialisation !");



        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", mailHost);


            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");


            // Authentification avec le mot de passe
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(mailInfosDTO.destinataire()));
            msg.setSubject(mailInfosDTO.subject());
            msg.setText(mailInfosDTO.text(), "utf-8", "html");
            Transport.send(msg);
            manageFailedMailInDatabase(mailInfosDTO, "DELETE");

        } catch (Exception e) {
            System.out.println("==> " + e.getMessage());
            LOGGER.info("Erreur lors de l'envoi du mail à {} !", mailInfosDTO.destinataire());
            manageFailedMailInDatabase(mailInfosDTO, "CREATE_OR_EDIT");
        }
    }

    @Transactional
    @Async
    public void sendMail(MailInfosDTO mailInfosDTO) {
        try {
            // Mimifier Message
            Properties props = new Properties();
            props.put("mail.smtp.host", mailHost);

            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");


            // Authentification avec le mot de passe
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });

           // Session session = Session.getInstance(props);

            MimeMessage message = new MimeMessage(session);

            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.setFrom(new InternetAddress(from));
            helper.setTo(new InternetAddress(mailInfosDTO.destinataire()));
            helper.setSubject(mailInfosDTO.subject());

            // Add context
            Context context = new Context();
            context.setVariable("content", mailInfosDTO.originalText());
            String html = templateEngine.process("email.html", context);
            message.setText(html, "utf-8", "html");
            helper.setText(html, true);

            Transport.send(message);

        } catch (Exception e) {
            System.out.println("error ================= " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    private void manageFailedMailInDatabase(MailInfosDTO mailInfosDTO, String action) {
        LOGGER.info("Mise à jour de la table FailedEmail en cours !");
        try {
            if (action.equalsIgnoreCase("CREATE_OR_EDIT")) {
                FailedMail failedMail = FailedMail.builder()
                        .email(mailInfosDTO.destinataire())
                        .subject(mailInfosDTO.subject())
                        .text(mailInfosDTO.originalText())
                        .createdDate(new Date())
                        .isSent(false)
                        .build();
                if (Objects.nonNull(mailInfosDTO.id()))
                    failedMail = failedMailRepository.findById(mailInfosDTO.id()).orElse(failedMail);

                failedMailRepository.save(failedMail);
            } else if (action.equalsIgnoreCase("DELETE") && Objects.nonNull(mailInfosDTO.id()))
                failedMailRepository.deleteById(mailInfosDTO.id());

        } catch (Exception exception) {
            LOGGER.error("Echec insertion dans la base de données: ");
        }
        LOGGER.info("La mise à jour a été faite avec succès ! => {}", mailInfosDTO.destinataire());
    }
    @Transactional
    @Async
    public void sendMailWithPJ(MailInfosDTO mailInfosDTO, String pathToAttachment) {
        try {
            // Créer un message MIME en utilisant JavaMailSender
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            // Configurer les détails de l'e-mail
            helper.setFrom(new InternetAddress(from));
            helper.setTo(new InternetAddress(mailInfosDTO.destinataire()));
            helper.setSubject(mailInfosDTO.subject());

            // Charger le fichier comme une ressource
            Path filePath = Paths.get(uploadDirectory).resolve(pathToAttachment).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            // Ajouter la pièce jointe
            helper.addAttachment(Objects.requireNonNull(resource.getFilename()), resource);

            // Ajouter le contexte et le contenu HTML
            Context context = new Context();
            context.setVariable("content", mailInfosDTO.originalText());
            context.setVariable("date",  LocalDateTime.now().getYear());
            String html = templateEngine.process("email.html", context);
            helper.setText(html, true);

            // Envoyer l'e-mail en utilisant JavaMailSender
            mailSender.send(message);

        } catch (Exception e) {
            System.out.println("error =================");
            System.out.println(e.getMessage());
        }
    }


    public void sendMailOS(MailInfosDTO mailInfos, String os) {
    }
}
