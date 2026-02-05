package sn.ods.obi.presentation.dto.dataset;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedQueryDTO {
    private Long id;
    private String nom;
    private String sql;
    private Long connexionId;
    private Long tenantId;
}
