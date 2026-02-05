package sn.ods.obi.presentation.dto.rapport;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RapportDTO {
    private Long id;
    private String nom;
    private String type;
    private String dossier;
    private Long datasetId;
    private Long tenantId;
    private String modifieLe;
    /** Configuration de visualisation (type, source, axes) en JSON */
    private String configJson;
}
