package sn.ods.obi.infrastructure.config.exceptions;

import java.time.LocalDateTime;

/**
 * @author Abdou Karim CISSOKHO
 * @created 07/01/2025-11:47
 * @project obi
 */
public record ApiError( String path,
                        String message,
                        int statusCode,
                        LocalDateTime localDateTime) {

}
