package sn.ods.obi.presentation.dto.responses.utilisateur;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sn.ods.obi.presentation.dto.requests.utilisateur.UserReqForAdminDTO;
import sn.ods.obi.presentation.dto.requests.utilisateur.UserReqForUserDTO;


@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserResForUserDTO extends UserReqForUserDTO {

    private Long id;
}
