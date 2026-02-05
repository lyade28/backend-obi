package sn.ods.obi.presentation.dto.multitenant;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDTO {
    private Long id;
    private LocalDateTime date;
    private String action;
    private String utilisateur;
    private String detail;
}
