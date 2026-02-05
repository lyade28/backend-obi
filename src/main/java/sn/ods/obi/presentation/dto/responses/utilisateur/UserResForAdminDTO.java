package sn.ods.obi.presentation.dto.responses.utilisateur;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import sn.ods.obi.presentation.dto.requests.utilisateur.UserReqForAdminDTO;


@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserResForAdminDTO extends UserReqForAdminDTO {

    private Long id;
}
