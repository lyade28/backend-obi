package sn.ods.obi.infrastructure.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;



@Component
@RequiredArgsConstructor
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

  //  private final I18nTranslate i18nTranslate;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(FORBIDDEN.value());
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
       /* mapper.writeValue(outputStream, Response
                .unauthorized()
                .setMessage(i18nTranslate.toTranslate(I18nKeys.UTILISATEUR_NOT_AUTHENTICAD))
                .setErrors(i18nTranslate.toTranslate(I18nKeys.UTILISATEUR_NOT_AUTHENTICAD)));

        */
        outputStream.flush();
    }
}
