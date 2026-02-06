package sn.ods.obi.application.services.interfaces;

import sn.ods.obi.presentation.dto.multitenant.ThemeConfigDTO;

import java.util.Optional;

public interface ThemeService {

    /**
     * Récupère le thème du tenant (null si non défini).
     */
    Optional<ThemeConfigDTO> getTheme(Long tenantId);

    /**
     * Enregistre ou met à jour le thème du tenant.
     */
    ThemeConfigDTO saveTheme(Long tenantId, ThemeConfigDTO dto);
}
