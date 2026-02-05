package sn.ods.obi.presentation.dto.requests.utilisateur;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForgotPasswordRequest {
   private String email;
}
