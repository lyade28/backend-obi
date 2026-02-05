package sn.ods.obi.presentation.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import sn.ods.obi.infrastructure.config.exceptions.APIException;


import java.io.Serializable;
import java.util.Objects;

/**
 * @author Abdou Karim CISSOKHO
 * @created 04/11/2023-22:41
 * @project gestion-courriers
 */

@Data
@SuperBuilder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class APIResponse {

    private boolean success;
    private String message;
    private Object metadata;
    private Object data;
    private String status;
    private Object errors;
    private String url;

    public static APIResponse success(Object data) {
        return APIResponse.builder().success(true).data(data).build();
    }

    public static APIResponse error(APIMessage message) {
        return APIResponse.builder()
                .success(false)
                .status(message.getCode())
                .message(message.getMessage())
                .build();
    }

    public static APIResponse error(APIException mfpaiException) {
        return APIResponse.builder()
                .success(false)
                .status(mfpaiException.getCode())
                .message(mfpaiException.getMessage())
                .build();
    }

    public static APIResponse error(APIMessage message, String s) {
        return APIResponse.builder()
                .success(false)
                .status(message.getCode())
                .message(String.format(message.getMessage(), s))
                .build();
    }

    public APIResponse message(String message) {
        this.message = message;
        return this;
    }

    public APIResponse data(Object data) {
        if (Objects.isNull(this.data)) this.data = data;
        return this;
    }

    public APIResponse errors(Object errors) {
        this.errors = errors;
        return this;
    }

    public APIResponse url(String url) {
        this.url = url;
        return this;
    }

    @Getter
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Builder
    public static class PageMetadata implements Serializable {
        private static final long serialVersionUID = 7156526077883281623L;
        private final int size;
        private final long totalElements;
        private final int totalPages;
        private final int number;

        public PageMetadata(int size, long totalElements, int totalPages, int number) {
            this.size = size;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.number = number;
        }
    }

}
