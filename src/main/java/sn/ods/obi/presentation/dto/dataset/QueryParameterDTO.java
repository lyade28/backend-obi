package sn.ods.obi.presentation.dto.dataset;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryParameterDTO {
    private Long id;
    private String nom;
    private String type;
    private String defaut;
    private Long queryId;
}
