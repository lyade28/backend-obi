package sn.ods.obi.infrastructure.Notification;


import sn.ods.obi.infrastructure.Notification.model.Notification;
import sn.ods.obi.presentation.dto.responses.Response;

public interface INotification {
    Response<Object> notifyUser(Notification notification);
    Response<Object> getNotifiesByUser(int page, int pageSize,Long idUser,  String codeProfile);
    Response<Object> notifyIsRead(Long idNotification);
    Response<Object> getListNotifiesByUser(Long idUser, String codeProfile);
}
