package sn.ods.obi.presentation.dto.connexion;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchemaTableDTO {
    private String connexion;
    private String schema;
    private Integer tables;
    private List<String> tableNames;
}
