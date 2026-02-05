package sn.ods.obi.presentation.dto.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Abdou Karim CISSOKHO
 * @created 04/11/2023-23:04
 * @project gestion-courriers
 */

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class ValidationRspError {
    private final String field;
    private final String message;
}
