package sn.ods.obi.presentation.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;



@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Builder
public class Response<T> {
    private Status status;
    private T payload;
    private Object errors;
    private Object metadata;
    private Object message;
    private String url;
    private int collectionSize;
    private Object allData;

    public static <T> Response<T> firstConnexion() {
        Response<T> response = new Response<>();
        response.setStatus(Status.INVALIDE_TOKEN);

        return response;
    }

    public static <T> Response<T> disabledAccount() {
        Response<T> response = new Response<>();
        response.setStatus(Status.DISABLED_ACCOUNT);
        return response;
    }

    public static <T> Response<T> badRequest() {
        Response<T> response = new Response<>();
        response.setStatus(Status.BAD_REQUEST);
        return response;
    }


    public static <T> Response<T> invalideUrl() {
        Response<T> response = new Response<>();
        response.setStatus(Status.INVALIDE_URL);
        return response;
    }

    public static <T> Response<T> ok() {
        Response<T> response = new Response<>();
        response.setStatus(Status.OK);
        return response;
    }

    public static <T> Response<T> unauthorized() {
        Response<T> response = new Response<>();
        response.setStatus(Status.UNAUTHORIZED);
        return response;
    }

    public static <T> Response<T> validationException() {
        Response<T> response = new Response<>();
        response.setStatus(Status.VALIDATION_EXCEPTION);
        return response;
    }

    public static <T> Response<T> wrongCredentials() {
        Response<T> response = new Response<>();
        response.setStatus(Status.WRONG_CREDENTIALS);
        return response;
    }

    public static <T> Response<T> accessDenied() {
        Response<T> response = new Response<>();
        response.setStatus(Status.ACCESS_DENIED);
        return response;
    }

    public static <T> Response<T> exception() {
        Response<T> response = new Response<>();
        response.setStatus(Status.EXCEPTION);
        return response;
    }

    public static <T> Response<T> notFound() {
        Response<T> response = new Response<>();
        response.setStatus(Status.NOT_FOUND);
        return response;
    }

    public static <T> Response<T> duplicateEntity() {
        Response<T> response = new Response<>();
        response.setStatus(Status.DUPLICATE_ENTITY);
        return response;
    }

    public static <T> Response<T> duplicateEmail() {
        Response<T> response = new Response<>();
        response.setStatus(Status.DUPLICATE_EMAIL);
        return response;
    }


    public static <T> Response<T> duplicateTelephone() {

        Response<T> response = new Response<>();
        response.setStatus(Status.DUPLICATE_TELEPHONE);
        return response;
    }

    public static <T> Response<T> tokenExpired() {
        Response<T> response = new Response<>();
        response.setStatus(Status.TOKEN_EXPIRED);
        return response;
    }

    public static <T> Response<T> unsupportedMediaType() {
        Response<T> response = new Response<>();
        response.setStatus(Status.UNSUPPORTED_MEDIA_TYPE);
        return response;
    }

    public static <T> Response<T> directoryNotFound() {
        Response<T> response = new Response<>();
        response.setStatus(Status.DIRECTORY_NOT_FOUND);
        return response;
    }

    public static <T> Response<T> methodeNotAllowed() {
        Response<T> response = new Response<>();
        response.setStatus(Status.METHOD_NOT_ALLOWED);
        return response;
    }


    @Getter
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Builder
    public static class PageMetadata implements Serializable{
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
