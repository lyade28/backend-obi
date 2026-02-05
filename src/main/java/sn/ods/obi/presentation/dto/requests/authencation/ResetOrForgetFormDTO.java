package sn.ods.obi.presentation.dto.requests.authencation;

import jakarta.validation.constraints.NotBlank;

import static sn.ods.obi.infrastructure.config.utils.i18n.I18nKeys.*;


public record ResetOrForgetFormDTO(
        @NotBlank(message = EMAIL_OBLIGATOIRE)
        String login,
        @NotBlank(message = CONNEXION_LOGIN_PASSWORD_NON_VIDE)
        String password,
        @NotBlank(message = CONNEXION_LOGIN_NEW_PASSWORD_NON_VIDE)
        String newPassword,
        String passwordConfirmed) {


}
