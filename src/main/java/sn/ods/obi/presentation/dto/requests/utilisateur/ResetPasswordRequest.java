package sn.ods.obi.presentation.dto.requests.utilisateur;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetPasswordRequest {
    private String token;
    private String newPassword;
}
