package sn.ods.obi.infrastructure.mails;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sn.ods.obi.presentation.dto.responses.mails.MailInfosDTO;


@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class mailController {
    private final MailService mailService;
    @PostMapping("/sentMailWithPJ/{fileName:.+}")
    @Operation(description = "Endpoint d'envoi de mail avec Piece jointe.'")
    public void getOneMutation(@RequestBody MailInfosDTO mailInfosDTO, @PathVariable String fileName){
         mailService.sendMailWithPJ(mailInfosDTO,fileName);
    }
}
