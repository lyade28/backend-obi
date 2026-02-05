package sn.ods.obi.infrastructure.Notification;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import sn.ods.obi.infrastructure.Notification.model.Notification;
import sn.ods.obi.infrastructure.Notification.model.QNotification;
import sn.ods.obi.presentation.dto.responses.Response;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static sn.ods.obi.infrastructure.Notification.model.QNotification.notification;


@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService implements INotification{
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    @Override
    public Response<Object> notifyUser(Notification notification) {
        try {
            notification.setDate(LocalDateTime.now());
            notification.setRead(false);
            Notification notificationSaved = notificationRepository.save(notification);
            //mettre la notification sur le canal du webSocket : /topic/notifications
            messagingTemplate.convertAndSend("/topic/notifications", notification);
            return Response.ok().setPayload(notificationSaved).setMessage("notification envoyée avec succés");
        }catch (Exception e){
            return Response.exception().setMessage("Une erreur est survenue lors de l'envoie de la notificaation");
        }
    }
    @Override
    public Response<Object> notifyIsRead(Long idNotification) {
        try {

            Notification notificationSaved = notificationRepository.findById(idNotification).orElse(null);
            notificationSaved.setRead(true);
            notificationRepository.save(notificationSaved);
            return Response.ok().setPayload(notificationSaved).setMessage("notification lue avec succés");
        }catch (Exception e){
            return Response.exception().setMessage("Une erreur est survenue lors de la lecture de la notificaation");
        }
    }

    @Override
    public Response<Object> getNotifiesByUser(int page, int pageSize, Long idUser,  String codeProfile) {
        try {

            Page<Notification> notifications;
            QNotification qNotification = notification;
            BooleanBuilder conditions = new BooleanBuilder();

            conditions.and(qNotification.idUser.eq(idUser));
            //les IdUsers égalent à zéro sont considéré comme des notifications publiques
            conditions.or(qNotification.idUser.eq(0L));
            if(!Objects.equals(codeProfile, "")) {
                conditions.or(qNotification.codeProfile.eq(codeProfile));
            }
            conditions.and(qNotification.isRead.eq(false));
            notifications = Objects.nonNull(conditions.getValue())?notificationRepository
                    .findAll(conditions.getValue(), PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")))
                    :notificationRepository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));

            Response.PageMetadata pageMetadata = Response.PageMetadata.builder()
                    .size(notifications.getSize())
                    .totalPages(notifications.getTotalPages())
                .totalElements( notifications.getTotalElements())
                    .number(notifications.getNumber())
                    .build();
            //récupération des notifications non lues
            if (notifications.getTotalElements() > 0) {
                Long notReads = notificationRepository.notificationsNotRead(false);
                notifications.getContent().get(0).setNotReads(notReads);
            }
            return Response.ok().setPayload(notifications.getContent())
                    .setMetadata(pageMetadata).setMessage("liste des notifications");
        }catch (Exception e){
            return Response.exception().setMessage("Une erreur est survenue lors de la récupération des notifications");
        }
    }
    public Response<Object> getListNotifiesByUser(Long idUser, String codeProfile) {
        try {
            List<Notification> notifications;
            QNotification qNotification = notification;
            BooleanBuilder conditions = new BooleanBuilder();

            conditions.and(qNotification.idUser.eq(idUser));
            // les IdUsers égalent à zéro sont considérés comme des notifications publiques
            conditions.or(qNotification.idUser.eq(0L));
            if (!Objects.equals(codeProfile, "")) {
                conditions.or(qNotification.codeProfile.eq(codeProfile));
            }

            // Récupérer toutes les notifications sans pagination
            notifications = (List<Notification>) notificationRepository.findAll(conditions.getValue(), Sort.by(Sort.Direction.DESC, "id"));

            // Récupération des notifications non lues
            if (!notifications.isEmpty()) {
                Long notReads = notificationRepository.notificationsNotRead(false);
                notifications.get(0).setNotReads(notReads);
            }

            return Response.ok().setPayload(notifications)
                    .setMessage("liste des notifications");
        } catch (Exception e) {
            return Response.exception().setMessage("Une erreur est survenue lors de la récupération des notifications");
        }
    }

}
