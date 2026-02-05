
package sn.ods.obi.infrastructure.mails;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sn.ods.obi.domain.model.other.FailedMail;
import sn.ods.obi.domain.repository.utilisateur.FailedMailRepository;
import sn.ods.obi.presentation.dto.responses.mails.MailInfosDTO;


import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

@Service
@RequiredArgsConstructor
class MailScheduler {
    @Value("${smtp.server.host}")
    private String mailHost;
    private static final Logger LOGGER = LoggerFactory.getLogger(MailScheduler.class);
    private final FailedMailRepository failedMailRepository;
   // private final INotificationService notificationService;

    @Scheduled(fixedDelayString = "${interval}")
    @Async
    public void retryToSendFailedEmails() {
        try {
            if (Boolean.FALSE.equals(sendPingRequest())) {
                LOGGER.info("Désolé ! Nous n'arrivons pas à joindre l'hôte {}", mailHost);
            } else {
                LOGGER.info("L'hôte {} est joignable !", mailHost);
                List<FailedMail> failedMails = failedMailRepository.findAll();

                if (!failedMails.isEmpty()) {
                    LOGGER.info("Nouvelle tentative d'envoi des mails précédemment échoués !");
                    failedMails.forEach(failedMail -> {
                        try {
                            MailInfosDTO mailInfosDTO = new MailInfosDTO(failedMail.getId(), failedMail.getText(), failedMail.getSubject(), null, failedMail.getEmail());
                           // notificationService.sendEmail(mailInfosDTO);
                        } catch (Exception e) {
                            LOGGER.error("Erreur in scheduler config: {}", e.getMessage());
                        }
                    });
                }
            }

        } catch (Exception e) {
            LOGGER.error("Exception lors de l'envoi des mails => {}", e.getMessage());
        }

    }

    private Boolean sendPingRequest() throws IOException {
        InetAddress inetAddress = InetAddress.getByName(mailHost);
        LOGGER.info("Envoi d'une requête Ping à {}", mailHost);
        return inetAddress.isReachable(5000);
    }


}
