package sn.ods.obi.presentation.dto.multitenant;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParametrageDTO {
    private String theme;
    private String fuseau;
    private String devise;
    private String logo;
}
