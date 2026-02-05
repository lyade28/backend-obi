package sn.ods.obi.infrastructure.config.exceptions;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import sn.ods.obi.presentation.dto.responses.APIMessage;


@Getter
@Setter
public class APIException extends RuntimeException {
    protected String code;

    protected String message;

    protected HttpStatus status;

    protected Object args;

    public APIException(APIMessage message) {
        this.code = message.getCode();
        this.status = HttpStatus.resolve(message.getHttpStatus());
        this.message = message.getMessage();
    }

    public APIException(String message) {
        super(message);
    }

    public APIException(APIMessage message, String msg) {
        this.code = message.getCode();
        this.status = HttpStatus.resolve(message.getHttpStatus());
        this.message = String.format(message.getMessage(), msg);
    }

    public APIException(String code, String message, HttpStatus status) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public APIException(APIMessage message, Object args) {
        this.code = message.getCode();
        this.status = HttpStatus.resolve(message.getHttpStatus());
        this.message = message.getMessage();
        this.args = args;
    }

    @Override
    public String toString() {
        return String.format("MFPAIException: %s - %s", code, message);
    }
}
