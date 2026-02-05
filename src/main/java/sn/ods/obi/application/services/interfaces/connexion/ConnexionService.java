package sn.ods.obi.application.services.interfaces.connexion;

import sn.ods.obi.presentation.dto.connexion.ConnexionDTO;
import sn.ods.obi.presentation.dto.connexion.ConnexionSafeDTO;
import sn.ods.obi.presentation.dto.connexion.SchemaTableDTO;

import java.util.List;
import java.util.Optional;

public interface ConnexionService {
    List<ConnexionSafeDTO> getConnexions(Long tenantId);
    Optional<ConnexionSafeDTO> getConnexion(Long id);
    ConnexionSafeDTO createConnexion(ConnexionDTO dto);
    ConnexionSafeDTO updateConnexion(Long id, ConnexionDTO dto);
    void deleteConnexion(Long id);
    boolean testConnexion(Long id);
    SchemaTableDTO getSchemas(Long id);
}
