package sn.ods.obi.presentation.dto.requests.authencation;

import jakarta.validation.constraints.NotBlank;

import static sn.ods.obi.infrastructure.config.utils.i18n.I18nKeys.CONNEXION_LOGIN_PASSWORD_NON_VIDE;
import static sn.ods.obi.infrastructure.config.utils.i18n.I18nKeys.EMAIL_OBLIGATOIRE;


public record LoginFormDTO(
        @NotBlank(message = EMAIL_OBLIGATOIRE)
        String login,
        @NotBlank(message = CONNEXION_LOGIN_PASSWORD_NON_VIDE)
        String password
) {
}
