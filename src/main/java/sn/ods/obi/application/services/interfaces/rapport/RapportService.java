package sn.ods.obi.application.services.interfaces.rapport;

import sn.ods.obi.presentation.dto.rapport.RapportDTO;

import java.util.List;
import java.util.Optional;

public interface RapportService {
    List<RapportDTO> getRapports(Long tenantId);
    Optional<RapportDTO> getRapport(Long id);
    RapportDTO createRapport(RapportDTO dto);
    RapportDTO updateRapport(Long id, RapportDTO dto);
    void deleteRapport(Long id);
}
