package sn.ods.obi.infrastructure.logging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {


    private final ErrorLogRepository errorLogRepository;

    public GlobalExceptionHandler(ErrorLogRepository errorLogRepository) {
        this.errorLogRepository = errorLogRepository;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleAllExceptions(Exception ex, WebRequest request) {
        ErrorLog errorLog = new ErrorLog();
        errorLog.setMessage(ex.getMessage());
        errorLog.setStackTrace(getStackTraceAsString(ex));
        errorLog.setTimestamp(LocalDateTime.now());

        errorLogRepository.save(errorLog);
    }

    private String getStackTraceAsString(Exception ex) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : ex.getStackTrace()) {
            sb.append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}