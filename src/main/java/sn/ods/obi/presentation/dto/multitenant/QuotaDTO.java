package sn.ods.obi.presentation.dto.multitenant;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotaDTO {
    private String stockage;
    private String connexions;
    private String utilisateurs;
}
