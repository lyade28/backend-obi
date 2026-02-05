package sn.ods.obi.presentation.dto.dataset;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatasetDTO {
    private Long id;
    private String name;
    private String description;
    private String sourceType;
    private String sourceLabel;
    private Long queryId;
    private Long connexionId;
    private Long importId;
    private Long tenantId;
    private String updatedAt;
}
