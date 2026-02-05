package sn.ods.obi.infrastructure.Notification;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sn.ods.obi.infrastructure.Notification.model.Notification;
import sn.ods.obi.presentation.dto.responses.Response;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final INotification iNotification ;

    @PostMapping(path = {"/add"})
    @Operation(description = "Endpoint de création de notification")
    public Response<Object> createNotification(@RequestBody Notification notification) {

        return iNotification.notifyUser(notification);
    }

    @GetMapping(path = {"/list/{idUser}"})
    @Operation(description = "Endpoint de recupération de notification")
    public Response<Object> getAllNotifications(@PathVariable Long idUser,
                                                @RequestParam (defaultValue = "")String codeProfile,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        return iNotification.getNotifiesByUser(page, pageSize, idUser, codeProfile);
    }
    @GetMapping(path = {"/listGlobal/{idUser}"})
    @Operation(description = "Endpoint de recupération de notification")
    public Response<Object> getListNotifications(@PathVariable Long idUser,
                                                @RequestParam (defaultValue = "")String codeProfile
                                               ) {
        return iNotification.getListNotifiesByUser(idUser, codeProfile);
    }

    @PatchMapping(path = {"/read/{idnotification}"})
    @Operation(description = "Lecture d'une notification")
    public Response<Object> readNotification(@PathVariable Long idnotification) {

        return iNotification.notifyIsRead(idnotification);
    }
}
