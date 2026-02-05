package sn.ods.obi.presentation.dto.import_;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataImportDTO {
    private Long id;
    private String nomFichier;
    private String typeFichier;
    private Long tenantId;
    private Integer rowCount;
    private List<Map<String, String>> columns;
    private String createdAt;
}
